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
 * Created by chenxiaopei on 2017/8/27.
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
    public DataMap selectById(DataMap map) throws Exception {
        map = builSqlData("Base-mapper.selectById", map);
        getSqlSession().selectOne(map.getStr("MapperId"), map);
        return map;
    }

    @Override
    public List<DataMap> selectList(DataMap map) throws Exception {
        map = builSqlData("Base-mapper.selectByAttribute", map);
        return getSqlSession().selectList(map.getStr("MapperId"), map);
    }

    @Override
    public List<DataMap> selectByAttribute(DataMap map) throws Exception {
        map = builSqlData("Base-mapper.selectByAttribute", map);
        return getSqlSession().selectList(map.getStr("MapperId"), map);
    }

    @Override
    public void delete(DataMap map) throws Exception {
        map = builSqlData("Base-mapper.deleteByIds", map);
        getSqlSession().delete(map.getStr("MapperId"), map);
    }

    @Override
    public Integer update(DataMap map) throws Exception {
        map = builSqlData("Base-mapper.updateByid", map);
        return getSqlSession().update(map.getStr("MapperId"), map);
    }

    @Override
    public Integer insert(DataMap map) throws Exception {
        map = builSqlData("Base-mapper.addEntity", map);
        return getSqlSession().insert(map.getStr("MapperId"), map);
    }

    @Override
    public List<DataMap> selectListqueryPage(DataMap map) throws Exception {
        map = builSqlData("Base-mapper.QueryBypage", map);
        return getSqlSession().selectList(map.getStr("MapperId"), map);
    }

    /**
     * 构建sql方法
     */

    private DataMap builSqlData(String mapperStr, DataMap map) throws Exception {

        //判断 map中是否 传入MapperId  如无则使用 mapperStr
        if (!map.containsKey("MapperId")) {
            map.put("MapperId", mapperStr);
        } else {
            map.put("MapperId", map.getMapperId());
        }

        if (map.containsKey("tableName")) {
            throw new BusinessException("获取不到表名，查询失败！");
        }
        return map;
    }


}
