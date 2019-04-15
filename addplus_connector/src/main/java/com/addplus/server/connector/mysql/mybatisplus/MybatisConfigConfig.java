package com.addplus.server.connector.mysql.mybatisplus;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "addplus.mysql_connector",havingValue = "true")
@MapperScan(basePackages = "com.addplus.server.api.mapper")
public class MybatisConfigConfig {

    @Value("${spring.profiles.active}")
    private String env;

    @Value("${mybatis.type-aliases-package}")
    public String typeAliasesPackage;

    @Value("${mybatis.mapper-locations}")
    public String mapperLocations;

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, GlobalConfig globalConfiguration, Interceptor[] interceptors) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        MybatisPlusProperties mybatisPlusProperties = new MybatisPlusProperties();
        mybatisPlusProperties.setMapperLocations(new String[]{mapperLocations});
        sqlSessionFactory.setMapperLocations(mybatisPlusProperties.resolveMapperLocations());
        sqlSessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        sqlSessionFactory.setDataSource(dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setCacheEnabled(false);
        configuration.setMapUnderscoreToCamelCase(true);
        //sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setGlobalConfig(globalConfiguration);
        sqlSessionFactory.setPlugins(interceptors);
        return sqlSessionFactory.getObject();
    }



    @Bean
    public GlobalConfig globalConfiguration() {
        GlobalConfig conf = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig=new GlobalConfig.DbConfig();
        dbConfig.setIdType(IdType.AUTO);
        dbConfig.setFieldStrategy(FieldStrategy.NOT_EMPTY);
        dbConfig.setDbType(DbType.MYSQL);
        dbConfig.setLogicDeleteValue("1");
        dbConfig.setLogicNotDeleteValue("0");
        conf.setDbConfig(dbConfig);
        conf.setSqlInjector(new LogicSqlInjector());
        return conf;
    }


    @Bean
    public Interceptor[] interceptors(){
        List<Interceptor> interceptorList = new ArrayList<>();
        interceptorList.add(new PaginationInterceptor());
        interceptorList.add(new OptimisticLockerInterceptor());
        if ("dev".equals(env)){
            interceptorList.add(new PerformanceInterceptor());
        }
        return interceptorList.toArray(new Interceptor[interceptorList.size()]);
    }
}
