package com.smpl.base.mapper;

import com.smpl.base.Exception.BusinessException;
import com.smpl.base.Utils.StringUtils;
import com.smpl.base.entity.DataMap;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * BaseMapper 实现类
 */

@Service(value = "baseMapper")
public class BaseMapperImpl extends SqlSessionDaoSupport implements BaseMapper {

    //使用sqlSessionFactory
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }


    @Override
    public List<DataMap> findByPage(DataMap map) throws Exception {
        verification("Base-mapper.selectQueryPage",map);
        return getSqlSession().selectList(map.getStr("MapperId"), map);
    }

    @Override
    public DataMap findById(DataMap map) throws Exception {
        verification("Base-mapper.selectById",map);
        return getSqlSession().selectOne(map.getStr("MapperId"), map);
    }

    @Override
    public List<DataMap> findByList(DataMap map) throws Exception {
        verification("Base-mapper.selectByAttribute",map);
        return getSqlSession().selectList(map.getStr("MapperId"), map);
    }

    @Override
    public List<DataMap> findByAttribute(DataMap map) throws Exception {
        verification("Base-mapper.selectByAttribute",map);
        return getSqlSession().selectList(map.getStr("MapperId"), map);
    }

    @Override
    public int insert(DataMap map) throws Exception {
        verification("Base-mapper.addEntity",map);
        return getSqlSession().insert(map.getStr("MapperId"), map);
    }

    @Override
    public int deleteById(DataMap map) throws Exception {
        verification("Base-mapper.deleteByIds",map);
        return getSqlSession().delete(map.getStr("MapperId"), map);
    }

    @Override
    public int deleteByAttribute(DataMap map) throws Exception {
        verification("Base-mapper.deleteByAttribute",map);
        return getSqlSession().delete(map.getStr("MapperId"), map);
    }

    @Override
    public int update(DataMap map) throws Exception {
        verification("Base-mapper.updateByid",map);
        return getSqlSession().update(map.getStr("MapperId"), map);
    }

    /**
     * 检验关键key 是否存在
     */
    private void verification(String mapperStr, DataMap map) throws Exception {

        //判断 map中是否 传入MapperId  如无则使用 mapperStr
        if (!map.containsKey("MapperId")) {
            map.put("MapperId", mapperStr);
        } else {
            map.put("MapperId", map.getMapperId());
        }

        if (map.containsKey("tableName")) {
            throw new BusinessException("获取不到表名，查询失败！");
        }
    }
}
