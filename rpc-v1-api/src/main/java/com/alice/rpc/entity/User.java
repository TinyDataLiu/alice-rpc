package com.alice.rpc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuchun
 * @date 2020/02/26  9:21
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 2451781534181852184L;
    private int id;
    private String name;

}
