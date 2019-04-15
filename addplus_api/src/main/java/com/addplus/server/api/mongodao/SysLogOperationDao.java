package com.addplus.server.api.mongodao;

import com.addplus.server.api.model.authority.SysLogOperation;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "addplus.mongodb_connector", havingValue = "true")
public class SysLogOperationDao extends BasicDao<SysLogOperation, ObjectId> {
    @Autowired
    public SysLogOperationDao(Datastore ds) {
        super(ds);
    }
}
