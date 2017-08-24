package com.smpl.base.TestController;

import com.smpl.base.entity.DataMap;
import com.smpl.base.entity.PageInfo;
import com.smpl.base.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by chenxiaopei on 2017/8/22.
 */
@Service("testServce")
public class TestSevcerImpl implements TestServce {

    @Autowired
    private BaseMapper baseMapper;
    @Override
    public List<DataMap> findByAll() {

        DataMap map=new DataMap();
        PageInfo pageInfo=new PageInfo();
        pageInfo.setPageNumber(1);
        pageInfo.setPageSize(5);
        map.put("tableName","t_account");
        map.put("pageInfo",pageInfo);
        try {
            return baseMapper.selectListqueryPage(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void add() {
        DataMap map=new DataMap();
        map.put("ACC_ID",3);
        map.put("ACC_CODE",3);
        map.put("ACC_NAME","Ago3");
        map.put("ACC_PASSWORD","Ago3");
        try {
            baseMapper.insert(map);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void delete() {

    }

    @Override
    public DataMap findById() {
        return null;
    }

    @Override
    public List<DataMap> findByName() {
        return null;
    }
}
