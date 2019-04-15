package com.addplus.server.shiro.config.sessioncluster;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.crazycake.shiro.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 类名: RedisSessionDAO
 *
 * @author 特大碗拉面
 * @version V1.0
 * @date 2017-10-10 16:00:39
 * @description 类描述: 对Shrio中的session进行CURD
 */
public class RedisSessionDAO extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
    private RedisManager redisManager;
    private String keyPrefix = "shiro_redis_session:";

    public RedisSessionDAO() {
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    private void saveSession(Session session) throws UnknownSessionException {
        if(session != null && session.getId() != null) {
            session=session;
            this.redisManager.set(this.getStringKey(session.getId()), session, this.redisManager.getExpire());
        } else {
            logger.error("session or session id is null");
        }
    }
    @Override
    public void delete(Session session) {
        if(session != null && session.getId() != null) {
            this.redisManager.del(this.getStringKey(session.getId()));
        } else {
            logger.error("session or session id is null");
        }
    }
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet();
        Set keys = this.redisManager.keys(this.keyPrefix + "*");
        if(keys != null && keys.size() > 0) {
            Iterator i$ = keys.iterator();

            while(i$.hasNext()) {
                String key = (String) i$.next();
                byte[] bytes = JSONObject.parseObject(key, byte[].class);
                Session s = (Session)SerializeUtils.deserialize(bytes);
                sessions.add(s);
            }
        }

        return sessions;
    }
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId == null) {
            logger.error("session id is null");
            return null;
        } else {
            Object sessionFirst = this.redisManager.get(this.getStringKey(sessionId));
            if(sessionFirst==null){
                return null;
            }
            return (Session) sessionFirst;
        }
    }

    private String getStringKey(Serializable sessionId) {
        String preKey = this.keyPrefix + sessionId;
        return preKey;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }


    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public String getKeyPrefix() {
        return this.keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }


}
