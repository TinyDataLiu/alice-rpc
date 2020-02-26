package com.alice.rpc.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuchun
 * @date 2020/02/26  12:13
 */
@Data
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = -7602022276473026926L;
    private int id;
    private String name;
}
