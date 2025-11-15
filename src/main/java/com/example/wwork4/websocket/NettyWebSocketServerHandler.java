package com.example.wwork4.websocket;

import com.example.wwork4.utils.ChannelContextUtils;
import com.example.wwork4.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.internal.ChannelUtils;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
   private  static final Logger logger=  LoggerFactory.getLogger(NettyWebSocketServerHandler.class);
    @Resource
    ChannelContextUtils channelContextUtils;
   @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {


         channelContextUtils.receiveMessage(channelHandlerContext,textWebSocketFrame);

    }

    /**
     * 通道就绪后 调用，一般用户来做初始化。
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有新的连接加入 。。。");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有连接断开...");
        channelContextUtils.removeContext(ctx.channel());
//        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("收到事件: {}", evt.getClass().getName());
        if(evt instanceof IdleStateEvent){
            IdleStateEvent e=(IdleStateEvent) evt;
            if(e.state()== IdleState.READER_IDLE) {
                logger.info("心跳超时");
                ctx.close();
            }else if(e.state()==IdleState.WRITER_IDLE){
                ctx.writeAndFlush("heart");
            }
        }
          if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            WebSocketServerProtocolHandler.HandshakeComplete complete=(WebSocketServerProtocolHandler.HandshakeComplete)evt;
            String url=complete.requestUri();
            String token =getToken(url);
            if(token==null){
                ctx.channel().close();
                return;
            }
            logger.info("url()",url);
              Claims claims= JwtUtils.parseJWT(token);
              String userId=claims.get("id", Integer.class).toString();
            channelContextUtils.addContext(userId,ctx.channel());
        }
        super.userEventTriggered(ctx, evt);
    }

    private String getToken(String url){
        if (url==null|| !url.contains("?")){
            return null;
        }
        String [] queryParams =url.split("\\?");
        if(queryParams.length!=2){
            return null;
        }
        String [] params=queryParams[1].split("=");
        if(params.length!=2){
            return  null;
        }
        return params[1];
    }

}
