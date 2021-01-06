package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.BrandeEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.entity.SpuEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.mapper.SpuMapper;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.BaiduBeanUtils;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName GoodsServiceImpl
 * @Description: TODO
 * @Author yyq
 * @Date 2021/1/5
 * @Version V1.0
 **/
@RestController
public class GoodsServiceImpl extends BaseApiService implements GoodsService {

    @Resource
    private SpuMapper spuMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private BrandMapper brandMapper;

    @Override
    public Result<PageInfo<SpuDTO>> getSpuInfo(SpuDTO spuDTO) {
        if (ObjectUtil.isNotNull(spuDTO.getPage()) && ObjectUtil.isNotNull(spuDTO.getRows()))
        PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());

        Example example = new Example(SpuEntity.class);
        Example.Criteria criteria = example.createCriteria();

        if (ObjectUtil.isNotNull(spuDTO.getSaleable()) && spuDTO.getSaleable() < 2)
            criteria.andEqualTo("saleable",spuDTO.getSaleable());
        if (!StringUtils.isEmpty(spuDTO.getTitle()))
            criteria.andLike("title","%"+spuDTO.getTitle()+"%");

        List<SpuEntity> spuEntities = spuMapper.selectByExample(example);

        List<SpuDTO> spuDTOList = spuEntities.stream().map(spuEntity -> {
            SpuDTO spuDTO1 = BaiduBeanUtils.copyProperties(spuEntity, SpuDTO.class);
            //根据cid1,cid2,cid3查询分类名称
            /*CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(spuDTO1.getCid1());
            CategoryEntity categoryEntity2 = categoryMapper.selectByPrimaryKey(spuDTO1.getCid2());
            CategoryEntity categoryEntity3 = categoryMapper.selectByPrimaryKey(spuDTO1.getCid3());
            spuDTO1.setCategoryName(categoryEntity.getName() + "/" + categoryEntity2.getName() + "/" + categoryEntity3.getName());*/

            List<CategoryEntity> categoryEntities = categoryMapper.selectByIdList(Arrays.asList(spuEntity.getCid1(), spuEntity.getCid2(), spuEntity.getCid3()));
            String categoryName = categoryEntities.stream().map(categoryEntity -> categoryEntity.getName()).collect(Collectors.joining("/"));
            spuDTO1.setCategoryName(categoryName);

           /* String categoryName = "";
            List<String> strings = new ArrayList<>();
            strings.set(0,"");
            categoryEntities.stream().forEach(categoryEntity -> {
                strings.set(0,strings.get(0) + categoryEntity.getName() + "/");
            });
            categoryName = strings.get(0).substring(0,strings.get(0).length());*/

            //根据brandId查询品牌名称
            BrandeEntity brandeEntity = brandMapper.selectByPrimaryKey(spuDTO1.getBrandId());
            spuDTO1.setBrandName(brandeEntity.getName());
            return spuDTO1;
        }).collect(Collectors.toList());

        PageInfo<SpuEntity> spuEntityPageInfo = new PageInfo<>(spuEntities);
       // return this.setResultSuccess(spuEntityPageInfo);
        return this.setResult(HTTPStatus.OK,spuEntityPageInfo.getTotal()+"",spuDTOList);
    }
}
