package com.addplus.server.connector.mysql.durid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

//import com.github.pagehelper.PageHelper;

/**
 * 类名：DruidSourceConfig
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/3 17:27
 * @describe 类描述：配置druid连接池,使用读取配置文件方式
 */
@Configuration
@ConditionalOnProperty(name = "addplus.mysql_connector",havingValue = "true")
@EnableTransactionManagement
public class DruidSourceConfig {

    @Value("${datasource.url}")
    private String url;

    @Value("${datasource.username}")
    private String username;

    @Value("${datasource.password}")
    private String password;

    @Value("${datasource.driverClassName}")
    private String driverClassName;

    @Value("${datasource.druid.initialSize}")
    private Integer initialSize;

    @Value("${datasource.druid.minIdle}")
    private Integer minIdle;

    @Value("${datasource.druid.maxActive}")
    private Integer maxActive;

    @Value("${datasource.druid.maxWait}")
    private Integer maxWait;

    @Value("${datasource.druid.timeBetweenEvictionRunsMillis}")
    private Integer timeBetweenEvictionRunsMillis;

    @Value("${datasource.druid.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;

    @Value("${datasource.druid.removeAbandoned}")
    private Boolean removeAbandoned;

    @Value("${datasource.druid.removeAbandonedTimeout}")
    private Integer removeAbandonedTimeout;

    @Value("${datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${datasource.druid.numTestsPerEvictionRun}")
    private String numTestsPerEvictionRun;

    @Value("${datasource.druid.testWhileIdle}")
    private Boolean testWhileIdle;

    @Value("${datasource.druid.testOnBorrow}")
    private Boolean testOnBorrow;

    @Value("${datasource.druid.testOnReturn}")
    private Boolean testOnReturn;

    @Value("${datasource.druid.poolPreparedStatements}")
    private Boolean poolPreparedStatements;

    @Value("${datasource.druid.maxPoolPreparedStatementPerConnectionSize}")
    private Integer maxPoolPreparedStatementPerConnectionSize;

    @Value("${datasource.druid.filters}")
    private String filters;



    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource()  {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }


//    @Bean
//    @Primary
//    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        try {
//            return bean.getObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Bean
//    @Primary
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }


    @Bean
    @Primary
    //配置事物管理
    public DataSourceTransactionManager masterTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }


}
