package com.example.C317DataSource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * connect
 * 连接类
 * @author wnm
 * @date 2021/1/4
 */
@ApiModel(value = "连接信息类")
public class Connect {

    @ApiModelProperty(value = "数据库类型")
    private String dbType; //数据库类型 {"mysql", "oracle", "oracleSid", "postgresql", "sqlserver", "sqlserverold", "dm", "sqlite", "h2Embedded", "h2Server", "access"}
    @ApiModelProperty(value = "ip地址")
    private String dbIp; //ip地址
    @ApiModelProperty(value = "端口")
    private String dbPort;//端口
    @ApiModelProperty(value = "数据库名")
    private String dbName;//数据库名
    @ApiModelProperty(value = "用户名")
    private String dbUserName;//用户名
    @ApiModelProperty(value = "密码")
    private String dbUserPassword;//密码
    @ApiModelProperty(value = "连接链接")
    private String dbUrl;//连接链接

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbIp() {
        return dbIp;
    }

    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbUserPassword() {
        return dbUserPassword;
    }

    public void setDbUserPassword(String dbUserPassword) {
        this.dbUserPassword = dbUserPassword;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

}
