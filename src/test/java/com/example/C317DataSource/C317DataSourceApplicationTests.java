package com.example.C317DataSource;

import com.example.C317DataSource.utils.SqlUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class C317DataSourceApplicationTests {

    @Test
    void contextLoads() {
        try {
            String[] dbTypeStrings = new String[]{"mysql", "oracle", "oracleSid", "postgresql", "sqlserver", "sqlserverold", "dm", "sqlite", "h2Embedded", "h2Server", "access"};
            SqlUtil.connectAction("mysql","biabia.dxwang.top","3306","dx21601","root","123456");
            //SqlUtil.connectAction("postgresql","biabia.dxwang.top","5432","coaldata","postgres","123456");
            //SqlUtil.connectAction("sqlserver","127.0.0.1","1433","TMS","sa","123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
