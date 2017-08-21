package com.smpl.base.mapper;

import com.smpl.base.Exception.BusinessException;
import com.smpl.base.Utils.StringUtils;
import com.smpl.base.entity.DataMap;
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
    public DataMap selectById(DataMap map)throws Exception {
        map=builSqlData("Base-mapper.selectById",map);
        getSqlSession().selectOne(map.getStr("MapperId"),map);
        return map;
    }

    @Override
    public List<DataMap> selectList(DataMap map)throws Exception {
        map=builSqlData("Base-mapper.selectByAttribute",map);
        return getSqlSession().selectList(map.getStr("MapperId"),map);
    }

    @Override
    public void delete(DataMap map) throws Exception{
        map=builSqlData("Base-mapper.deleteByIds",map);
        getSqlSession().delete(map.getStr("MapperId"),map);
    }

    @Override
    public Integer update(DataMap map)throws Exception {
        map=builSqlData("Base-mapper.updateByid",map);
        return getSqlSession().update(map.getStr("MapperId"),map);
    }

    @Override
    public Integer insert(DataMap map) throws Exception{
        map=builSqlData("Base-mapper.addEntity",map);
        return getSqlSession().insert(map.getStr("MapperId"),map);
    }

    @Override
    public List<DataMap> selectByAttribute(DataMap map)throws Exception {
        map=builSqlData("Base-mapper.addEntity",map);
        List<DataMap> maps=getSqlSession().selectList(map.getStr("MapperId"),map);
        return maps;
    }

    /**
     * 构建sql方法
     */

    private DataMap builSqlData(String mapperStr,DataMap map)throws Exception{
        DataMap dataMap=new DataMap();
        //判断 map中是否 传入MapperId  如无则使用 mapperStr
        if (StringUtils.isEmpty(map.getStr(map.getMapperId()))){
            dataMap.put("MapperId",mapperStr);
        }else {
            dataMap.put("MapperId",map.getMapperId());
        }

        if (StringUtils.isEmpty(map.getStr("tableName"))){
            throw new BusinessException("获取不到表名，查询失败！");
        }
        return dataMap;
    }




}
