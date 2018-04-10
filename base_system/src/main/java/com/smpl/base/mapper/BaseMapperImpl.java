package com.smpl.base.mapper;

import com.smpl.base.Exception.BusinessException;
import com.smpl.base.Utils.StringUtils;
import com.smpl.base.entity.DataMap;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.formula.functions.T;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

/**
 * BaseMapper 实现类
 */

@Repository("baseMapper")
public class BaseMapperImpl extends SqlSessionDaoSupport implements BaseMapper {

    //使用sqlSessionFactory
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }


    @Override
    public List findByPage(String mapperStr,Object map) throws Exception {
        return getSqlSession().selectList(mapperStr, map);
    }

    @Override
    public Object findById(String mapperStr,Object map) throws Exception {
        return getSqlSession().selectOne(mapperStr, map);
    }

    @Override
    public List<Object> findByList(String mapperStr,Object map) throws Exception {
        return getSqlSession().selectList(mapperStr, map);
    }

    @Override
    public List<Object> findByAttribute(String mapperStr,Object map) throws Exception {
        return getSqlSession().selectList(mapperStr, map);
    }

    @Override
    public int insert(String mapperStr,Object map) throws Exception {;
        return getSqlSession().insert(mapperStr, map);
    }

    @Override
    public int deleteById(String mapperStr,Object map) throws Exception {
        return getSqlSession().delete(mapperStr, map);
    }

    @Override
    public int deleteByAttribute(String mapperStr,Object map) throws Exception {
        return getSqlSession().delete(mapperStr, map);
    }

    @Override
    public int update(String mapperStr,Object map) throws Exception {
        return getSqlSession().update(mapperStr, map);
    }

    @Override
    public DataMap queryTableColum(DataMap map) throws Exception {
        return getSqlSession().selectOne("Base-mapper.queryTableColum",map);
    }

}
