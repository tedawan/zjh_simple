package com.addplus.server.provider.serviceimpl.web.authoritymodule;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysRoleMenuFunctionMapper;
import com.addplus.server.api.model.authority.SysRoleMenuFunction;
import com.addplus.server.api.model.authority.ext.SysRoleMenuFunctionExt;
import com.addplus.server.api.service.web.authoritymodule.RoleMenuFunctionService;
import com.addplus.server.api.utils.DataUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类名: RoleMenufunctionService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/11 下午1:48
 * @description 类描述: 角色-功能关联实现类
 */
@Service(interfaceClass = RoleMenuFunctionService.class)
public class RoleMenuFunctionServiceImpl implements RoleMenuFunctionService {

    @Autowired
    private SysRoleMenuFunctionMapper sysRoleMenufunctionMapper;


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<SysRoleMenuFunction> getRoleMenuByRoleId(Long id) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(id)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysRoleMenuFunction roleMenu = new SysRoleMenuFunction();
        roleMenu.setrId(id);
        roleMenu.setIsDeleted(0);
        List<SysRoleMenuFunction> sysRoleMenufunctionList = sysRoleMenufunctionMapper.selectList(new QueryWrapper<SysRoleMenuFunction>(roleMenu));
        if (sysRoleMenufunctionList == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }

        return sysRoleMenufunctionList;
    }


    @Override
    public List<SysRoleMenuFunction> getRoleMenuByRoleAndMenuId(Long menuId, Long roleId) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(roleId, menuId)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysRoleMenuFunction roleMenu = new SysRoleMenuFunction();
        roleMenu.setrId(roleId);
        roleMenu.setmId(menuId);
        roleMenu.setIsDeleted(0);
        List<SysRoleMenuFunction> sysRoleMenufunctionList = sysRoleMenufunctionMapper.selectList(new QueryWrapper<SysRoleMenuFunction>(roleMenu));
        if (sysRoleMenufunctionList == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }

        return sysRoleMenufunctionList;
    }

    @Override
    public Boolean addRoleMenufunction(SysRoleMenuFunction sysRoleMenufunction) throws Exception {
        if (sysRoleMenufunction == null || DataUtils.EmptyOrNegativeOrZero(sysRoleMenufunction.getmId(), sysRoleMenufunction.getrId())) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        sysRoleMenufunction.setIsDeleted(0);
        Date date = new Date();
        sysRoleMenufunction.setGmtCreate(date);
        sysRoleMenufunction.setGmtModified(date);
        String modifyUser =  RpcContext.getContext().getAttachment("id");
        sysRoleMenufunction.setModifyUser(modifyUser);
        int count = sysRoleMenufunctionMapper.insert(sysRoleMenufunction);
        if (count > 0) {
            //更新内容shrio里面内容
            redisTemplate.convertAndSend(StringConstant.REDIS_SHRIO_PERMISSION_TPOIC,StringConstant.REDIS_SHRIO_PERMISSION_TPOIC);
            return true;
        } else {
            throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL, false);
        }
    }

    @Override
    public Boolean logicalDeletedRoleMenufunctionById(Long id) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(id)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysRoleMenuFunction sysRoleMenufunction = new SysRoleMenuFunction();
        sysRoleMenufunction.setId(id);
        sysRoleMenufunction.setIsDeleted(0);
        int count = sysRoleMenufunctionMapper.selectCount(new QueryWrapper<SysRoleMenuFunction>(sysRoleMenufunction));
        if (count > 0) {
            sysRoleMenufunction.setIsDeleted(1);
            sysRoleMenufunction.setGmtModified(new Date());
            String modifyUser =  RpcContext.getContext().getAttachment("id");
            sysRoleMenufunction.setModifyUser(modifyUser);
            count = sysRoleMenufunctionMapper.updateById(sysRoleMenufunction);
            if (count > 0) {
                //更新内容shrio里面内容
                redisTemplate.convertAndSend(StringConstant.REDIS_SHRIO_PERMISSION_TPOIC, StringConstant.REDIS_SHRIO_PERMISSION_TPOIC);
                return true;
            } else {
                throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL, false);
            }
        } else {
            throw new ErrorException(ErrorCode.SYS_ERROR_NOT_AVAILABLE);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRoleMenuFunction(SysRoleMenuFunctionExt sysRoleMenuFunctionExt) throws Exception {
        if (sysRoleMenuFunctionExt == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        if (DataUtils.EmptyOrNegativeOrZero(sysRoleMenuFunctionExt.getRoleId())) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        if (sysRoleMenuFunctionExt.getmId() != null && sysRoleMenuFunctionExt.getmId().length > 0) {
            List<SysRoleMenuFunction> sysRoleMenuFunctionList = getRoleMenuByRoleId(sysRoleMenuFunctionExt.getRoleId());
            List<Long> addList = new ArrayList<>();
            List<Long> delList = new ArrayList<>();
            for (Long mId : sysRoleMenuFunctionExt.getmId()) {
                boolean res = true;
                for (SysRoleMenuFunction r : sysRoleMenuFunctionList) {
                    if (mId.equals(r.getmId())) {
                        res = false;
                    }
                }
                if (res) {
                    addList.add(mId);
                }
            }
            for (SysRoleMenuFunction rm : sysRoleMenuFunctionList) {
                boolean res = true;
                for (Long m : sysRoleMenuFunctionExt.getmId()) {
                    if (rm.getmId().equals(m)) {
                        res = false;
                    }
                }
                if (res) {
                    delList.add(rm.getmId());
                }
            }
            int addRes = 0;
            int delRes = 0;
            if (delList.size() > 0) {
                addRes = sysRoleMenufunctionMapper.batchDelete(sysRoleMenuFunctionExt.getRoleId(), delList);
                if (addRes <= 0) {
                    throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL);
                }
            }
            if (addList.size() > 0) {
                delRes = sysRoleMenufunctionMapper.batchInsert(sysRoleMenuFunctionExt.getRoleId(), addList);
                if (delRes <= 0) {
                    throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL);
                }
            }
            if (addRes > 0 || delRes > 0) {
                //更新内容shrio里面内容
                redisTemplate.convertAndSend(StringConstant.REDIS_SHRIO_PERMISSION_TPOIC, StringConstant.REDIS_SHRIO_PERMISSION_TPOIC);
            }
        }
        return true;
    }
}
