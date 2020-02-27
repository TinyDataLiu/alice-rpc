package com.alice.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义协议，模拟数据传输规范
 *
 * @author liuchun
 * @date 2020/02/27  13:03
 */
@Data
public class Protocol implements Serializable {
    private static final long serialVersionUID = 6074885029101980027L;
    /**
     * 目标服务类名
     */
    private String className;
    /**
     * 目标服务方法名
     */
    private String methodName;
    /**
     * 参数列表
     */
    private Class<?>[] types;
    /**
     * 参数
     */
    private Object[] args;
}
