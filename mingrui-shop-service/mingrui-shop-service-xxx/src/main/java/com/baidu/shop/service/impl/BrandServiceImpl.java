package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandeEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtils;
import com.baidu.shop.utils.ObjectUtil;
import com.baidu.shop.utils.PinyinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BrandServiceImpl
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/25
 * @Version V1.0
 **/
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    @Transactional
    @Override
    public Result<JSONObject> saveBrandInfo(BrandDTO brandDTO) {

        BrandeEntity brandeEntity = BaiduBeanUtils.copyProperties(brandDTO, BrandeEntity.class);

        brandeEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandeEntity.getName().toCharArray()[0]), false).toCharArray()[0]);

        brandMapper.insertSelective(brandeEntity);

        //维护中间表数据
        String categories = brandDTO.getCategories();
        if (StringUtils.isEmpty(brandDTO.getCategories())){
            return this.setResultError("错误");
        }
        ArrayList<CategoryBrandEntity> categoryBrandEntities  = new ArrayList<>();

        if (categories.contains(",")){
            categoryBrandMapper.insertList(
                    Arrays.asList(categories.split(","))
                            .stream()
                            .map(categoryIdStr -> new CategoryBrandEntity(Integer.valueOf(categoryIdStr),brandeEntity.getId()))
                            .collect(Collectors.toList())
            );
//            String[] categoryArr = categories.split(",");
//            for (String s:categoryArr) {
//                CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
//                categoryBrandEntity.setBrandId(brandeEntity.getId());
//                categoryBrandEntity.setCategoryId(Integer.valueOf(s));
//                categoryBrandEntities.add(categoryBrandEntity);
//            }
            categoryBrandMapper.insertList(categoryBrandEntities);
        }else{
            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setBrandId(brandeEntity.getId());
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));
            categoryBrandMapper.insert(categoryBrandEntity);
        }


        return this.setResultSuccess();
    }

    @Override
    public Result<PageInfo<BrandeEntity>> getBrandInfo(BrandDTO brandDTO) {

        if (!StringUtils.isEmpty(brandDTO.getSort())){
            PageHelper.orderBy(brandDTO.getSort() +" "+(Boolean.valueOf(brandDTO.getOrder())?"desc":"asc"));
        }

        //分页
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());

        BrandeEntity brandeEntity = BaiduBeanUtils.copyProperties(brandDTO, BrandeEntity.class);

        Example example = new Example(BrandeEntity.class);

        example.createCriteria().andLike("name", "%" + brandeEntity.getName() + "%");

        List<BrandeEntity> list = brandMapper.selectByExample(example);

        PageInfo<BrandeEntity> pageInfo = new PageInfo<>(list);
        return this.setResultSuccess(pageInfo);
    }
}
