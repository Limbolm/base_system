package com.smpl.base.mapper;

import com.smpl.base.Exception.BusinessException;
import com.smpl.base.Utils.StringUtils;
import com.smpl.base.entity.DataMap;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
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
        map=builObject(map);
        return getSqlSession().update(map.getStr("MapperId"),map);
    }

    @Override
    public Integer insert(DataMap map) throws Exception{
        map=builSqlData("Base-mapper.addEntity",map);
        map=builObject(map);
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

        //判断 map中是否 传入MapperId  如无则使用 mapperStr
        if (StringUtils.isEmpty(map.getStr(map.getMapperId()))){
            map.put("MapperId",mapperStr);
        }else {
            map.put("MapperId",map.getMapperId());
        }

        if (StringUtils.isEmpty(map.getStr("tableName"))){
            throw new BusinessException("获取不到表名，查询失败！");
        }
        return map;
    }

    /**
     * 为update add 方法构造参数对象
     * @param map
     * @return
     */
    private DataMap builObject(DataMap map){
        DataMap dataMap=new DataMap();
        // 构造 字段值
        StringBuffer keyBuffer=new StringBuffer("(");
        StringBuffer valueBuffer=new StringBuffer("(");
        Iterator<String> iterator=map.keySet().iterator();
        while (iterator.hasNext()){
            if (!iterator.next().equals("MapperId")||!iterator.next().equals("tableName")||iterator.next().equals("id")){
                keyBuffer.append(iterator.next());
                keyBuffer.append(",");
                valueBuffer.append(map.getStr(iterator.next()));
                valueBuffer.append(",");
            }
        }

        keyBuffer.toString().replace(keyBuffer.toString().charAt(keyBuffer.toString().length()-1), ')');
        valueBuffer.toString().replace(valueBuffer.toString().charAt(valueBuffer.toString().length()-1), ')');
        dataMap.put("MapperId",map.getStr("MapperId"));
        dataMap.put("tableName",map.getStr("tableName"));
        dataMap.put("key",keyBuffer.toString());
        dataMap.put("value",valueBuffer.toString());
        if (!StringUtils.isEmpty(map.getStr("id"))){
            dataMap.put("id",map.getStr("id"));
        }
        return dataMap;
        }


}
