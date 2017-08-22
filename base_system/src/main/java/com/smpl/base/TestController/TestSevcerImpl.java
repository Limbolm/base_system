package com.smpl.base.TestController;

import com.smpl.base.entity.DataMap;
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
        map.put("tableName","t_account");
        try {
            return baseMapper.selectList(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void add() {

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
