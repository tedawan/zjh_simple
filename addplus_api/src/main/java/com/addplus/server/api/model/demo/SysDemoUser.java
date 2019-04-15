package com.addplus.server.api.model.demo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SysDemoUser implements Serializable {
    private static final long serialVersionUID = 8095760544534811208L;
    private Integer id;
    private String nickName;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
    //地址信息，和用户是一对一的关系
    private SysDemoAddress demoAddress;
    //地址id
    private Integer demoAddressId;
    //用户拥有的车，和用户是一对多的关系
    private List<SysDemoCar> cars;
    private Date createDate;
}
