package com.addplus.server.api.model.demo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysDemoCar implements Serializable {
    private static final long serialVersionUID = -4938705749057952542L;
    private Integer id;
    private String color;
    private String name;
    //用户id
    private Long demoUserId;
}
