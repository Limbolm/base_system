package com.smpl.base.mapper;

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
    public DataMap selectById(DataMap map) {

        getSqlSession().selectOne("");
        return null;
    }

    @Override
    public List<DataMap> selectList(DataMap map) {
        return getSqlSession().selectList("");
    }

    @Override
    public void delete(DataMap map) {

    }

    @Override
    public Integer update(DataMap map) {
        return null;
    }

    @Override
    public Integer insert(DataMap map) {
        return null;
    }

    @Override
    public List<DataMap> selectByAttribute(DataMap map) {
        return null;
    }

    /**
     * 构建sql方法
     */

    private DataMap builSqlData(DataMap map){
        DataMap dataMap=new DataMap();
        if (!StringUtils.isEmpty(map.getMapperId())){
            dataMap.put("mapperId",map.getMapperId());
        }

        return null;
    }

}
