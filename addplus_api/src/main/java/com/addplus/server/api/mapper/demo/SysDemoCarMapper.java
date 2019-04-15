package com.addplus.server.api.mapper.demo;

import com.addplus.server.api.model.demo.SysDemoCar;
import com.addplus.server.api.utils.BaseAddMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysDemoCarMapper extends BaseAddMapper<SysDemoCar> {
    /**
     * 根据用户id查询所有的车
     */
    @Select("SELECT * FROM `sys_demo_car` WHERE demo_user_id = #{demo_user_id}")
    List<SysDemoCar> findCarByUserId(Integer demoUserId);
}
