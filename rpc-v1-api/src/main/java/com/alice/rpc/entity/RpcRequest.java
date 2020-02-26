package com.alice.rpc.entity;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 用于封装请求参数
 *
 * @author liuchun
 * @date 2020/02/26  9:33
 */
@Data
public class RpcRequest implements Serializable{

    private static final long serialVersionUID = 6787104902491036503L;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法
     */
    private String method;


    /**
     * 参数
     */
    private Object[] args;
}
