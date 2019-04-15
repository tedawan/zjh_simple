package com.addplus.server.shiro.config.shiro;

import com.addplus.server.api.mapper.authority.SysMenuCommonMapper;
import com.addplus.server.api.mapper.authority.SysMenuElementMapper;
import com.addplus.server.api.model.authority.SysMenuCommon;
import com.addplus.server.api.model.authority.ext.SysMenuRoleExt;
import com.addplus.server.api.utils.DataUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>SysUser: Zhang Kaitao
 * <p>Date: 14-2-25
 * <p>Version: 1.0
 */
@Service
public class ShiroFilerChainManager {


    private Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Autowired
    private DefaultFilterChainManager filterChainManager;

    @Autowired
    private DefaultFilterChainManager otherfilterChainManager;

    @Autowired
    private SysMenuElementMapper sysMenuElementMapper;

    @Autowired
    private SysMenuCommonMapper sysMenuCommonMapper;

    @Value("${spring.profiles.active}")
    private String active;


    public synchronized void initFilterChains() {
        //1、首先删除以前老的filter chain并注册默认的
        otherfilterChainManager.getFilterChains().clear();
        otherfilterChainManager.addToChain("/get/rest/**", "anon");
        otherfilterChainManager.addToChain("/post/rest/**", "anon");
        //先加入公共接口
        List<SysMenuCommon> sysMenuCommons = sysMenuCommonMapper.getMenuCommonByModule("web");
        if (sysMenuCommons != null && !sysMenuCommons.isEmpty()) {
            for (SysMenuCommon sysMenuCommon : sysMenuCommons) {
                if (!DataUtils.isEmpty(sysMenuCommon.getFilter(), sysMenuCommon.getUrl())) {
                    otherfilterChainManager.addToChain(sysMenuCommon.getUrl(), sysMenuCommon.getFilter());
                }
            }
        }
        //加入接口权限接口
        int countAll = sysMenuElementMapper.getMunuElementGroupMethodRoleByCount();
        if (countAll > 0) {
            int loop = DataUtils.getSize(countAll, 50);
            if (loop > 0) {
                for (int i = 1; i < loop + 1; i++) {
                    Page<SysMenuRoleExt> page = new Page<SysMenuRoleExt>(i, 50);
                    List<SysMenuRoleExt> sysMenuRoleExtList = sysMenuElementMapper.getMunuElementGroupMethodRole(page);
                    if (sysMenuRoleExtList != null && !sysMenuRoleExtList.isEmpty()) {
                        for (SysMenuRoleExt sysMenuRoleExt : sysMenuRoleExtList) {
                            if (StringUtils.isNotBlank(sysMenuRoleExt.getUrl())) {
                                otherfilterChainManager.addToChain(sysMenuRoleExt.getUrl(), "roles", sysMenuRoleExt.getrId());
                            }
                        }
                    }
                }
            }
            otherfilterChainManager.addToChain("/**", "basicFilter");
        } else {
            //当所有菜单查询没有，设置只要登录就能访问。当分配到用户时候就取消
            logger.error("当前菜单库查询不到有菜单");
            otherfilterChainManager.addToChain("/**", "authc");
        }
        filterChainManager.getFilterChains().clear();
        filterChainManager.getFilterChains().putAll(otherfilterChainManager.getFilterChains());
    }

}
