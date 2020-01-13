package com.jimu.study.controller;

import com.jimu.study.model.VipType;
import com.jimu.study.service.VipTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/vipType")
public class VipTypeController {

    @Autowired
    private VipTypeService vipTypeService;

    @PostMapping("/insert")
    public Integer insert(@RequestBody VipType vipType){
        return vipTypeService.insert(vipType);
    }

    @ApiOperation("返回VIP列表")
    @GetMapping("/findVipList")
    public List<VipType> findVipList(){
        return vipTypeService.findVipList();
    }
}
