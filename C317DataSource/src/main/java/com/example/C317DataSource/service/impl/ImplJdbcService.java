package com.example.C317DataSource.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import com.example.C317DataSource.common.ServerResponse;
import com.example.C317DataSource.dao.JdbcDao;
import com.example.C317DataSource.model.Connect;
import com.example.C317DataSource.model.Jdbc;
import com.example.C317DataSource.service.IJdbcService;
import com.example.C317DataSource.utils.JDBCUtil;
import com.example.C317DataSource.utils.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ImplJDBCService
 *
 * @author wnm
 * @date 2021/1/3
 */
@Service
public class ImplJdbcService implements IJdbcService {
    @Autowired
    private JdbcDao jdbcDao;

    @Override
    public ServerResponse<String[]> getType(){
        // {"mysql", "oracle", "oracleSid", "postgresql", "sqlserver", "sqlserverold", "dm", "sqlite", "h2Embedded", "h2Server", "access"};
        String[] dbType = new String[]{"mysql", "oracle", "postgresql", "sqlserver", "sqlserverold"};
        return ServerResponse.createBySuccess("获取当前可连数据库类型成功！",dbType);
    }

    @Override
    public ServerResponse<String> getPort(String type){
        String port=JDBCUtil.getDbDefaultPort(type);
        return ServerResponse.createBySuccess(type+"默认端口获取成功！",port);
    }

    @Override
    public ServerResponse<Map<String,Object>> creatJDBC(Connect connect){
        Map<String,Object> map=new HashMap<>();
        Jdbc jdbc=new Jdbc();
        jdbc.setName(connect.getDbType()+"数据库连接");
        jdbc.setAuthor("");
        jdbc.setCreated(new Date());
        jdbc.setUpdated(new Date());
        DruidDataSource dataSource = new DruidDataSource();
        try {
            String url= JDBCUtil.getJdbcUrl(connect.getDbUrl(), connect.getDbType(), connect.getDbIp(), connect.getDbPort(), connect.getDbName());
            connect.setDbUrl(url);
            DriverManager.getConnection(url,connect.getDbUserName(), connect.getDbUserPassword());
            dataSource.setUrl(url);
            if ("access".equals(connect.getDbType())) {
                dataSource.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
            }
            dataSource.setUsername(connect.getDbUserName());
            dataSource.setPassword(connect.getDbUserPassword());
            dataSource.setTestWhileIdle(false);
            dataSource.setFailFast(true);
            List<String> getTableNames = SqlUtil.getTableNames(dataSource,connect.getDbType(),connect.getDbUserName());
            map.put("table_names",getTableNames);
            jdbc.setConnect(connect);
            String id=jdbcDao.insert(jdbc);
            map.put("_id",id);
            return ServerResponse.createBySuccess("数据库连接成功！",map);
        }
        catch (SQLException e){
            return ServerResponse.createByErrorMessage(e.getMessage());
        } finally {
            JdbcUtils.close(dataSource);
        }
    }

    @Override
    public ServerResponse<Map<String,Object>> getTableData(String id,String tableName){
        Map<String,Object> map=new HashMap<>();
        map.put("_id",id);
        Jdbc jdbc=jdbcDao.selectById(id);
        Connect connect=jdbc.getConnect();
        DataSource dataSource = null;
        try {
            dataSource = SqlUtil.getDataSource(connect.getDbType(), connect.getDbIp(), connect.getDbPort(), connect.getDbName(), connect.getDbUserName(), connect.getDbUserPassword(),null,"Druid");
            List<String> getTableFields=SqlUtil.getTableFields(dataSource,tableName);
            map.put("columns",getTableFields);
            List<Map<String, Object>> getTableData=SqlUtil.getTableData(dataSource,tableName);
            map.put("rows",getTableData);
            return ServerResponse.createBySuccess(tableName+"表数据获取成功！",map);
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage(e.getMessage());
        } finally {
            if (dataSource instanceof DruidDataSource) {
                JdbcUtils.close((Closeable) dataSource);
            }
        }
    }

    @Override
    public ServerResponse<Map<String,Object>> update(Jdbc jdbc){
        Map<String,Object> map=new HashMap<>();
        map.put("_id",jdbc.getId());
        jdbc.setUpdated(new Date());
        jdbcDao.update(jdbc);
        return ServerResponse.createBySuccess("连接信息修改成功！",map);
    }

    @Override
    public ServerResponse<String> deleteById(String id){
        jdbcDao.deleteById(id);
        return ServerResponse.createBySuccessMessage("删除成功！");
    }

}
