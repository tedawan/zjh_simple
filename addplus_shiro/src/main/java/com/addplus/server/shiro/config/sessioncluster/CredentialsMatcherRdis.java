package com.addplus.server.shiro.config.sessioncluster;

import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.shiro.config.shiro.CustomizedToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * 类名: CredentialsMatcherRdis
 *
 * @author 特大碗拉面
 * @version V1.0
 * @date 2017-10-12 13:47:07
 * @description 类描述: 重写密码校验工具，提供登录多次锁定账号
 */
public class CredentialsMatcherRdis extends HashedCredentialsMatcher {

    @Value("${shrio.login.maxRetry}")
    private int maxRetry;

    @Value("${shrio.login.lockTime}")
    private int lockTime=3600;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;



    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        CustomizedToken customizedToken = (CustomizedToken) token;
        String rediFiefx = MessageFormat.format(StringConstant.REDIS_PREFIX, customizedToken.getLoginType().getType(), customizedToken.getUsername().toUpperCase().hashCode());
        boolean isExit = redisTemplate.hasKey(rediFiefx);
        long retryCount=0;
        if(isExit==false){
            retryCount = redisTemplate.opsForValue().increment(rediFiefx, 1);
        }else {
            String count = redisTemplate.opsForValue().get(rediFiefx);
            retryCount = Long.valueOf(count);
        }
        if(retryCount > maxRetry) {
            long expret = redisTemplate.getExpire(rediFiefx);
            if(expret==-1){
                redisTemplate.expire(rediFiefx, lockTime, TimeUnit.SECONDS);
            }
            throw new LockedAccountException();
        }
        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            redisTemplate.delete(rediFiefx);
        }else{
            if(isExit){
                redisTemplate.opsForValue().increment(rediFiefx, 1);
            }
        }
        return matches;

    }
}
