package com.example.C317DataSource.service;

import com.example.C317DataSource.common.ServerResponse;
import com.example.C317DataSource.model.Connect;
import com.example.C317DataSource.model.Jdbc;

import java.util.Map;

/**
 * IJDBCService
 *
 * @author wnm
 * @date 2021/1/3
 */
public interface IJdbcService {

    //获取所有连接类型
    ServerResponse<String[]> getType();

    // 获取指定指定数据库的默认端口
    ServerResponse<String> getPort(String type);

    // 创建连接
    ServerResponse<Map<String,Object>> creatJDBC(Connect connect);

    // 获取指定表数据
    ServerResponse<Map<String,Object>> getTableData(String id,String tableName);

    // 修改连接信息
    ServerResponse<Map<String,Object>> update(Jdbc jdbc);

    // 获取指定表数据
    ServerResponse<String> deleteById(String id);

}
