package com.addplus.server.api.model.authority.ext;

import com.addplus.server.api.model.authority.SysMenuFunction;

import java.util.List;

/**
 * 类名: SysMenuFunctionUser
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/4 下午8:29
 * @description 类描述: 用户返回实体类
 */
public class SysMenuFunctionUser extends SysMenuFunction implements Comparable<SysMenuFunctionUser> {

    private static final long serialVersionUID = -5219697408128990212L;

    private List<SysMenuFunctionUser> menuFunctionChilders;

    public List<SysMenuFunctionUser> getMenuFunctionChilders() {
        return menuFunctionChilders;
    }

    public void setMenuFunctionChilders(List<SysMenuFunctionUser> menuFunctionChilders) {
        this.menuFunctionChilders = menuFunctionChilders;
    }

    @Override
    public int compareTo(SysMenuFunctionUser o) {
        int oSort = o.getSort();
        if (this.getSort() > oSort) {
            return -1;
        }else if (this.getSort() < oSort) {
            return 1;
        }else{
            long id = o.getId();
            if(this.getId()>id){
                return 1;
            }else if(this.getId()<id){
                return -1;
            }
        }
        return 0;
    }
}
