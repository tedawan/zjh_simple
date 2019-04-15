package com.addplus.server.shiro.utils;

import com.addplus.server.api.model.authority.SysUser;
import org.apache.shiro.SecurityUtils;

import java.util.List;

/**
 * 类名: ShiroUtils
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/2/26 下午2:03
 * @description 类描述: shiro获取本身一些信息工具类
 */
public class ShiroUtils {

    /**
      * 方法描述：返回当前登录用户主键id,没有则返回null
      *
      * @author zhangjiehang
      * @param
      * @return
      * @date 2018/2/26 下午2:05
      * @throws Exception
      */
    public static Long getUserId(){
        Long userId = (Long) SecurityUtils.getSubject().getSession().getAttribute("id");
        return userId;
    }

    /**
     * 方法描述：获取当前登录用户所属的登录登录
     *
     * @return (0 : 系统 1 : 普通用户)
     * @throws Exception
     * @author zhangjiehang
     * @date 2018/10/27 6:33 PM
     */
    public static Integer getLoginType() {
        Integer loginType = (Integer) SecurityUtils.getSubject().getSession().getAttribute("loginType");
        return loginType;
    }

    /**
      * 方法描述：获取当前登录用户角色列表
      *
      * @author zhangjiehang
      * @param
      * @return
      * @date 2018/2/26 下午2:08
      * @throws Exception
      */
    public static List<Integer> getUserRoleIdList(){
        List<Integer> list = (List<Integer>) SecurityUtils.getSubject().getSession().getAttribute("id");
        return list;
    }

    /**
      * 方法描述：获取当前用户的所有信息
      *
      * @author zhangjiehang
      * @param
      * @return
      * @date 2018/2/26 下午2:10
      * @throws Exception
      */
    public static SysUser getUser(){
       return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }


}
