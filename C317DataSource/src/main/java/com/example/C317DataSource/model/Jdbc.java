package com.example.C317DataSource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * JDBC
 * jdbc对象
 * @author wnm
 * @date 2021/1/4
 */
@ApiModel(value = "jdbc对象")
public class Jdbc {
    @Id
    @ApiModelProperty(value = "id")
    private String id;//id
    @ApiModelProperty(value = "连接备注信息")
    private String name;//名字
    @ApiModelProperty(value = "用户id")
    private String author;//作者
    @ApiModelProperty(value = "连接信息")
    private Connect connect;//连接信息
    @ApiModelProperty(value = "创建时间")
    private Date created;// 创建时间
    @ApiModelProperty(value = "更新时间")
    private Date updated;// 更新时间

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Connect getConnect() {
        return connect;
    }

    public void setConnect(Connect connect) {
        this.connect = connect;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
