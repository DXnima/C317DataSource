package com.example.C317DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class C317DataSourceApplication {

    public static String port;
    public static String path;

    @Value("${server.port}")
    public void setPort(String port) {
        C317DataSourceApplication.port = port;
    }
    @Value("${server.servlet.context-path}")
    public void setPath(String path) {
        C317DataSourceApplication.path = path;
    }

    public static void main(String[] args) {
        SpringApplication.run(C317DataSourceApplication.class, args);
        printServerUrl();
    }

    private static void printServerUrl() {

        String url = String.format("http://%s%s%s%s", "localhost" + ":",port , path,"doc.html");
        System.err.println("-----------------------------------------------------------------");
        System.err.println("##### 程序启动成功！");
        System.err.println("##### 点击API接口文档 : " + url);
        System.err.println("-----------------------------------------------------------------");

    }

}
