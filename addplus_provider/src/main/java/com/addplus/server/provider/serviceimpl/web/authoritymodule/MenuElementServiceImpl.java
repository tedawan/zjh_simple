package com.addplus.server.provider.serviceimpl.web.authoritymodule;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysMenuElementMapper;
import com.addplus.server.api.mapper.authority.SysUserMapper;
import com.addplus.server.api.model.authority.SysMenuElement;
import com.addplus.server.api.model.authority.SysUser;
import com.addplus.server.api.service.web.authoritymodule.MenuElementService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by 特大碗拉面 on 2017/11/9 0009.
 */
@Service(interfaceClass = MenuElementService.class)
public class MenuElementServiceImpl implements MenuElementService {

    @Autowired
    private SysMenuElementMapper sysMenuElementMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<String> getUserFunctionList(Integer userId) throws Exception {
        if(userId!=null){
           SysUser sysUser = sysUserMapper.selectById(userId);
            if(sysUser !=null){
                List<String> list=  Arrays.asList( sysUser.getRoles().split(","));
                if (!list.isEmpty()) {
                    Integer loginType = Integer.parseInt(RpcContext.getContext().getAttachment("loginType"));
                    List<String> menuElements = sysMenuElementMapper.getUserFunctionList(list, loginType);
                    if(menuElements!=null&&!menuElements.isEmpty()){
                        return menuElements;
                    }else{
                        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
                    }
                }
            }
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);

        }else{
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }

    }

    @Override
    public List<SysMenuElement> getMenuElementList() throws Exception {
        List<SysMenuElement> sysMenuElements = sysMenuElementMapper.getMenuElementList();
        if(sysMenuElements !=null&&!sysMenuElements.isEmpty()){
            return sysMenuElements;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
    }

    @Override
    public List<SysMenuElement> getMenuElementByfunctionId(Integer mId) throws Exception {
        if(mId==null||mId<=0){
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysMenuElement sysMenuElement = new SysMenuElement();
        sysMenuElement.setmId(mId);
        sysMenuElement.setIsDeleted(0);
        List<SysMenuElement> elementList= sysMenuElementMapper.selectList(new QueryWrapper<SysMenuElement>(sysMenuElement));
        if(elementList!=null&&!elementList.isEmpty()){
            return elementList;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
    }

    @Override
    public SysMenuElement getMenuElementById(Long eId) throws Exception {
        if(eId==null||eId<=0){
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysMenuElement sysMenuElement = new SysMenuElement();
        sysMenuElement.setIsDeleted(0);
        sysMenuElement.setId(eId);
        SysMenuElement sysMenuElementNew = sysMenuElementMapper.selectById(sysMenuElement);
        if(sysMenuElementNew !=null){
            return sysMenuElement;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);

    }

    @Override
    public Boolean addMenuElement(SysMenuElement sysMenuElement) throws Exception {
        if(sysMenuElement ==null || sysMenuElement.getmId()==null){
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Date date = new Date();
        sysMenuElement.setIsDeleted(0);
        sysMenuElement.setGmtCreate(date);
        sysMenuElement.setGmtModified(date);
        String modifyUser =  RpcContext.getContext().getAttachment("id");
        sysMenuElement.setModifyUser(modifyUser);
        int count = sysMenuElementMapper.insert(sysMenuElement);
        if(count>0) {
            return true;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL,false);
    }

    @Override
    public Boolean deleteMenuElementById(Long eId) throws Exception{
        if(eId==null||eId<=0){
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysMenuElement sysMenuElement = new SysMenuElement();
        sysMenuElement.setId(eId);
        sysMenuElement.setIsDeleted(1);
        String modifyUser =  RpcContext.getContext().getAttachment("id");
        sysMenuElement.setModifyUser(modifyUser);
        int count = sysMenuElementMapper.updateById(sysMenuElement);
        if(count>0){
            return true;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL,false);
    }

    @Override
    public Boolean updateMenuElementById(SysMenuElement sysMenuElement)throws Exception {
        if(sysMenuElement ==null){
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        sysMenuElement.setGmtModified(new Date());
        String modifyUser =  RpcContext.getContext().getAttachment("id");
        sysMenuElement.setModifyUser(modifyUser);
        int count = sysMenuElementMapper.updateById(sysMenuElement);
        if(count>0){
            return true;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL,false);
    }
}
