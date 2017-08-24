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
    private static DataMap builObject(DataMap<String,String> map){
        DataMap dataMap=new DataMap<String,String>();
        // 构造 字段值
        StringBuffer keyBuffer=new StringBuffer("(");
        StringBuffer valueBuffer=new StringBuffer("(");

        int i=0;
        for (Map.Entry<String,String> entry:map.entrySet()){
            i++;
            if (!(entry.getKey().equals("MapperId")||entry.getKey().equals("tableName")||entry.getKey().equals("id"))){
                keyBuffer.append(entry.getKey());
                valueBuffer.append(entry.getValue());
                if (i!=(map.size()-1)){
                    keyBuffer.append(",");
                    valueBuffer.append(",");
                }
            }

        }

        keyBuffer.append(")");
        valueBuffer.append(")");
        dataMap.put("MapperId",map.getStr("MapperId"));
        dataMap.put("tableName",map.getStr("tableName"));
        dataMap.put("key",keyBuffer.toString());
        dataMap.put("value",valueBuffer.toString());
        if (!StringUtils.isEmpty(map.getStr("id"))){
            dataMap.put("id",map.getStr("id"));
        }
        return dataMap;
        }

    public static void main(String[] args) {

        DataMap dataMap=new DataMap();
        dataMap.put("MapperId","testMapperID");
        dataMap.put("tableName","tsetTableName");
        dataMap.put("key","1");
        dataMap.put("value","value1");
        dataMap.put("key2","2");
        dataMap.put("value2","value2");
        dataMap.put("key3","3");
        dataMap.put("value3","value3");

        DataMap map=builObject(dataMap);

        System.out.println(map.toString());
    }

}
