package com.example.C317DataSource.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import org.apache.commons.lang.StringUtils;
import org.h2.table.TableType;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.*;
import java.util.*;

/**
 * Data2JsonUtil
 *
 * @author wnm
 * @date 2020/12/29
 */
public class JDBCUtil {

    public static String getDbDefaultPort(String DB_TYPE) {
        if ("sqlserverold".equalsIgnoreCase(DB_TYPE)) {
            DB_TYPE = "sqlserver";
        } else if ("oracleSid".equalsIgnoreCase(DB_TYPE)) {
            DB_TYPE = "oracle";
        }
        if ("sqlserver".equalsIgnoreCase(DB_TYPE)) {
            return "1433";
        } else if ("oracle".equalsIgnoreCase(DB_TYPE)) {
            return "1521";
        } else if ("mysql".equalsIgnoreCase(DB_TYPE)) {
            return "3306";
        } else if ("postgresql".equalsIgnoreCase(DB_TYPE)) {
            return "5432";
        } else if ("dm".equalsIgnoreCase(DB_TYPE)) {
            return "5236";
        } else if ("h2Server".equalsIgnoreCase(DB_TYPE)) {
            return "9092";
        }
        return null;
    }

    /**
     * 获取jdbc连接地址
     */
    public static String getJdbcUrl(String jdbcUrl, String DB_TYPE, String dbIp, String dbPort, String dbName) {
        if (StringUtils.isNotEmpty(jdbcUrl)) {
            return jdbcUrl;
        }
        if ("sqlserver".equalsIgnoreCase(DB_TYPE)) {
            //jdbc:sqlserver://localhost:3433;DatabaseName=dbname
            jdbcUrl = String.format("jdbc:sqlserver://%s:%s;DatabaseName=%s", dbIp, dbPort, dbName);
        } else if ("oracle".equalsIgnoreCase(DB_TYPE)) {
            //jdbc:oracle:thin:@[HOST_NAME]:PORT:[DATABASE_NAME]
            jdbcUrl = String.format("jdbc:oracle:thin:@%s:%s/%s", dbIp, dbPort, dbName);
        } else if ("oracleSid".equalsIgnoreCase(DB_TYPE)) {
            //jdbc:oracle:thin:@[HOST_NAME]:PORT:[DATABASE_NAME]
            jdbcUrl = String.format("jdbc:oracle:thin:@%s:%s:%s", dbIp, dbPort, dbName);
        } else if ("mysql".equalsIgnoreCase(DB_TYPE)) {
            //jdbc:mysql://bad_ip:3306/database
            jdbcUrl = String.format("jdbc:mysql://%s:%s/%s", dbIp, dbPort, dbName);
        } else if ("postgresql".equalsIgnoreCase(DB_TYPE)) {
            jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", dbIp, dbPort, dbName);
        } else if ("sqlserverold".equalsIgnoreCase(DB_TYPE)) {
            jdbcUrl = String.format("jdbc:jtds:sqlserver://%s:%s/%s", dbIp, dbPort, dbName);
        } else if ("dm".equalsIgnoreCase(DB_TYPE)) {
            jdbcUrl = String.format("jdbc:dm://%s:%s/%s", dbIp, dbPort, dbName);
        } else if ("sqlite".equalsIgnoreCase(DB_TYPE)) {
            jdbcUrl = String.format("jdbc:sqlite:%s", dbName);
        } else if ("h2Embedded".equalsIgnoreCase(DB_TYPE)) {
            jdbcUrl = String.format("jdbc:h2:%s", dbName);
        } else if ("h2Server".equalsIgnoreCase(DB_TYPE)) {
            jdbcUrl = String.format("jdbc:h2:tcp://%s:%s/%s", dbIp, dbPort, dbName);
        } else if ("access".equalsIgnoreCase(DB_TYPE)) {
            jdbcUrl = String.format("jdbc:ucanaccess://%s", dbName);
        }
        System.out.print("\n解析出jdbcUrl: " + jdbcUrl);
        return jdbcUrl;
    }

    public static String convertDatabaseCharsetType(String userName, String schema, String type) {
        if ("sqlserverold".equalsIgnoreCase(type)) {
            type = "sqlserver";
        } else if ("oracleSid".equalsIgnoreCase(type)) {
            type = "oracle";
        }
        String dbUser;
        if ("oracle".equals(type)) {
            dbUser = StringUtils.defaultIfBlank(schema, userName).toUpperCase();
        } else if ("postgresql".equals(type)) {
            dbUser = StringUtils.defaultIfBlank(schema, "public");
        } else if ("mysql".equals(type)) {
            dbUser = null;
        } else if ("sqlserver".equals(type)) {
            dbUser = StringUtils.defaultIfBlank(schema, "dbo");
        } else if ("db2".equals(type)) {
            dbUser = StringUtils.defaultIfBlank(schema, userName).toUpperCase();
        } else if ("h2Server".equals(type)) {
            dbUser = StringUtils.defaultIfBlank(schema, "PUBLIC");
        } else {
            dbUser = StringUtils.defaultIfBlank(schema, userName);
        }
        return dbUser;
    }

    /**
     * 获取ResultSet
     * @author wnm
     * @date 2021/1/3
     * @param dataSource, sql
     * @return java.sql.ResultSet
     */
    public static ResultSet getResultSetBySql(DataSource dataSource,String sql) {
        try {
            final ResultSet[] rs = new ResultSet[1];
            // 使用jdbc原生方法获取
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.query(sql, r->{
                rs[0] =r;
            });
            return rs[0];
        } catch (Throwable e) {
            // 获取表失败！
            return null;
        }
    }

    /**
     * 将一个未处理的ResultSet解析为Map列表.
     * @param rs
     * @return
     */
    public static List<Map<String, Object>> parseResultSetToMapList(ResultSet rs) {
        //
        List<Map<String, Object>> result = new ArrayList<>();
        //
        if (null == rs) {
            return null;
        }
        //
        try {
            while (rs.next()) {
                //
                Map<String, Object> map = parseResultSetToMap(rs);
                //
                if (null != map) {
                    result.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //
        return result;
    }

    /**
     * 解析ResultSet的单条记录,不进行 ResultSet 的next移动处理
     *
     * @param rs
     * @return
     */
    private static Map<String, Object> parseResultSetToMap(ResultSet rs) {
        //
        if (null == rs) {
            return null;
        }
        //
        Map<String, Object> map = new HashMap<String, Object>();
        //
        try {
            ResultSetMetaData meta = rs.getMetaData();
            //
            int colNum = meta.getColumnCount();
            //
            for (int i = 1; i <= colNum; i++) {
                // 列名
                String name = meta.getColumnLabel(i); // i+1
                Object value = rs.getObject(i);
                // 加入属性
                map.put(name, value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //
        return map;
    }

    public static void close(Connection conn) {
        if(conn!=null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //
    public static void close(Statement stmt) {
        if(stmt!=null) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //
    public static void close(ResultSet rs) {
        if(rs!=null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
