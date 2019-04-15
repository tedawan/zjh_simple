package com.addplus.server.api.model.demo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysDemoData implements Serializable {
    private static final long serialVersionUID = 8095760544534811208L;
    private Integer id;
    private String dataName;
}
