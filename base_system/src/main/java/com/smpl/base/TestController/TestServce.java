package com.smpl.base.TestController;

import com.smpl.base.entity.DataMap;

import java.util.List;

/**
 * Created by chenxiaopei on 2017/8/22.
 */
public interface TestServce {


    List<DataMap> findByAll();
    void add();
    void delete();
    DataMap findById();
    List<DataMap> findByName();



}
