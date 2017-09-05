package com.smpl.base.service;

import com.smpl.base.entity.DataMap;
import com.smpl.base.mapper.BaseMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
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

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service("baseService")
public class BaseServiceImpl implements BaseService{


    @Inject
    private BaseMapper baseMapper;


    @Override
    public List<DataMap> findByList(DataMap map) throws Exception {
        return null;
    }

    @Override
    public DataMap findById(DataMap map) throws Exception {
        return null;
    }

    @Override
    public List<DataMap> findByAttribute(DataMap map) throws Exception {
        return null;
    }

    @Override
    public int insert(DataMap map) throws Exception {
        return 0;
    }

    @Override
    public int bacthInsert(List<DataMap> maps) throws Exception {
        return 0;
    }

    @Override
    public int deleteById(DataMap map) throws Exception {
        return 0;
    }

    @Override
    public int deleteByAttribute(DataMap map) throws Exception {
        return 0;
    }

    @Override
    public int update(DataMap map) throws Exception {
        return 0;
    }

    @Override
    public int bacthUpdate(List<DataMap> maps) throws Exception {
        return 0;
    }

    @Override
    public DataMap queryTableColum(DataMap map) throws Exception {
        return null;
    }
}
