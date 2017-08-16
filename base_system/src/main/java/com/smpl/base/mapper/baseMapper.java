package com.smpl.base.mapper;

import com.smpl.base.entity.DataMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 公共mpper类
 */
public interface baseMapper {

    /**
     * 根据id查询数据
     * @param map
     * @return
     */
    DataMap selectById(DataMap map);

    /**
     * 根据实体类查询数据
     * @param map
     * @return
     */
    List<DataMap> selectList(DataMap map);

    /**
     * 根据属性查询数据
     * @param map
     * @return
     */
    List<DataMap> selectByAttribute(DataMap map);


    /**
     * 删除
     * @param map
     */
    void delete(DataMap map);


    /**
     * 更新
     * @param map
     * @return
     */
    Integer update(DataMap map);

    /**
     * 新增
     * @param map
     * @return
     */
    Integer insert(DataMap map);




}
