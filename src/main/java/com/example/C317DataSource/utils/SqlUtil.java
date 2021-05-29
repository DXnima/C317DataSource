package com.example.C317DataSource.utils;

import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
import cn.hutool.db.meta.TableType;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.*;
import java.util.*;

public class SqlUtil {

    /**
     * 连接数据库
     * @author wnm
     * @date 2020/12/30
     * @param dbType, dbIp, dbPort, dbName, dbUserName, dbUserPassword
     * @return void
     */
    public static void connectAction(String dbType, String dbIp, String dbPort, String dbName, String dbUserName, String dbUserPassword) throws Exception {
        DataSource dataSource = null;
        try {
            dataSource = getDataSource(dbType, dbIp, dbPort, dbName, dbUserName, dbUserPassword,null,"Druid");
            List<String> getTableNames=getTableNames(dataSource,dbType,dbUserName);
            System.out.print("\n表名：");
            System.out.print(getTableNames);
            System.out.print("\n");
            List<String> getTableFields=getTableFields(dataSource,getTableNames.get(0));
            System.out.print("\n表字段：");
            System.out.print(getTableFields);
            System.out.print("\n");
            List<Map<String, Object>> getTableData=getTableData(dataSource,getTableNames.get(0));
            System.out.print("\n表数据：");
            System.out.print(getTableData);
            System.out.print("\n");
        } finally {
            if (dataSource instanceof DruidDataSource) {
                JdbcUtils.close((Closeable) dataSource);
            }
        }
    }

    /**
     * 获取数据源
     * @author wnm
     * @date 2020/12/29
     * @param dbType, dbIp, dbPort, dbName, dbUserName, dbUserPassword, jdbcUrl, dataSourceType]
     * @return javax.sql.DataSource
     */
    public static DataSource getDataSource(String dbType, String dbIp, String dbPort, String dbName, String dbUserName, String dbUserPassword, String jdbcUrl, String dataSourceType) {
        DataSource dataSource = null;
        if ("Druid".equals(dataSourceType)) {
            dataSource = getDruidDataSource(dbType, dbIp, dbPort, dbName, dbUserName, dbUserPassword, jdbcUrl);
        } else if ("Driver".equals(dataSourceType)) {
            dataSource = new DriverManagerDataSource(JDBCUtil.getJdbcUrl(jdbcUrl, dbType, dbIp, dbPort, dbName), dbUserName, dbUserPassword);
        } else if ("Simple".equals(dataSourceType)) {
            dataSource = getSimpleDataSource(dbType, dbIp, dbPort, dbName, dbUserName, dbUserPassword, jdbcUrl);
        }
        return dataSource;
    }

    /**
     *获取DruidDataSource
     * @author wnm
     * @date 2020/12/29
     * @param dbType, dbIp, dbPort, dbName, dbUserName, dbUserPassword, jdbcUrl]
     * @return com.alibaba.druid.pool.DruidDataSource
     */
    public static DruidDataSource getDruidDataSource(String dbType, String dbIp, String dbPort, String dbName, String dbUserName, String dbUserPassword, String jdbcUrl) {
        DruidDataSource dataSource = new DruidDataSource();
        try {
            String url= JDBCUtil.getJdbcUrl(jdbcUrl, dbType, dbIp, dbPort, dbName);
            DriverManager.getConnection(url,dbUserName,dbUserPassword);
            dataSource.setUrl(url);
            if ("access".equals(dbType)) {
                dataSource.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
            }
            dataSource.setUsername(dbUserName);
            dataSource.setPassword(dbUserPassword);
            dataSource.setTestWhileIdle(false);
            dataSource.setFailFast(true);
            return dataSource;
        }
        catch (SQLException e){
            String m=e.getMessage();
            System.out.print("\n错误："+m);
            return dataSource;
        }

    }

