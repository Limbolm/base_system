package com.smpl.base.service;

import com.smpl.base.Exception.BusinessException;
import com.smpl.base.Utils.PropertiesUtil;
import com.smpl.base.annotations.TableSeg;
import com.smpl.base.entity.DataMap;
import com.smpl.base.entity.PageInfo;
import com.smpl.base.mapper.BaseMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

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
    public List findByList(Object map) throws Exception {
        DataMap forMap=validate("Base-mapper.selectByAttribute",map);
        return baseMapper.findByList(forMap.getStr("MapperId"),forMap);
    }

    @Override
    public Object findById(Object map) throws Exception {
        DataMap forMap=validate("Base-mapper.selectById",map);
        return baseMapper.findById(forMap.getStr("MapperId"),forMap);
    }

    @Override
    public List findByAttribute(Object map) throws Exception {
        DataMap forMap=validate("Base-mapper.selectByAttribute",map);
        return baseMapper.findByAttribute(forMap.getStr("MapperId"),forMap);
    }

    @Override
    public int insert(Object map) throws Exception {
        DataMap forMap=validate( "Base-mapper.addEntity",map);
        return baseMapper.insert(forMap.getStr("MapperId"),forMap);
    }

    @Override
    public int bacthInsert(List maps) throws Exception {
        int i=0;
        for (Object map:maps) {
            DataMap forMap=validate( "Base-mapper.addEntity",map);
            baseMapper.insert(forMap.getStr("MapperId"),forMap);
            i++;
        }
        return i;
    }

    @Override
    public int deleteById(Object map) throws Exception {
        DataMap forMap=validate("Base-mapper.deleteByIds",map);
        return  baseMapper.deleteById(forMap.getStr("MapperId"),forMap);
    }

    @Override
    public int deleteByAttribute(Object map) throws Exception {
        DataMap forMap=validate("Base-mapper.deleteByAttribute",map);
        return baseMapper.deleteByAttribute(forMap.getStr("MapperId"),forMap);
    }

    @Override
    public int update(Object map) throws Exception {
        DataMap forMap=validate("Base-mapper.updateByid",map);
        return baseMapper.update(forMap.getStr("MapperId"),forMap);
    }

    @Override
    public int bacthUpdate(List maps) throws Exception {
        int i=0;
        for (Object map:maps) {
            DataMap forMap=validate( "Base-mapper.updateByid",map);
            baseMapper.update(forMap.getStr("MapperId"),forMap);
            i++;
        }
        return i;
    }

    @Override
    public DataMap queryTableColum(Object map) throws Exception {
        PropertiesUtil pro=new PropertiesUtil();
        String database_name=pro.getBaseDataName();
        DataMap forMap=validate("Base-mapper.updateByid",map);
        forMap.put("database_name",database_name);
        return baseMapper.queryTableColum(forMap);
    }


    /**
     * 数据校验 与封装
     * @param map
     * @param mapperStr
     * @return
     */
    private DataMap validate(String mapperStr,Object map){
        if (map instanceof Map){
            DataMap framsMap=forDataMap(map);
            if (!framsMap.containsKey("MapperId")) {
                forDataMap(map).put("MapperId", mapperStr);
            }
            if (framsMap.containsKey("tableName")) {
                throw new BusinessException("获取不到表名，查询失败！");
            }
            return framsMap;
        }else{


            return null;

        }

    }
    private DataMap forDataMap(Object formMap){
        return (DataMap)formMap;
    }

    public static void main(String[] args) {
        PageInfo pageInfo= new PageInfo();
        TableSeg tableSeg= pageInfo.getClass().getAnnotation(TableSeg.class);
        System.out.printf( tableSeg.tableName());
    }


}
