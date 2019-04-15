package com.addplus.server.provider.filter;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.model.base.Token;
import com.addplus.server.api.utils.security.AESUtils;
import com.addplus.server.provider.config.TokenService;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.protocol.dubbo.DecodeableRpcInvocation;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

@Activate(group = Constants.PROVIDER)
@Service
public class TokenFilter implements Filter {


    private Map<String, Boolean> tokenAnnontationMap;

    private RedisTemplate redisTemplate;

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private TokenService tokenService;

    public TokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Map<String, Boolean> getTokenAnnontationMap() {
        return tokenAnnontationMap;
    }

    public void setTokenAnnontationMap(Map<String, Boolean> tokenAnnontationMap) {
        this.tokenAnnontationMap = tokenAnnontationMap;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String model = RpcContext.getContext().getAttachment(StringConstant.MODEL);
        if (!"rest".equals(model)) {
            return invoker.invoke(invocation);
        }
        RpcInvocation rpcInvocation = (RpcInvocation) invocation;
        String needToken = rpcInvocation.getAttachment(StringConstant.NEED_TOKEN);
        //不需要token
        if (StringConstant.NOCONFIRM.equals(needToken)) {
            return invoker.invoke(invocation);
        } else {
            String mapKey = rpcInvocation.getAttachment("interface") + "_" + rpcInvocation.getMethodName();
            Boolean tokenAnnotaion = tokenAnnontationMap.get(mapKey);
            if (tokenAnnotaion == null || !tokenAnnotaion) {
                //获取token
                String token = rpcInvocation.getAttachment(StringConstant.REQ_TOKEN_KEY);
                if (StringConstant.NOT_TOKEN.equals(token)) {
                    return new RpcResult(new ErrorException(ErrorCode.SYS_ERROR_NOT_TOKEN));
                } else {
                    if (tokenService.isDevProfiles()) {
                        if ("lanyue".equals(token)) {
                            return invoker.invoke(invocation);
                        }
                    }
                    //获取token的accessKey获取Redis中serectKey
                    if (token.length() < 49) {
                        return new RpcResult(new ErrorException(ErrorCode.SYS_ERROR_TOKEN_ERROR));
                    }
                    String memberIdAccessKey = token.substring(0, token.length() - 24);
                    String memberId = memberIdAccessKey.substring(0, memberIdAccessKey.length() - 24);
                    Object secretKeyObj = redisTemplate.opsForValue().get(StringConstant.TOKEN_REDIS_PREFIX + memberId.toLowerCase());
                    if (secretKeyObj != null) {
                        Token tokenOrign = JSON.parseObject(secretKeyObj.toString(), Token.class);
                        //解密字符是时间戳
                        String signDecrypt = AESUtils.decryptAES(token.substring(token.length() - 24, token.length()), tokenOrign.getSecretKey().substring(8, 24), 0);
                        if (StringUtils.isBlank(signDecrypt)) {
                            return new RpcResult(new ErrorException(ErrorCode.SYS_ERROR_TOKEN_ERROR));
                        } else {
                            //校验当前请求是否还在有效期
                            long requestTimeStamp = Long.valueOf(signDecrypt);
                            long nowTimeStamp = System.currentTimeMillis();
                            long timeDifference = Math.abs(nowTimeStamp - requestTimeStamp);
                            //获取后面校验部分,当前设定5s为有效请求时间
                            if (100000 > timeDifference) {
                                return invoker.invoke(invocation);
                            } else {
                                return new RpcResult(new ErrorException(ErrorCode.SYS_ERROR_REQUEST_TIMEOUT));
                            }
                        }
                    } else {
                        //不存在redis，说明需要重新登录获取token授权
                        return new RpcResult(new ErrorException(ErrorCode.SYS_ERROR_TOKEN_EXPIRE));
                    }
                }
            } else {
                return invoker.invoke(invocation);
            }
        }
    }
}
