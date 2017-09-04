package com.smpl.base.mapper;

import com.smpl.base.entity.DataMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 公共mpper类
 *  做为通用的dao 提供最基础的RCUD
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

    /**
     *
     *此为基础的dao层 只允许baseSevce 调用 其他的 业务sevce 全部继承 baseSevce
     * 1.findByPage 分页查询
     * 2.findById 根据id 查询数据
     * 3.findByList 不分页查询
     * 4.findByAttribute  根据条件查询 可有多个条件
     * 5.insert 新增
     * 6.deleteById 删除 根据id 单个传入id  多个传入 ids 以','分隔
     * 7.deleteByAttribute 根绝条件删除
     * 8.update 更新
     */




}
