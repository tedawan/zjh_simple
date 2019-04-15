package com.addplus.server.api.mapper.demo;

import com.addplus.server.api.model.demo.SysDemoAddress;
import com.addplus.server.api.utils.BaseAddMapper;
import org.apache.ibatis.annotations.Select;

public interface SysDemoAddressMapper extends BaseAddMapper<SysDemoAddress> {

    /**
     * 根据地址id查询地址
     */
    @Select("SELECT * FROM `sys_demo_address` WHERE id = #{id}")
    SysDemoAddress findAddressById(Integer id);
}
