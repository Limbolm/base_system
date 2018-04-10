package com.smpl.base.mapper;

import com.smpl.base.entity.DataMap;

import java.util.List;
/**
 * 公共mpper类
 *  做为通用的dao 提供最基础的RCUD
 *  *此为基础的dao层 只允许baseSevce 调用 其他的 业务sevce 全部继承 baseSevce
 * 1.findByPage 分页查询
 * 2.findById 根据id 查询数据
 * 3.findByList 不分页查询
 * 4.findByAttribute  根据条件查询 可有多个条件
 * 5.insert 新增
 * 6.deleteById 删除 根据id 单个传入id  多个传入 ids 以','分隔
 * 7.deleteByAttribute 根绝条件删除
 * 8.update 更新
 */
public interface BaseMapper<T> {
    /**
     * findByPage 分页查询
     * @param map
     * @return
     * @throws Exception
     */
    List<T> findByPage(String mapperStr,T map)throws Exception;

    /**
     * 根据id 查询
     * @param map
     * @return
     * @throws Exception
     */
    T findById(String mapperStr,T map)throws Exception;

    /**
     *  不分页查询
     * @param map
     * @return
     * @throws Exception
     */
    List<T> findByList(String mapperStr,T map)throws Exception;

    /**
     * 根绝条件查询
     * @param map
     * @return
     * @throws Exception
     */
    List<T> findByAttribute(String mapperStr,T map)throws Exception;

    /**
     * 新增
     * @param map
     * @return
     * @throws Exception
     */
    int insert(String mapperStr,T map)throws Exception;

    /**
     * 根据id 删除
     * @param map
     * @return
     * @throws Exception
     */
    int deleteById(String mapperStr,T map)throws Exception;

    /**
     * 根据条件 尽心删除
     * @param map
     * @return 返回删除 数目
     * @throws Exception
     */
    int deleteByAttribute(String mapperStr,T map)throws Exception;

    /**
     * 更新
     * @param map
     * @return
     * @throws Exception
     */
    int update(String mapperStr,T map)throws Exception;

    /**
     *
     */
    DataMap queryTableColum(DataMap map)throws Exception;


}
