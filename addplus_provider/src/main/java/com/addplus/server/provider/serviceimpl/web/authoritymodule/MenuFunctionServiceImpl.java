package com.addplus.server.provider.serviceimpl.web.authoritymodule;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.*;
import com.addplus.server.api.model.authority.SysMenuFunction;
import com.addplus.server.api.model.authority.SysRoleMenuFunction;
import com.addplus.server.api.model.authority.ext.SysMenuFunctionUser;
import com.addplus.server.api.model.authority.ext.SysRoleMenuFunctionUser;
import com.addplus.server.api.service.web.authoritymodule.MenuFunctionService;
import com.addplus.server.api.service.web.authoritymodule.RoleMenuFunctionService;
import com.addplus.server.api.utils.DataUtils;
import com.addplus.server.api.utils.MenuFunctionTreeUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类名: MenuFunctionServiceImpl
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/4 下午1:34
 * @description 类描述: 菜单功能实现类
 */
@Service(interfaceClass = MenuFunctionService.class)
public class MenuFunctionServiceImpl implements MenuFunctionService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysMenuFunctionMapper sysMenuFunctionMapper;

    @Autowired
    private SysMenuElementMapper sysMenuElementMapper;

    @Autowired
    private SysRoleMenuElementMapper sysRoleMenuElementMapper;

    @Autowired
    private SysRoleMenuFunctionMapper sysRoleMenufunctionMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RoleMenuFunctionService roleMenufunctionService;


    @Override
    public SysMenuFunction getMenuFunction(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysMenuFunction sysMenuFunction = new SysMenuFunction();
        sysMenuFunction.setId(id);
        sysMenuFunction.setIsDeleted(0);
        SysMenuFunction sysMenuFunctionNew = sysMenuFunctionMapper.selectById(id);
        if (sysMenuFunctionNew == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
        return sysMenuFunctionNew;
    }

    @Override
    public Page<SysMenuFunctionUser> getMenuPageByType(Integer pageNo, Integer pageSize, Integer type) throws Exception {
        Page page = new Page(pageNo, pageSize);
        List<SysMenuFunction> sysMenuFunctionList = sysMenuFunctionMapper.selectMenuFunctionOrderSort(page, type);
        if (sysMenuFunctionList != null && !sysMenuFunctionList.isEmpty()) {
            List<SysMenuFunction> sysMenuFunctions = new ArrayList<SysMenuFunction>();
            for (SysMenuFunction sysMenuFunction : sysMenuFunctionList) {
                List<SysMenuFunction> sysMenuFunctionCliderList = sysMenuFunctionMapper.selectMenuFunctionTreeById(sysMenuFunction.getId());
                if (sysMenuFunctionCliderList != null && !sysMenuFunctionCliderList.isEmpty()) {
                    sysMenuFunctions.addAll(sysMenuFunctionCliderList);
                }
            }
            List<SysMenuFunctionUser> menuTree = MenuFunctionTreeUtils.getMenuFunctionTree(sysMenuFunctions);
            if (menuTree.isEmpty()) {
                throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
            }
            page.setOptimizeCountSql(true);
            page.setRecords(menuTree);
            return page;
        } else {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
    }

    @Override
    public Page<SysMenuFunctionUser> getMenuFunctionByPage(Integer pageNo, Integer pageSize) throws Exception {
        Page page = new Page(pageNo, pageSize);
        Integer type = Integer.valueOf(RpcContext.getContext().getAttachment("loginType"));
        List<SysMenuFunction> sysMenuFunctionList = sysMenuFunctionMapper.selectMenuFunctionOrderSort(page, type);
        if (sysMenuFunctionList != null && !sysMenuFunctionList.isEmpty()) {
            List<SysMenuFunction> sysMenuFunctions = new ArrayList<SysMenuFunction>();
            for (SysMenuFunction sysMenuFunction : sysMenuFunctionList) {
                List<SysMenuFunction> sysMenuFunctionCliderList = sysMenuFunctionMapper.selectMenuFunctionTreeById(sysMenuFunction.getId());
                if (sysMenuFunctionCliderList != null && !sysMenuFunctionCliderList.isEmpty()) {
                    sysMenuFunctions.addAll(sysMenuFunctionCliderList);
                }
            }
            List<SysMenuFunctionUser> menuTree = MenuFunctionTreeUtils.getMenuFunctionTree(sysMenuFunctions);
            if (menuTree.isEmpty()) {
                throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
            }
            page.setOptimizeCountSql(true);
            page.setRecords(menuTree);
            return page;
        } else {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
    }

    @Override
    public Page<SysRoleMenuFunctionUser> getMenuFunctionByPageWithRole(Integer pageNo, Integer pageSize, Long roleId) throws Exception {
        if (roleId == null || roleId <= 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        // 获取roleId角色拥有的菜单权限数组
        List<SysRoleMenuFunction> sysRoleMenufunctionList = roleMenufunctionService.getRoleMenuByRoleId(roleId);
        Page page = new Page(pageNo, pageSize);
        Integer type = sysRoleMapper.selectById(roleId).getType();
        List<SysMenuFunction> sysMenuFunctionList = sysMenuFunctionMapper.selectMenuFunctionOrderSort(page, type);
        if (sysMenuFunctionList != null && !sysMenuFunctionList.isEmpty()) {
            List<SysMenuFunction> sysMenuFunctions = new ArrayList<SysMenuFunction>();

            for (SysMenuFunction sysMenuFunction : sysMenuFunctionList) {
                List<SysMenuFunction> sysMenuFunctionCliderList = sysMenuFunctionMapper.selectMenuFunctionTreeById(sysMenuFunction.getId());
                if (sysMenuFunctionCliderList != null && !sysMenuFunctionCliderList.isEmpty()) {
                    sysMenuFunctions.addAll(sysMenuFunctionCliderList);
                }
            }

            List<SysMenuFunctionUser> menuTree = MenuFunctionTreeUtils.getMenuFunctionTree(sysMenuFunctions);
            List<SysRoleMenuFunctionUser> roleMenuTree = new ArrayList<SysRoleMenuFunctionUser>();

            for (int i = 0; i < menuTree.size(); i++) {
                SysRoleMenuFunctionUser roleMenuUser = new SysRoleMenuFunctionUser();
                SysMenuFunctionUser menuFunction = menuTree.get(i);
                if (menuFunction.getMenuFunctionChilders() != null && menuFunction.getMenuFunctionChilders().size() > 0) { // 是否有子数组
                    Boolean childrenCheckedFull = true; // 子数组中是所有元素都选中--用于判断父菜单是否做全选标志
                    Boolean childrenCheckedNone = true; // 子数组中是所有元素都未选中--用于判断父菜单是否做半选标志

                    List<SysRoleMenuFunctionUser> childrenList = new ArrayList<SysRoleMenuFunctionUser>();
                    for (int j = 0; j < menuFunction.getMenuFunctionChilders().size(); j++) {
                        SysRoleMenuFunctionUser subRoleMenu = new SysRoleMenuFunctionUser();
                        SysMenuFunctionUser subMenu = menuFunction.getMenuFunctionChilders().get(j);
                        // 加checked
                        if (sysRoleMenufunctionList != null && !sysRoleMenufunctionList.isEmpty()) {
                            subRoleMenu.setChecked(this.contains(sysRoleMenufunctionList, subMenu.getId()));
                            childrenCheckedNone = false;
                        } else {
                            subRoleMenu.setChecked(false);
                            childrenCheckedFull = false;
                        }
                        subRoleMenu.setName(subMenu.getName());
                        subRoleMenu.setId(subMenu.getId());

                        childrenList.add(subRoleMenu);
                    }

                    // 给父菜单加checked 和 halfChecked标志
                    if (childrenCheckedFull == true || childrenCheckedNone == true) {
                        roleMenuUser.setHalfChecked(false);
                    } else {
                        roleMenuUser.setHalfChecked(true);
                    }

                    // 加checked
                    if (sysRoleMenufunctionList != null && !sysRoleMenufunctionList.isEmpty()) {
                        roleMenuUser.setChecked(this.contains(sysRoleMenufunctionList, menuFunction.getId()));
                    } else {
                        roleMenuUser.setChecked(false);
                    }


                    roleMenuUser.setId(menuFunction.getId());
                    roleMenuUser.setName(menuFunction.getName());
                    roleMenuUser.setChildren(childrenList);
                } else {
                    // 加checked
                    if (sysRoleMenufunctionList != null && !sysRoleMenufunctionList.isEmpty()) {
                        roleMenuUser.setChecked(this.contains(sysRoleMenufunctionList, menuFunction.getId()));
                    } else {
                        roleMenuUser.setChecked(false);
                    }
                    // 判断是否选中
                    roleMenuUser.setId(menuFunction.getId());
                    roleMenuUser.setName(menuFunction.getName());
                }

                roleMenuTree.add(roleMenuUser);
            }

            if (roleMenuTree.isEmpty()) {
                throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
            }
            page.setOptimizeCountSql(true);
            page.setRecords(roleMenuTree);
            return page;
        } else {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }

    }

    private Boolean contains(List<SysRoleMenuFunction> list, Long obj) {
        Integer size = list.size();
        for (SysRoleMenuFunction roleMenu : list
        ) {
            if (roleMenu.getmId().equals(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean updateMenuFunctionById(SysMenuFunction sysMenuFunction) throws Exception {
        if (sysMenuFunction == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        if (sysMenuFunction.getId() == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        if (sysMenuFunction.getPid() != null) {
            if (-1 == sysMenuFunction.getPid()) {
                sysMenuFunction.setPath(sysMenuFunction.getId().toString());
            } else {
                SysMenuFunction sysMenuFunctionP = new SysMenuFunction();
                sysMenuFunctionP.setId(sysMenuFunction.getPid());
                sysMenuFunctionP.setIsDeleted(0);
                sysMenuFunctionP = sysMenuFunctionMapper.selectOne(new QueryWrapper<>(sysMenuFunctionP));
                String path = null;
                if (sysMenuFunctionP != null && StringUtils.isNotBlank(sysMenuFunctionP.getPath())) {
                    path = sysMenuFunctionP.getPath().concat("," + sysMenuFunction.getId());
                } else {
                    path = sysMenuFunction.getId().toString();
                }
                sysMenuFunction.setType(sysMenuFunctionP.getType());
                sysMenuFunction.setPath(path);
            }
        }
        sysMenuFunction.setGmtModified(new Date());
        String modifyUser =  RpcContext.getContext().getAttachment("id");
        sysMenuFunction.setModifyUser(modifyUser);
        int count = sysMenuFunctionMapper.updateById(sysMenuFunction);
        if (count > 0) {
            if (StringUtils.isNotBlank(sysMenuFunction.getUrl())) {
                //更新内容shrio里面内容
                redisTemplate.convertAndSend(StringConstant.REDIS_SHRIO_PERMISSION_TPOIC, StringConstant.REDIS_SHRIO_PERMISSION_TPOIC);
            }
            return true;
        } else {
            throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL);
        }
    }

    @Override
    @Transactional
    public Boolean deleteMenuFunctionById(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        List<Integer> mIdList = sysMenuFunctionMapper.getMenuFunctionChilders(id);
        if (mIdList != null && !mIdList.isEmpty()) {
            int count = sysMenuFunctionMapper.updateLogicallyDeleteChilders(id);
            if (count > 0) {
                //关联逻辑删除菜单_角色关联的按钮
                sysRoleMenufunctionMapper.updateRoleFunctionDeleteByeId(mIdList);
                //关联逻辑删除当前菜单拥有的按钮
                int eCount = sysMenuElementMapper.updateMenuElementDelete(mIdList);
                if (eCount > 0) {
                    //逻辑删除角色_按钮
                    sysRoleMenuElementMapper.updateLogicallyDeleteBymenuElementId(mIdList);
                }
                return true;
            }
        }
        //暂无可更新内容
        throw new ErrorException(ErrorCode.SYS_ERROR_NOT_AVAILABLE);
    }

    @Override
    @Transactional
    public SysMenuFunction addMenuFunction(SysMenuFunction sysMenuFunction) throws Exception {
        if (sysMenuFunction == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        sysMenuFunction.setIsDeleted(0);
        int count = sysMenuFunctionMapper.selectCount(new QueryWrapper<>(sysMenuFunction));
        if (count < 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
        Date date = new Date();
        sysMenuFunction.setGmtCreate(date);
        sysMenuFunction.setGmtModified(date);
        String modifyUser =  RpcContext.getContext().getAttachment("id");
        sysMenuFunction.setModifyUser(modifyUser);
        sysMenuFunction.setPath("-");
        count = sysMenuFunctionMapper.insert(sysMenuFunction);
        if (count > 0) {
            //更新path
            if (-1 == sysMenuFunction.getPid()) {
                sysMenuFunction.setPath(sysMenuFunction.getId().toString());
            } else {
                SysMenuFunction sysMenuFunctionP = new SysMenuFunction();
                sysMenuFunctionP.setId(sysMenuFunction.getPid());
                sysMenuFunctionP.setIsDeleted(0);
                sysMenuFunctionP = sysMenuFunctionMapper.selectOne(new QueryWrapper<>(sysMenuFunctionP));
                String path = null;
                if (sysMenuFunctionP != null && StringUtils.isNotBlank(sysMenuFunctionP.getPath())) {
                    path = sysMenuFunctionP.getPath().concat("," + sysMenuFunction.getId());
                } else {
                    path = sysMenuFunction.getId().toString();
                }
                sysMenuFunction.setType(sysMenuFunctionP.getType());
                sysMenuFunction.setPath(path);

            }
            sysMenuFunctionMapper.updateById(sysMenuFunction);
            return sysMenuFunction;
        } else {
            throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL);
        }
    }

    @Override
    public Page<SysMenuFunction> searchMenuFunctionByName(String name, Integer pageNo, Integer pageSize, String pid) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(pageNo, pageSize)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Page<SysMenuFunction> page = new Page<SysMenuFunction>(pageNo, pageSize);
        List<SysMenuFunction> sysMenuFunctionList = sysMenuFunctionMapper.searchMenuFunctionByName(page, pid, name);
        if (sysMenuFunctionList != null && !sysMenuFunctionList.isEmpty()) {
            page.setRecords(sysMenuFunctionList);
            return page;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
    }

    @Override
    public Page<SysMenuFunction> getMenuFunctionListByPage(Integer pageNo, Integer pageSize) throws Exception {
        Page<SysMenuFunction> page = new Page<>(pageNo, pageSize);
        page = (Page<SysMenuFunction>) sysMenuFunctionMapper.selectPage(page, null);
        if (page != null) {
            return page;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
    }

    @Override
    public Integer refreshMenu() {
        redisTemplate.convertAndSend(StringConstant.REDIS_SHRIO_PERMISSION_TPOIC, StringConstant.REDIS_SHRIO_PERMISSION_TPOIC);
        return 1;
    }

    @Override
    public Boolean refreshAuthority() throws Exception {
        //更新内容shrio里面内容
        redisTemplate.convertAndSend(StringConstant.REDIS_SHRIO_PERMISSION_TPOIC, StringConstant.REDIS_SHRIO_PERMISSION_TPOIC);
        return true;
    }
}
