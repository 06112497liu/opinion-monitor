/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tjwang
 * @version $Id: JDBCProperties.java, v 0.1 2017/7/5 0005 14:07 tjwang Exp $
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class JDBCProperties {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
