package com.example.C317DataSource.controller;

import com.example.C317DataSource.common.ServerResponse;
import com.example.C317DataSource.model.Connect;
import com.example.C317DataSource.model.Jdbc;
import com.example.C317DataSource.service.IJdbcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * JDBCController
 *
 * @author wnm
 * @date 2021/1/3
 */
@Api(tags = "连接数据库接口")
@RestController
public class JdbcController {
    @Autowired
    private IJdbcService iJdbcService;

    //获取所有连接类型
    @ApiOperation(value = "获取支持数据的连接类型", notes = "获取所有连接类型，目前支持数据库有：\"mysql\", \"oracle\", \"postgresql\", \"sqlserver\", \"sqlserverold\"")
    @RequestMapping(value = "/type", method = RequestMethod.GET)
    public ServerResponse<String[]> getType(){
        return iJdbcService.getType();
    }

    // 获取指定指定数据库的默认端口
    @ApiOperation(value = "获取指定指定数据库的默认端口", notes = "目前支持数据库有：\"mysql\", \"oracle\", \"postgresql\", \"sqlserver\", \"sqlserverold\"")
    @ApiImplicitParam(name = "type",value = "字符串类型，目前支持数据库有：\"mysql\", \"oracle\", \"postgresql\", \"sqlserver\", \"sqlserverold\"",required = true,dataType = "String",paramType = "query")
    @RequestMapping(value = "/port", method = RequestMethod.GET)
    public ServerResponse<String> getPort(String type){
        return iJdbcService.getPort(type);
    }

    // 创建连接
    @ApiOperation(value = "创建连接", notes = "创建连接，获取数据库表")
    @RequestMapping(value = "/connection", method = RequestMethod.POST)
    public ServerResponse<Map<String,Object>> creatJDBC(@RequestBody Connect connect){
        return iJdbcService.creatJDBC(connect);
    }

    // 获取指定表数据
    @ApiOperation(value = "获取指定表数据", notes = "创建连接成功后，获取指定表数据")
    @RequestMapping(value = "/getTableData", method = RequestMethod.POST)
    public ServerResponse<Map<String,Object>> getTableData(@RequestBody Map<String,String> map){
        String id=map.get("id");
        String tableName=map.get("tableName");
        return iJdbcService.getTableData(id,tableName);
    }

    // 修改连接信息
    @ApiOperation(value = "修改连接信息", notes = "修改连接信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    ServerResponse<Map<String,Object>> update(@RequestBody Jdbc jdbc){
        return iJdbcService.update(jdbc);
    }

    // 删除连接信息
    @ApiOperation(value = "删除连接信息", notes = "删除连接信息")
    @ApiImplicitParam(name = "id",value = "字符串类型")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    ServerResponse<String> deleteById(String id){
        return iJdbcService.deleteById(id);
    }
}
