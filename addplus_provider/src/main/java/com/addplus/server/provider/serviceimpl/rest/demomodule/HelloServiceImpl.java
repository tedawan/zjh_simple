package com.addplus.server.provider.serviceimpl.rest.demomodule;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.demo.SysDemoUserMapper;
import com.addplus.server.api.model.demo.SysDemoUser;
import com.addplus.server.api.service.queue.AsyncService;
import com.addplus.server.api.service.rest.demomodule.HelloService;
import com.addplus.server.api.utils.DataUtils;
import com.addplus.server.provider.annotation.NotInclude;
import com.addplus.server.provider.annotation.NotToken;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {



    @Autowired
    private SysDemoUserMapper sysDemoUserMapper;

    @Reference(check = false, async = true)
    private AsyncService asyncService;

    @Override
    @NotToken
    public String sayHello(String name) {
        SysDemoUser sysDemoUser = new SysDemoUser();
        sysDemoUser.setId(1);
        sysDemoUser.setVersion(1);
        sysDemoUser.setNickName("test111");
        sysDemoUser.setCreateDate(new Date());
        sysDemoUserMapper.updateById(sysDemoUser);
        sysDemoUserMapper.deleteById(1);
        return "Hello" + name;
    }

    @Override
    public SysDemoUser addUser(SysDemoUser user) {
        user.setDeleted(0);
        user.setVersion(1);
        user.setCreateDate(new Date());
        sysDemoUserMapper.insert(user);
        return user;
    }


    @Override
    @NotInclude
    public String demoSendMessageByPhone(String code, String phone) throws Exception {
        asyncService.sendAliYunMessage(code, phone);
        return "SUCCESS";
    }

    @Override
    public String demoDelayMessageRabbit(String context) throws Exception {
        asyncService.delayMessage(context);
        return "SUCCESS";
    }

    @Override
    public SysDemoUser getDemoOneProperty(Integer demoUserId) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(demoUserId)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysDemoUser sysDemoUser = sysDemoUserMapper.findUserWithDemoAddress(demoUserId);
        if (sysDemoUser == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
        return sysDemoUser;
    }

    @Override
    public SysDemoUser getDenoManyProperty(Integer demoUserId) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(demoUserId)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysDemoUser sysDemoUser = sysDemoUserMapper.getDemoUserWithDemoCar(demoUserId);
        if (sysDemoUser == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
        return sysDemoUser;
    }

    @Override
    public SysDemoUser getDenoOneManyProperty(Integer demoUserId) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(demoUserId)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysDemoUser sysDemoUser = sysDemoUserMapper.getDemoUserWithDemoCarAndDemoAddress(demoUserId);
        if (sysDemoUser == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
        return sysDemoUser;
    }

    @Override
    public List<SysDemoUser> getAllDemoUser() throws Exception {
        return sysDemoUserMapper.testXmlDemoUser();
    }
}
