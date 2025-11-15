package com.example.wwork4.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NettyWebSocketServer {
    private  static final Logger logger=  LoggerFactory.getLogger(NettyWebSocketServerHandler.class);

    private static EventLoopGroup bossGroup =new NioEventLoopGroup(1);

    private static EventLoopGroup workGroup =new NioEventLoopGroup();
    @Resource()
    private NettyWebSocketServerHandler nettyWebSocketServerHandler;
    @PreDestroy
    public void close(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    @PostConstruct
    public  void start(){
        logger.info("SpringBoot启动完成，开始启动Netty服务器...");

        // ✅ 在独立线程中启动Netty
        new Thread(() -> {
            try {
                run();
            } catch (Exception e) {
                logger.error("Netty启动失败", e);
            }
        }, "netty-server").start();

    }

    public void  run(){
        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
            serverBootstrap.childHandler(new ChannelInitializer<>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    //设置几个重要处理器
                    //对Http协议的支持，使用http的编码器，解码器
                    pipeline.addLast(new HttpServerCodec());
                    //聚合解码 http-request
                    //保证接收的http请求的完整性
                    pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                    //心跳 long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit
                    //readerIdleTime 读超时时间
                    //writerIdleTime 写超时时间
                    //allIdleTime 所有类型的超时时间
                    pipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));

                    //将Http协议升级为ws协议
                    pipeline.addLast(new WebSocketServerProtocolHandler("/ws",null,true,655360,true,
                            true,10000L));
                    pipeline.addLast(nettyWebSocketServerHandler);

                }
            });
            ChannelFuture channelFuture= serverBootstrap.bind(5051).sync();
            logger.info("netty成功启动");
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            logger.info("启动错误",e);
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }


}
