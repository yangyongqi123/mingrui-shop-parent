package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @ClassName CategoryService
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/22
 * @Version V1.0
 **/
@Api(tags = "商品分类接口")
public interface CategoryService {
    @ApiOperation(value = "通过查询商品分类")
    @GetMapping(value = "/category/list")
    Result<List<CategoryEntity>> getCategoryByPid(Integer pid);

    @ApiOperation(value = "通过Id删除分类")
    @DeleteMapping(value = "/category/delete")//前台请求方法   方法名必须一致
    Result<JsonObject> delCateGoryById(Integer id);
}
