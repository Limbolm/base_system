package com.smpl.base.mapper;

import com.smpl.base.entity.DataMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 公共mpper类
 */
public interface BaseMapper {

    /**
     * 根据id查询数据
     * @param map
     * @return
     */
    DataMap selectById(DataMap map)throws Exception;

    /**
     * 根据实体类查询数据
     * @param map
     * @return
     */
    List<DataMap> selectList(DataMap map)throws Exception;

    /**
     * 根据属性查询数据
     * @param map
     * @return
     */
    List<DataMap> selectByAttribute(DataMap map)throws Exception;


    /**
     * 删除
     * @param map
     */
    void delete(DataMap map)throws Exception;


    /**
     * 更新
     * @param map
     * @return
     */
    Integer update(DataMap map)throws Exception;

    /**
     * 新增
     * @param map
     * @return
     */
    Integer insert(DataMap map)throws Exception;

    /**
     * 分页查询
     * @param map
     * @return
     * @throws Exception
     */
    List<DataMap> selectListqueryPage(DataMap map)throws Exception;




}
