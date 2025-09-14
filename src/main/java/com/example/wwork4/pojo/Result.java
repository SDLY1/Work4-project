package com.example.wwork4.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Base base;
//    private Integer code;
//    private  String msg;
    private Object data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Base {
        private Integer code;
        private String msg;

    }


    public  static  Result success(){
        return  new Result(new Base(10000,"success"),null);
    }
    public static Result success(Object data) {
        return new Result(new Base(10000, "success"), data);
    }

    public static Result error(String msg) {
        return new Result(new Base(-1, msg), null);
    }
}
