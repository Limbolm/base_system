package com.smpl.base.service;

import com.smpl.base.entity.DataMap;

import java.util.List;

/**
 *   基础业务类 所有类都继承该类 对dao层进行操作
//                   _ooOoo_
//                  o8888888o
//                  88" . "88
//                  (| -_- |)
//                  O\  =  /O
//               ____/`---'\____
//             .'  \\|     |//  `.
//            /  \\|||  :  |||//  \
//           /  _||||| -:- |||||-  \
//           |   | \\\  -  /// |   |
//           | \_|  ''\---/''  |   |
//           \  .-\__  `-`  ___/-. /
//         ___`. .'  /--.--\  `. . __
//      ."" '<  `.___\_<|>_/___.'  >'"".
//     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//     \  \ `-.   \_ __\ /__ _/   .-` /  /
// ======`-.____`-.___\_____/___.-`____.-'======
//                   `=---='
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//         佛祖保佑       永无BUG
//  本模块已经经过开光处理，绝无可能再产生bug
// =============================================
 */
public interface BaseService <T>{

    /**
     *查询
     * @param map
     * @return
     */
    List<T> findByList(T map)throws Exception;

    /**
     * 根据Id查询
     * @param map
     * @return
     * @throws Exception
     */
    T findById(T map)throws Exception;

    /**
     *  根据条件查询
     * @param map
     * @return
     * @throws Exception
     */
    List<T> findByAttribute(T map)throws Exception;

    /**
     *  新增
     * @param map
     * @return
     * @throws Exception
     */
    int insert(T map)throws Exception;

    /**
     * 批量新增
     * @param maps
     * @return
     * @throws Exception
     */
    int bacthInsert(List<T> maps)throws Exception;

    /**
     * 根据id删除
     * @param map
     * @return
     * @throws Exception
     */
    int deleteById(T map)throws Exception;

    /**
     * 根据条件删除
     * @param map
     * @return
     * @throws Exception
     */
    int deleteByAttribute(T map)throws Exception;

    /**
     *  更新
     * @param map
     * @return
     * @throws Exception
     */
    int update(T map)throws Exception;

    /**
     * 批量更新
     * @param maps
     * @return
     * @throws Exception
     */
    int bacthUpdate(List<T> maps)throws Exception;

    /**
     * 根据数据库名及表名查询表的字段
     * @param map
     * @return
     * @throws Exception
     */
    DataMap queryTableColum(T map) throws Exception;


}
