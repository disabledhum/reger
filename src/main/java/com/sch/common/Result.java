package com.sch.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public  static <T> Result<T> success(T data){
                Result<T> result=new Result<>();
                result.data=data;
                result.code=1;
                return result;
    }

    public  static  <T> Result<T> error(String msg){
            Result<T> result=new Result<>();
            result.code=0;
            result.msg=msg;
            return result;
    }
}
