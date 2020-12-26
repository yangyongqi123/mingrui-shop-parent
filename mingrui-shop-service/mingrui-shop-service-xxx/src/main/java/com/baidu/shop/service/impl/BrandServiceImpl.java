package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandeEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtils;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName BrandServiceImpl
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/25
 * @Version V1.0
 **/
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public Result<PageInfo<BrandeEntity>> getBrandInfo(BrandDTO brandDTO) {
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
