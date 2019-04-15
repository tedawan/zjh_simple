package com.addplus.server.provider.serviceimpl.web.authoritymodule;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysUserMapper;
import com.addplus.server.api.model.authority.SysUser;
import com.addplus.server.api.service.web.authoritymodule.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private DefaultHashService defaultHashService;

    @Override
    public SysUser selectByUsername(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        SysUser sysUser = new SysUser();
        sysUser.setAccount(account);
        sysUser.setIsDeleted(0);
        SysUser sysUserOne = sysUserMapper.selectOne(new QueryWrapper<SysUser>(sysUser));
        return sysUserOne;
    }

    @Override
    public Boolean updateLoginUser(SysUser sysUser) {
        SysUser sysUserUpdate = new SysUser();
        sysUserUpdate.setId(sysUser.getId());
        sysUserUpdate.setLoginAddress(sysUser.getLoginAddress());
        sysUserUpdate.setLoginCount(sysUser.getLoginCount() + 1);
        sysUserUpdate.setLoginTime(new Date());
        sysUserUpdate.setGmtModified(new Date());
        String modifyUser =  RpcContext.getContext().getAttachment("id");
        sysUserUpdate.setModifyUser(modifyUser);
        int count = sysUserMapper.updateById(sysUserUpdate);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean addUser(SysUser sysUser) throws ErrorException {
        if (sysUser == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        } else {
            if (StringUtils.isBlank(sysUser.getRoles()) || StringUtils.isBlank(sysUser.getAccount()) || StringUtils.isBlank(sysUser.getPassword()) || StringUtils.isBlank(sysUser.getNickname()) || StringUtils.isBlank(sysUser.getPhone())) {
                throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
            }
        }
        sysUser.setGmtCreate(new Date());
        sysUser.setLoginCount(0);
        sysUser.setIsDeleted(0);
        sysUser.setStatus(0);
        String modifyUser =  RpcContext.getContext().getAttachment("id");
        sysUser.setModifyUser(modifyUser);
        //生成加密后的密码
        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("md5").setSource(sysUser.getPassword())
                .setSalt(ByteSource.Util.bytes(sysUser.getAccount())).setIterations(2).build();
        String hexPassword = defaultHashService.computeHash(request).toHex();
        sysUser.setPassword(hexPassword);
        int count = sysUserMapper.insert(sysUser);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteUserById(Long id) throws Exception {
        if (id == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        } else if (id <= 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        String modifyUser =  RpcContext.getContext().getAttachment("id");
        sysUser.setModifyUser(modifyUser);
        sysUserMapper.updateById(sysUser);
        int count = sysUserMapper.deleteById(id);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SysUser selectUserById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setIsDeleted(0);
        SysUser sysUserNew = sysUserMapper.selectOne(new QueryWrapper<SysUser>(sysUser));
        if (sysUserNew == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        } else {
            if (sysUserNew.getStatus() == 1) {
                //抛出冻结异常码
                throw new ErrorException(ErrorCode.SYS_LOGIN_MEMBER_DISABLE);
            }
        }
        return sysUserNew;
    }

    @Override
    public SysUser modifyUserGetInfoById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setIsDeleted(0);
        SysUser sysUserNew = sysUserMapper.selectOne(new QueryWrapper<SysUser>(sysUser));
        if (sysUserNew == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
        return sysUserNew;
    }

    @Override
    public String encryptString(SysUser sysUser) throws Exception {
        if (sysUser == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        //生成加密后的密码
        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("md5").setSource(sysUser.getPassword())
                .setSalt(ByteSource.Util.bytes(sysUser.getAccount())).build();
        String hexPassword = defaultHashService.computeHash(request).toHex();
        return hexPassword;
    }

    @Override
    public Boolean updateUser(SysUser sysUser) throws Exception {
        if (sysUser == null || sysUser.getId() == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        sysUser.setGmtModified(new Date());
        String modifyUser =  RpcContext.getContext().getAttachment("id");
        sysUser.setModifyUser(modifyUser);
        int count = sysUserMapper.updateById(sysUser);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Page<SysUser> getAllUsers(Integer pageNo, Integer pageSize) throws Exception {
        if (pageNo == null || pageSize == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Page<SysUser> page = new Page<SysUser>(pageNo,pageSize);
        SysUser sysUser = new SysUser();
        sysUser.setIsDeleted(0);
        try {
            page = (Page<SysUser>) sysUserMapper.selectPage(page,new QueryWrapper<SysUser>(sysUser));
            if (page != null && page.getTotal()!=0) {
                return page;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
    }

    @Override
    public SysUser getByUser() throws Exception {
        Long memId= Long.valueOf(RpcContext.getContext().getAttachment("id"));
        SysUser sysUser = new SysUser();
        sysUser.setIsDeleted(0);
        sysUser.setId(memId);
        return sysUserMapper.selectOne(new QueryWrapper<SysUser>(sysUser));

    }

    @Override
    public Integer getUserNameCount(String account) throws Exception {
        if (StringUtils.isBlank(account)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysUser sysUser = new SysUser();
        sysUser.setAccount(account);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>(sysUser);
        int res = sysUserMapper.selectCount(queryWrapper);
        return res;
    }

}
