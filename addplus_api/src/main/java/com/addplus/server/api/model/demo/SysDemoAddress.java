package com.addplus.server.api.model.demo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysDemoAddress implements Serializable {
    private static final long serialVersionUID = -360489577307283923L;
    private Integer id;
    private String province;
    private String city;
}
