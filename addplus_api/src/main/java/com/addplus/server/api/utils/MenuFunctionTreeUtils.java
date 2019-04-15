package com.addplus.server.api.utils;

import com.addplus.server.api.model.authority.SysMenuFunction;
import com.addplus.server.api.model.authority.ext.SysMenuFunctionUser;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * 类名: MenuFunctionTreeUtils
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/5 下午3:29
 * @description 类描述: 菜单树形工具类
 */
public class MenuFunctionTreeUtils {

    /**
     * 方法描述：后置遍历菜单树形，从最低层向上进行遍历。
     *
     * @param sysMenuFunctions
     * @return
     * @throws Exception
     * @author zhangjiehang
     * @date 2017/12/5 下午5:20
     */
    public static List<SysMenuFunctionUser> getMenuFunctionTree(List<SysMenuFunction> sysMenuFunctions) {
        if (sysMenuFunctions != null && !sysMenuFunctions.isEmpty()) {
            List<Long> menuList = new ArrayList<>();
            HashMap<Long, SysMenuFunctionUser> hashMap = new HashMap<>();
            SysMenuFunctionUser menuFunctionUser = null;
            for (SysMenuFunction sysMenuFunction : sysMenuFunctions) {
                menuFunctionUser = new SysMenuFunctionUser();
                BeanUtils.copyProperties(sysMenuFunction, menuFunctionUser, SysMenuFunction.class);
                hashMap.put(sysMenuFunction.getId(), menuFunctionUser);
                menuList.add(sysMenuFunction.getId());
            }
            for (Long num : menuList) {
                menuFunctionUser = hashMap.get(num);
                if (hashMap.containsKey(menuFunctionUser.getPid())) {
                    SysMenuFunctionUser menuFunctionUserFather = hashMap.get(menuFunctionUser.getPid());
                    List<SysMenuFunctionUser> functionChirldrenList = null;
                    if (menuFunctionUserFather.getMenuFunctionChilders() != null && !menuFunctionUserFather.getMenuFunctionChilders().isEmpty()) {
                        functionChirldrenList = menuFunctionUserFather.getMenuFunctionChilders();
                    } else {
                        functionChirldrenList = new ArrayList<SysMenuFunctionUser>();
                    }
                    functionChirldrenList.add(menuFunctionUser);
                    menuFunctionUserFather.setMenuFunctionChilders(functionChirldrenList);
                    hashMap.put(menuFunctionUserFather.getId(), menuFunctionUserFather);
                    hashMap.remove(menuFunctionUser.getId());
                }
            }
            List<SysMenuFunctionUser> menuTree = new ArrayList<SysMenuFunctionUser>();
            for (Map.Entry<Long, SysMenuFunctionUser> o : hashMap.entrySet()) {
                menuTree.add(o.getValue());
            }
            if (!menuTree.isEmpty()) {
                Collections.sort(menuTree);
                //进行排序
                for (SysMenuFunctionUser functionUser : menuTree) {
                    List<SysMenuFunctionUser> menuFunctionChilders = functionUser.getMenuFunctionChilders();
                    if (menuFunctionChilders != null && !menuFunctionChilders.isEmpty()) {
                        Collections.sort(menuFunctionChilders);
                    }
                }
                return menuTree;
            }
        }
        return null;
    }

}
