package com.smpl.base.TestController;

import com.smpl.base.entity.DataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by chenxiaopei on 2017/8/22.
 */
@Controller
@RequestMapping(value = "/test")
public class Test {

@Autowired
private TestServce testServce;

    @RequestMapping("/findByAll")
    public List<DataMap> queryByList(){
       /* List<DataMap> list=testServce.findByAll();*/
        testServce.add();
        return null;

    }




}
