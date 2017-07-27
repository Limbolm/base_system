package com.smpl.base.mapper;

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
    Map<String,Objects> selectById(Map<String, Objects> map);

    /**
     * 根据实体类查询数据
     * @param map
     * @return
     */
    List<Map<String,Objects>> selectList(Map<String, Objects> map);

    /**
     * 根据属性查询数据
     * @param map
     * @return
     */
    List<Map<String,Objects>> selectByAttribute(Map<String,Objects> map);


    /**
     * 删除
     * @param map
     */
    void delete(Map<String,Objects> map);


    /**
     * 更新
     * @param map
     * @return
     */
    Integer update(Map<String,Objects> map);

    /**
     * 新增
     * @param map
     * @return
     */
    Integer insert(Map<String,Objects> map);




}
