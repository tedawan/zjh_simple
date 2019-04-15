package com.addplus.server.provider.config;

import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.model.base.Token;
import com.alibaba.fastjson.JSON;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 类名: TokenService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/6/22 上午10:53
 * @description 类描述: Token类信息生成
 */
@Service
public class TokenService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.profiles.active}")
    private String actice;

    /**
     * 方法描述：生成Token码
     *
     * @param
     * @return
     * @throws Exception
     * @author zhangjiehang
     * @date 2018/6/22 上午11:07
     */
    public Token getTokenKey(String memberId) {
        //删除原有的key
        removeTokenKey(memberId);
        Token token = new Token();
        String accessKey = new ObjectId().toString();
        token.setAccessKey(memberId + accessKey);
        token.setSecretKey(new ObjectId().toString());
        token.setMemId(memberId);
        redisTemplate.opsForValue().set(StringConstant.TOKEN_REDIS_PREFIX + memberId, JSON.toJSONString(token), 24, TimeUnit.HOURS);
        return token;
    }

    public void removeTokenKey(String memberId) {
        if (redisTemplate.hasKey(StringConstant.TOKEN_REDIS_PREFIX + memberId)) {
            redisTemplate.delete(StringConstant.TOKEN_REDIS_PREFIX + memberId);
        }
    }

    public Boolean isDevProfiles() {
        if ("dev".equals(actice)) {
            return true;
        }
        return false;
    }

}
