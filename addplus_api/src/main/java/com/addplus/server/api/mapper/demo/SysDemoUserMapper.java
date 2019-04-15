package com.addplus.server.api.mapper.demo;

import com.addplus.server.api.model.demo.SysDemoUser;
import com.addplus.server.api.utils.BaseAddMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SysDemoUserMapper extends BaseAddMapper<SysDemoUser> {

    /**
     * - property = “demoAddress”, 表示要将返回的查询结果赋值给DemoUser的demoAddress属性
     * - column = “demo_address_id” 是指将DemoUser表中的demo_address_id作为com.addplus.townmall.server.api.mapper.demo.SysDemoAddressMapper.findAddressById的查询参数
     * - one 表示这是一个一对一的查询
     * - @One(select = “方法全路径) 表示我们调用的方法
     */
    @Select("SELECT * FROM `sys_demo_user` where id = #{id}")
    @Results({
            @Result(property = "demoAddress",column = "demo_address_id",
            one = @One(select="com.addplus.server.api.mapper.demo.SysDemoAddressMapper.findAddressById"))
    })
    SysDemoUser findUserWithDemoAddress(Integer id);

    /**
     * - property = “cars”, 表示要将返回的查询结果赋值给demo_user的cars属性
     * - column = “id” 是指将demo_user表中的用户主键id作为com.addplus.townmall.server.api.mapper.demo.SysDemoCarMapper.findCarByUserId的查询参数
     * - many 表示这是一个一对多的查询
     * - @Many(select = “方法全路径) 表示我们调用的方法, 方法参数userId就是上面column指定的列值

     */
    @Select("SELECT * FROM `sys_demo_user` where id = #{id}")
    @Results({
            @Result(property = "cars",column = "id",
            many = @Many(select = "com.addplus.server.api.mapper.demo.SysDemoCarMapper.findCarByUserId")
            )
    })
    SysDemoUser getDemoUserWithDemoCar(Integer id);

    @Select("SELECT * FROM `sys_demo_user` where id = #{id}")
    @Results({
            @Result(property = "demoAddress",column = "demo_address_id",
                    one = @One(select="com.addplus.server.api.mapper.demo.SysDemoAddressMapper.findAddressById")),
            @Result(property = "cars",column = "id",
                    many = @Many(select = "com.addplus.server.api.mapper.demo.SysDemoCarMapper.findCarByUserId")
            )
    })
    SysDemoUser getDemoUserWithDemoCarAndDemoAddress(Integer id);


    @Select("SELECT * FROM sys_demo_user")
    @Results({
            @Result(property = "demoAddress",column = "demo_address_id",
                    one = @One(select="com.addplus.server.api.mapper.demo.SysDemoAddressMapper.findAddressById")),
            @Result(property = "cars",column = "id",
                    many = @Many(select = "com.addplus.server.api.mapper.demo.SysDemoCarMapper.findCarByUserId")
            )
    })
    List<SysDemoUser> getDemoUserList();


    List<SysDemoUser> testXmlDemoUser();
}
