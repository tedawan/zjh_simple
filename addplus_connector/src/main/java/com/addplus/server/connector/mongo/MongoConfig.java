package com.addplus.server.connector.mongo;

import com.mongodb.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "addplus.mongodb_connector",havingValue = "true")
public class MongoConfig {

    @Value("${mongo.socketKeepAlive}")
    private boolean socketKeepAlive;

    @Value("${mongo.threadsAllowed}")
    private int threadsAllowed;

    @Value("${mongo.connectionsPerHost}")
    private int connectionsPerHost;

    @Value("${mongo.maxWaitTime}")
    private int maxWaitTime;

    @Value("${mongo.connectTimeout}")
    private int connectTimeout;

    @Value("${mongo.socketTimeout}")
    private int socketTimeout;

    @Value("${mongo.writeConcern}")
    private int writeConcern;

    @Value("${mongo.host}")
    private String host;

    @Value("${mongo.port}")
    private int port;

    @Value("${mongo.database}")
    private String database;

    @Value("${mongo.username}")
    private String username;

    @Value("${mongo.password}")
    private char[] password;

    @Bean(name="mongoServerAddress")
    public ServerAddress getServerAddress(){
        ServerAddress serverAddress = new ServerAddress(host,port);
        return serverAddress;
    }

    @Bean(name = "mongoCredential")
    public MongoCredential getMongoCredential(){
        return MongoCredential.createCredential(username,database,password);
    }

    @Bean(name = "readPreference")
    public ReadPreference getReadPreference(){
        return ReadPreference.secondaryPreferred();
    }

    @Bean(name = "writeConcern")
    public WriteConcern getWriteConcern(){
        return new WriteConcern(writeConcern);
    }

    @Bean(name = "sanberBuilder")
    public MongoBuilder getSanberBuilder(WriteConcern writeConcern, ReadPreference readPreference){
        return new MongoBuilder(writeConcern,readPreference,maxWaitTime,connectTimeout,socketTimeout,threadsAllowed,connectionsPerHost,socketKeepAlive);
    }

    @Bean(name = "options")
    public MongoClientOptions getMongoClientOptions(MongoBuilder sanberBuilder){
        return sanberBuilder.build();
    }

    @Bean(name = "mongoClient")
    public MongoClient getMongoClient(ServerAddress serverAddress, MongoCredential mongoCredential, MongoClientOptions mongoClientOptions) {
        List<MongoCredential> list = new ArrayList<>();
        list.add(mongoCredential);
        return new MongoClient(serverAddress, list, mongoClientOptions);
    }

    @Bean(name = "morphia")
    public Morphia getMorphia(){
        return new Morphia();
    }

    @Bean(name = "datastore")
    public Datastore getDatastore(Morphia morphia,MongoClient mongoClient){
        return morphia.createDatastore(mongoClient,database);
    }
}
