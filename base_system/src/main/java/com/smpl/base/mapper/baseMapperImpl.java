package com.smpl.base.mapper;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by chenxiaopei on 2017/7/26.
 */
public class baseMapperImpl extends SqlSessionDaoSupport implements baseMapper {

    //使用sqlSessionFactory
    @Autowired
    private  SqlSessionFactory sqlSessionFactory;

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory)
    {
        super.setSqlSessionFactory(sqlSessionFactory);
    }





    @Override
    public Map<String, Objects> selectById(Map<String, Objects> map) {

        getSqlSession().selectOne("");
        return null;
    }

    @Override
    public List<Map<String, Objects>> selectList(Map<String, Objects> map) {
        return getSqlSession().selectList("");
    }

    @Override
    public List<Map<String, Objects>> selectByAttribute(Map map) {
        return null;
    }
}
