package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("规格接口")
public interface SpecificationService {
    @ApiOperation("通过条件查询规则数据")
    @GetMapping("specgroup/getSpecGroupInfo")
    Result<List<SpecGroupEntity>> getSpecGroupInfo(SpecGroupDTO specGroupDTO);

    @ApiOperation("新增规则数据")
    @PostMapping("specgroup/save")
    Result<JSONObject> saveSpecGroupInfo(@RequestBody  SpecGroupDTO specGroupDTO);

    @ApiOperation("修改规则数据")
    @PutMapping("specgroup/save")
    Result<JSONObject> editSpecGroupInfo(@RequestBody  SpecGroupDTO specGroupDTO);

    @ApiOperation("删除规则数据")
    @DeleteMapping("specgroup/delete")
    Result<JSONObject> deleteSpecGroupInfo(Integer id);


}
