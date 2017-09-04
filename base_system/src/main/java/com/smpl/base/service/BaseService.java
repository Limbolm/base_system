package com.smpl.base.service;

import com.smpl.base.entity.DataMap;

import java.util.List;

/**
 *   基础业务类 所有类都继承该类 对dao层进行操作
 */
public interface BaseService {

    /**
     *查询
     * @param map
     * @return
     */
    List<DataMap> findByList(DataMap map)throws Exception;

    /**
     * 根据Id查询
     * @param map
     * @return
     * @throws Exception
     */
    DataMap findById(DataMap map)throws Exception;

    /**
     *  根据条件查询
     * @param map
     * @return
     * @throws Exception
     */
    List<DataMap> findByAttribute(DataMap map)throws Exception;

    /**
     *  新增
     * @param map
     * @return
     * @throws Exception
     */
    int insert(DataMap map)throws Exception;

    /**
     * 批量新增
     * @param maps
     * @return
     * @throws Exception
     */
    int bacthInsert(List<DataMap> maps)throws Exception;

    /**
     * 根据id删除
     * @param map
     * @return
     * @throws Exception
     */
    int deleteById(DataMap map)throws Exception;

    /**
     * 根据条件删除
     * @param map
     * @return
     * @throws Exception
     */
    int deleteByAttribute(DataMap map)throws Exception;

    /**
     *  更新
     * @param map
     * @return
     * @throws Exception
     */
    int update(DataMap map)throws Exception;

    /**
     * 批量更新
     * @param maps
     * @return
     * @throws Exception
     */
    int bacthUpdate(List<DataMap> maps)throws Exception;


}