    /**
     *获取SimpleDataSource
     * @author wnm
     * @date 2020/12/29
     * @param dbType, dbIp, dbPort, dbName, dbUserName, dbUserPassword, jdbcUrl]
     * @return org.springframework.jdbc.datasource.SimpleDriverDataSource
     */
    public static SimpleDriverDataSource getSimpleDataSource(String dbType, String dbIp, String dbPort, String dbName, String dbUserName, String dbUserPassword, String jdbcUrl) {
        jdbcUrl = JDBCUtil.getJdbcUrl(jdbcUrl, dbType, dbIp, dbPort, dbName);
        String driverClass = null;
        Driver driver=null;
        try {
            if ("access".equals(dbType)) {
                driverClass = "net.ucanaccess.jdbc.UcanaccessDriver";
            } else {
                driverClass = JdbcUtils.getDriverClassName(jdbcUrl);
            }
            driver=JdbcUtils.createDriver(driverClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource(driver,jdbcUrl, dbUserName, dbUserPassword);
        return dataSource;
    }

    public static List<String> showSqlServerTables(DataSource dataSource, String dbType) throws SQLException {
        List<String> tables = new ArrayList<>();
        String sql = "";
        if ("sqlserver".equals(dbType)) {
            sql = "select c.name from sys.objects c where c.type='u'";
        } else if ("sqlserverold".equals(dbType)) {
            sql = "select c.name from sysobjects c where c.type='u'";
        } else if ("access".equals(dbType)) {
            sql = "select table_name from information_schema.tables";
        } else {
            return tables;
        }
        tables = new JdbcTemplate(dataSource).queryForList(sql, String.class);
        return tables;
    }

    /**
     * 获取数据库的表名
     * @author wnm
     * @date 2020/12/30
     * @param dbType, dbIp, dbPort, dbName, dbUserName, dbUserPassword
     * @return void
     */
    public static List<String> getTableNames(DataSource dataSource,String dbType, String dbUserName) throws SQLException{
        List<String> tableNames;
        try {
                tableNames = MetaUtil.getTables(dataSource, JDBCUtil.convertDatabaseCharsetType(dbUserName, "", dbType), TableType.valueOf("TABLE"));
                return tableNames;
            } catch (Throwable e) {
                if ("sqlserver".equals(dbType) || "sqlserverold".equals(dbType) || "access".equals(dbType)) {
                    tableNames = SqlUtil.showSqlServerTables(dataSource, dbType);
                } else {
                    if ("oracleSid".equals(dbType)) {
                        dbType = "oracle";
                    }
                    Connection connection = dataSource.getConnection();
                    try {
                        tableNames = JdbcUtils.showTables(connection, dbType);
                    } finally {
                        JdbcUtils.close(connection);
                    }
                }
                return tableNames;
            }
    }

    /**
     * 获取表字段信息
     * @author wnm
     * @date 2020/12/30
     * @param dataSource, tableName
     * @return cn.hutool.db.meta.Table
     */
    public static List<String> getTableFields(DataSource dataSource,String tableName) {
        Table table = new Table(tableName);
        List<String> fields=new ArrayList<>();
        try {
            table = MetaUtil.getTableMeta(dataSource, tableName);
            for (Column column : table.getColumns()) {
                fields.add(column.getName());
            }
            return fields;
        } catch (Throwable e) {
            // 获取表结构失败！使用jdbc原生方法获取
            String querySql = String.format("select * from %s where 1=2", tableName);
            ResultSet rs = JDBCUtil.getResultSetBySql(dataSource, querySql);
            try {
                assert rs != null;
                ResultSetMetaData rsMetaData = rs.getMetaData();
                for (int i = 1, len = rsMetaData.getColumnCount(); i <= len; i++) {
                    Column column = new Column();
                    column.setName(rsMetaData.getColumnName(i));
                    column.setType(rsMetaData.getColumnType(i));
                    column.setSize(rsMetaData.getColumnDisplaySize(i));
                    column.setComment(rsMetaData.getColumnLabel(i));
                    table.setColumn(column);
                }
                for (Column column : table.getColumns()) {
                    fields.add(column.getName());
                }
                return fields;
            } catch (Throwable ee) {
                return null;
            }
        }
    }

    /**
     * 获取指定表格数据
     * @author wnm
     * @date 2021/1/3
     * @param dataSource, tableName
     * @return java.sql.ResultSet
     */
    public static List<Map<String, Object>> getTableData(DataSource dataSource,String tableName) {
        final List<Map<String, Object>>[] rsMap = new ArrayList[1];
        try {

            // 使用jdbc原生方法获取
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.query("select * from "+tableName, rs->{
                rsMap[0]= JDBCUtil.parseResultSetToMapList(rs);
            });
            return rsMap[0];
        } catch (Throwable e) {
            // 获取表失败！
            return null;
        }
    }

}
