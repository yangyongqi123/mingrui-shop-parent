package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import com.baidu.shop.utils.ObjectUtil;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/22
 * @Version V1.0
 **/
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {
        CategoryEntity entity = new CategoryEntity();
        entity.setParentId(pid);
        List<CategoryEntity> list = categoryMapper.select(entity);
        return this.setResultSuccess(list);
    }

    @Transactional//事务注解  增删改都加这个注解
    @Override
    public Result<JsonObject> delCateGoryById(Integer id) {
        //判断Id是否合法
        if (ObjectUtil.isNull(id)) return this.setResultError("Id不合法");

        //根据Id查询数据
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);

        //判断对象不为空
        if (ObjectUtil.isNull(categoryEntity)) return this.setResultError("数据不存在");

        //判断当前节点是否为父节点
        if (categoryEntity.getIsParent() == 1) return this.setResultError("当前节点为父节点");

        //相当于拼接 Sql
        Example example = new Example(CategoryEntity.class);
        example.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        List<CategoryEntity> categoryEntities = categoryMapper.selectByExample(example);

        if (categoryEntities.size() <= 1){

            CategoryEntity entity = new CategoryEntity();
            entity.setIsParent(0);
            entity.setId(categoryEntity.getParentId());

            categoryMapper.updateByPrimaryKeySelective(entity);
        }

        categoryMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }
}
