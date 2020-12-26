package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandeEntity;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "品牌接口")
public interface BrandService {

    @ApiOperation(value = "获取品牌信息")
    @GetMapping(value = "/brand/list")
    Result<PageInfo<BrandeEntity>>  getBrandInfo(BrandDTO brandDTO);
}
