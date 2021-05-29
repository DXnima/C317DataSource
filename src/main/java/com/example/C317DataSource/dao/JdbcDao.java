package com.example.C317DataSource.dao;

import com.example.C317DataSource.model.Connect;
import com.example.C317DataSource.model.Jdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.sqlite.JDBC;

/**
 * JDBCDao
 *
 * @author wnm
 * @date 2021/1/4
 */
@Component
public class JdbcDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 创建对象
     */
    public String insert(Jdbc jdbc) {
        Jdbc jdbc1=mongoTemplate.save(jdbc);
        return jdbc1.getId();
    }

    /**
     * 根据用户名查询对象
     * @return
     */
    public Jdbc selectById(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query , Jdbc.class);
    }

    /**
     * 更新对象
     */
    public void update(Jdbc jdbc) {
        Query query=new Query(Criteria.where("_id").is(jdbc.getId()));
        Update update= new Update().set("connect", jdbc.getConnect()).set("name",jdbc.getName()).set("updated",jdbc.getUpdated());
        //更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query,update, Jdbc.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,TestEntity.class);
    }

    /**
     * 删除对象
     * @param id
     */
    public void deleteById(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Jdbc.class);
    }

}
