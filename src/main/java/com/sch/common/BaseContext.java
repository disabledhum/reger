package com.sch.common;

import java.nio.file.LinkOption;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setId(Long id){
        threadLocal.set(id);
    }


    public static Long getId(){
        return threadLocal.get();
    }
}
