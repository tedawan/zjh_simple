package com.addplus.server.connector.mongo;

import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

/**
 * 类名: MongoBuilder
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/3/3 下午3:34
 * @description 类描述:mongodb详细配置类
 */
public class MongoBuilder extends MongoClientOptions.Builder
{
    
    public MongoBuilder(WriteConcern writeConcern, ReadPreference readPreference, int maxWaitTime, int connectTimeout, int socketTimeout, int threadsAllowed, int connectionsPerHost,
                        boolean socketKeepAlive)
    {
        // 写入操作不确认，仅可提示网络异常，比ACKNOWLEDGED规则性能高一倍
        super.writeConcern(WriteConcern.ACKNOWLEDGED);
        // 读写分离，读操作仅通过从属节点
        // super.readPreference(ReadPreference.secondary());
        //super.writeConcern(writeConcern);
        super.readPreference(readPreference);
        super.maxWaitTime(maxWaitTime);
        super.connectTimeout(connectTimeout);
        super.socketTimeout(socketTimeout);
        super.socketKeepAlive(socketKeepAlive);
        super.connectionsPerHost(connectionsPerHost);
        super.threadsAllowedToBlockForConnectionMultiplier(threadsAllowed);
    }
}
