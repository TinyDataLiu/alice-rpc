package com.alice.rpc.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuchun
 * @date 2020/02/26  11:58
 */
@Data
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -5888019852315516890L;
    private String className;
    private String methodName;
    private Object[] args;
}
