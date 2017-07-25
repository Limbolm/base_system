package com.smpl.base.mapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 公共mpper类
 */
public interface baseMapper<T> {

    /**
     * 根据id查询数据
     * @param id
     * @param clazz
     * @return
     */
    Map<String,Objects> selectById(String id,Class clazz);

    /**
     * 根据实体类查询数据
     * @param clazz
     * @return
     */
    List<Map<String,Objects>> selectList(Class clazz);

    /**
     * 根据属性查询数据
     * @param map
     * @return
     */
    List<Map<String,Objects>> selectByAttribute(Map<String,Objects> map);

}
