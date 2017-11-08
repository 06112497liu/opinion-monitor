/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.bbd.config.properties.JDBCProperties;
import com.mybatis.pagination.OffsetLimitInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Mybatis配置
 *
 * @author tjwang
 * @version $Id: MyBatisConfig.java, v 0.1 2017/7/5 0005 10:32 tjwang Exp $
 */
@Configuration
@EnableTransactionManagement
public class MyBatisConfig implements TransactionManagementConfigurer {

    public static final Logger logger = LoggerFactory.getLogger(MyBatisConfig.class);
    @Value("${mybatis.mapperLocations}")
    private String             mapperLocations;
    @Autowired
    private JDBCProperties     jdbcProperties;

    @Bean
    public static OffsetLimitInterceptor offsetLimitInterceptor() {
        OffsetLimitInterceptor offsetLimitInterceptor = new OffsetLimitInterceptor();
        offsetLimitInterceptor.setDialectClass("com.mybatis.dialect.MySQLDialect");
        return offsetLimitInterceptor;
    }

    @Bean("dataSource")
    public DataSource dataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", jdbcProperties.getDriverClassName());
        props.put("url", jdbcProperties.getUrl());
        props.put("username", jdbcProperties.getUsername());
        props.put("password", jdbcProperties.getPassword());
        return DruidDataSourceFactory.createDataSource(props);
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource ds) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(ds); // 指定数据源
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));

        fb.setPlugins(new Interceptor[] { offsetLimitInterceptor() });

        return fb.getObject();
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        try {
            return new DataSourceTransactionManager(dataSource());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        throw new RuntimeException("annotationDrivenTransactionManager 异常");
    }

}
