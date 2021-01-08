# mingrui-shop-parent
商品项目
# 环境搭建

## 系统架构

后台管理系统+前台门户系统

分布式结构+微服务

## 项目结构

- mingrui-shop-parent
  - mingrui-shop-basics
    - mingrui-shop-basic-eurka-server
    - mingrui-shop-basic-upload-server
    - mingrui-shop-basic-zuul-server
  - mingrui-shop-commons
    - mingrui-shop-commom-core
  - mingrui-shop-services
    - mingrui-shop-service-xxx
  - mingrui-shop-services-api
    - mingrui-shop-service-api-xxx

## 项目搭建

### 技术选型

#### 前端:

html/css/JavaScript

jQuery

#### 后端:

Mybatis/Spring/springMVC

spring Boot

spring Cloud

#### 开发环境

jdk1.8

#### 域名

一级域名:www.mrshop.com

二级域名:manage.mrshop.com;api.mrshop.com

## 创建项目

### 创建父工程 

mingrui-shop-parent,删除src文件夹,配置pom文件引入依赖

```xml
<properties> <!--项目构建编码-->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF- 8</project.reporting.outputEncoding> <!--声明JDK版本-->
    <java.version>1.8</java.version> <!--spring cloud 版本.注意此版本是建立在boot2.2.2版本上的-->
    <mr.spring.cloud.version>Hoxton.SR1</mr.spring.cloud.version>
</properties>

<!--boot 版本-->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.1.RELEASE</version> <!--始终从仓库中获取，不从本地路径获取-->
    <relativePath/>
</parent>
<dependencies>
    <!-- 集成commons工具类 -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
    </dependency>
    <!-- 集成lombok 框架 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency> 
    <!--junit测试-->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
    </dependency> 
    <!-- SpringBoot整合eureka客户端 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency> 
    <!--boot 测试模块-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

<!-- 项目依赖,子级模块可以继承依赖-->
<dependencyManagement>
    <dependencies> <!--cloud 依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${mr.spring.cloud.version}</version>
            <type>pom</type> <!--解决maven单继承的问题-->
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- 注意： 这里必须要添加， 否者各种依赖有问题 -->
<repositories>
    <repository>
        <id>spring-milestones</id>
        <name>Spring Milestones</name>
        <url>https://repo.spring.io/libs-milestone</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

### 创建基础服务父工程 

mingrui-shop-basics,删除src文件,将pom文件里的打包方式设置为pom,暂时不引入依赖

### 创建公共工程

mingrui-shop-commoms,删除src文件夹,将pom文件里的打包方式设置为pom,暂时不引入依赖

### 创建服务实现工程

mingrui-shop-service,删除src文件夹,配置pom文件引入依赖

```xml
<!-- SpringBoot-整合Web组件 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency> 
<!-- springcloud feign组件 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

### 创建服务接口工程

mingrui-shop-service-api,删除src文件夹,配置pom文件引入依赖

```xml
<!-- SpringBoot-整合Web组件 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 创建eureka服务

在mingrui-shop-basics下创建mingrui-shop-basic-eureka-server,配置pom文件引入依赖

```xml
<!--eureka 服务依赖-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
```

配置文件application.yml

```yml
server:
  port: 8761
spring:
  application:
    name: eureka-server
eureka:
  client:
    # eureka服务url,值为map集合默认key为defaultZone
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka
    # 当前服务是否同时注册
    register-with-eureka: false
    # 去注册中心获取其他服务的地址
    fetch-registry: false
  instance:
    hostname: localhost
    # 定义服务续约任务（心跳）的调用间隔，单位：秒 默认30
    lease-renewal-interval-in-seconds: 1
    # 定义服务失效的时间，单位：秒 默认90
    lease-expiration-duration-in-seconds: 2
  server:
    # 测试时关闭自我保护机制，保证不可用服务及时踢出
    enable-self-preservation: false
```

启动类RunEurekaServerApplication

```java
@SpringBootApplication
@EnableEurekaServer
public class RunEurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunEurekaServerApplication.class);
    }
}
```

# 商品分类增删改查

## 查询

类别管理的查询出来是有前台进行处理的不需要后台分页

前台树状图默认传parentId为0是从父类查询 也就是查询全部

用list接收数据,用this.setResultSuccess(list);传给前台

```java
@Override//查询
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setParentId(pid);
        List<CategoryEntity>list = categoryMapper.select(categoryEntity);
        return this.setResultSuccess(list);//调用返回成功方法,传list
    }
```

## 新增

Category查询出来是树状图的,有父子叶结构,要在某个节点下添加一个子节点,那么改节点必须是父节点状态( Is_Parent字段控制着节点状态,1为父节点状态,0为子节点状态也就是在它下面没有叶节点 )

所以第一步就是把改数据的IsParent设置为1

前台点击新增会传来一个 name="新的节点"	parentId为点击创建节点id 的一个数据

new一个新的对象,把id设置为parentId,isParent设置为1,进行父类状态的修改

然后新增数据传来的数据

```java
    @Transactional//事务
    @Override//新增
    public Result<Object> saveCategory(CategoryEntity categoryEntity) {
//        if (categoryEntity.getIsParent() != 1){
//            CategoryEntity cate = new CategoryEntity();
//            cate.setIsParent(1);
//            categoryMapper.updateByPrimaryKeySelective(cate);
//        }

        CategoryEntity cate = new CategoryEntity();
        cate.setId(categoryEntity.getParentId());
        cate.setIsParent(1);
        categoryMapper.updateByPrimaryKeySelective(cate);

        categoryMapper.insertSelective(categoryEntity);
        return this.setResultSuccess();//调用返回成功方法,不用传参数
    }
```

## 修改

Category的修改没有涉及到过多的其他因素,只需要修改本身就可以

```java
    @Transactional
    @Override//修改
    public Result<Object> editCategory(CategoryEntity categoryEntity) {
        categoryMapper.updateByPrimaryKeySelective(categoryEntity);
        return this.setResultSuccess();//调用返回成功方法,不用传参数
    }
```

## 删除

Category的删除

1.判断接收id是否合法,id不能为0,因为Category表id没有0,更不能小于0,id不能为null,调用返回错误信息方法

2.考虑到一个问题,如果你现在删除的是某个父节点的最后一个子节点,那么这个子节点的父节点的状态应该调整为子节点状态也就是IsParent要修改为0

所以要根据接收的id查询一条完整的数据!!

3.查询出一条数据用对象接收,判断改对象是否存在,如果不存在调用返回错误信息方法

4.再判断对象的IsParent是否为1,为1是父节点,不能被直接删除,所以调用返回错误信息方法

5.我们要查询出来跟他同级节点的数据,利用ParentId,用list集合接收

6.判断

如果list的长度<=1 满足条件就可以修改父类的状态了,因为长度=1的时候,最后这条数据就是要删除的自己

new一个对象,把IsParent设置为0,id设置为上面对象的ParentId,进行修改操作

7.删除传来的id的数据

```java
    public Result<Object> deleteById(Integer id) {
        //判断前台传来的id是否合法
            if (null == id || id<= 0) return this.setResultError("id不合法");
        //根据id查询一条数据
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);
        //判断查询一条数据是否存在
        if (null == categoryEntity) return  this.setResultError("数据不存在");
        //根据查询的数据的 判断isid是否为1 1为父节点不能删除
        if (categoryEntity.getIsParent() == 1) return this.setResultError("当前节点为父节点");

        Example example = new Example(categoryEntity.getClass());
        example.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        List<CategoryEntity>categoryEntityList = categoryMapper.selectByExample(example);

        if (categoryEntityList.size() <= 1){
            CategoryEntity UpdateCategory = new CategoryEntity();
            UpdateCategory.setIsParent(0);
            UpdateCategory.setId(categoryEntity.getParentId());
            categoryMapper.updateByPrimaryKeySelective(UpdateCategory);
        }

        categoryMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }
```

# 品牌增删改查

### 查询

#### 前台

##### 在item中新建Mrbrand.vue

```vue
<template>
  <v-card>
    <v-card-title>
      <v-btn color="info">新增</v-btn>
      <!-- 调按钮和输入框之间的间距 -->
      <v-spacer />
      <!--
            append-icon : 图标
            label : input默认值
        -->
      <v-text-field
        append-icon="search"
        label="品牌名称"
        @keyup.enter="getTableData()"
        v-model="search"
      ></v-text-field>
    </v-card-title>
    <!-- 表格组件 -->
        <!-- :pagination.sync="pagination" 绑定分页属性,并将分页参数绑定给data.pagination
        -->
        <!-- :total-items="total" 设置总条数,获取data.total中的值 -->
    <v-data-table
      :headers="headers"
      :items="desserts"
      :pagination.sync="pagination"
      :total-items="total"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
        <td class="text-xs-center">
          <!-- src 是html标签的属性 :src="vue的属性" -->
          <img :src="props.item.image" />
        </td>
        <td class="text-xs-center">{{ props.item.letter }}</td>
      </template>
    </v-data-table>
  </v-card>
</template>
<script>
export default {
  name: "MrBrand",//组件的名称
  data() {
    return {
      pagination: {},//分页参数信息
      total: 0,//总条数,初始值为0
      search: "",
      headers: [
        {
          text: "id",
          align: "center",
          value: "id",
        },
        {
          text: "品牌名称",
          align: "center",
          value: "name",
        },
        {
          text: "品牌logo",
          align: "center",
          value: "image",
        },
        {
          text: "首字母",
          align: "center",
          value: "letter",
        },
      ],
      desserts: [],//数据
    };
  },
    //组件加载完毕后执行的方法
  mounted() {
    this.getTableData();//查询方法,刷新列表
  },
  methods: {
    getTableData() {
      this.$http
        .get("/brand/list", {
          //查询传递的参数
          params: {
            page: this.pagination.page,
            rows: this.pagination.rowsPerPage,
            sort: this.pagination.sortBy,
            order: this.pagination.descending,
            name: this.search
          },
        })
        .then((resp) => {
          //给数据属性赋值
          this.desserts = resp.data.data.list;
          //给总条数赋值
          this.total = resp.data.data.total;
        })
        .catch((error) => console.log(error));
    }
  },
  watch: {//监控属性
    pagination() {//监控分页属性的变化
      this.getTableData();//刷新列表
    }
  }
};
</script>
```

##### 修改改index.js 27行

```js
route("/item/brand",'/item/MrBrand',"MrBrand"),
```

#### 后台

##### 在common-core 中的pom.xml添加依赖

```xml
	<!--帮助开发人员快速生成API文档-->
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>2.9.2</version>
    </dependency>
```

#####  在base包下新建BaseDTO

```java
package com.baidu.shop.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName BaseBTO
 * @Description: TODO
 * @Author 
 * @Date 2020/12/25
 * @Version V1.0
 **/
@Data
@ApiModel(value = "用于传输数据.需要其他bto继承此类")
public class BaseDTO {
    @ApiModelProperty(value="当前页",example = "1")
    private Integer page;
    @ApiModelProperty(value="每页显示条数",example = "5")
    private Integer rows;
    @ApiModelProperty(value="排序字段")
    private String sort;
    @ApiModelProperty(value="是否升序")
    private String order;
    public String  getOrderBy(){
        return sort+" "+(Boolean.valueOf(order)?"desc":"asc");//将排序转为boolean类型   true为倒序 false为正序
    }
}

```

##### 在mingrui-shop-server-api的pom.xml添加分页工具依赖

```xml
<!--分页工具-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.2.10</version>
        </dependency>
```

##### 在shop包下新建dto包新建BrandDTO类 用来继承baseDTO

```java
package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import sun.plugin2.message.Message;

import javax.validation.constraints.NotNull;

/**
 * @ClassName BrandDTO
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/25
 * @Version V1.0
 **/
@Data
@ApiModel(value="品牌DTO")
public class BrandDTO extends BaseDTO {
    @ApiModelProperty(value="品牌主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer id;
    @ApiModelProperty(value = "品牌名字")
    @NotNull(message = "品牌名字不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String name;
    @ApiModelProperty(value="品牌图片")
    private String image;
    @ApiModelProperty(value="品牌首字母")
    private Character letter;
}

```

##### 在shop-entity包下新建实体类 BrandEntity

```java
package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName BrandEntity
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/25
 * @Version V1.0
 **/
@Data
@Table(name = "tb_brand")
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String image;

    private Character letter;
}

```

##### 在shop-service接口中新建接口BrandService

```java
package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(tags="品牌接口 ")
public interface BrandService {
    @GetMapping(value="brand/list")
    @ApiOperation(value = "查询品牌列表")
    Result<PageInfo<BrandEntity>>getBrandInfo(BrandDTO brandDTO);

}
```

##### 在mingrui-shop-server-xxx下的shop-mapper添加BrandMapper接口

```java
package com.baidu.shop.mapper;

import com.baidu.shop.entity.BrandEntity;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<BrandEntity> {
}
```

##### 在shop-service.impl包下新建实现类BrandServiceImpl

```java
package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.baidu.shop.utils.PinyinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

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

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO) {
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());//分页
		//排序字不为空的时候进行排序
        if(!StringUtils.isEmpty(brandDTO.getSort())) PageHelper.orderBy(brandDTO.getOrderBy());

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO,BrandEntity.class);

        Example example = new Example(BrandEntity.class);
        //品牌名字不为空的时候进行条件查询  
        if (ObjectUtil.isNotNull(brandEntity.getName())){
            //模糊匹配
            example.createCriteria().andLike("name","%"+brandEntity.getName()+"%");
        }

		//查询结果返回到一个List集合中
        List<BrandEntity>brandEntities=brandMapper.selectByExample(example);

        PageInfo<BrandEntity>pageInfo = new PageInfo<>(brandEntities);

        return this.setResultSuccess(pageInfo);
    }
}
```



### 新增

#### 前台

##### 新建MrBrandForm.vue

```vue
<template>
  <div>
    <v-card-text>
      <v-form v-model="valid" ref="form">
        <v-text-field
          v-model="brand.name"
          label="品牌名称"
          :rules="nameRules"
          required
        ></v-text-field>
		//查询分类信息
        <v-cascader
          url="/category/list"
          required
          v-model="brand.categories"
          multiple
          label="商品分类"
        />

        <v-layout row>
          <v-flex xs3>
            <span style="font-size: 16px; color: #444">品牌LOGO：</span>
          </v-flex>
          <v-flex>
            <v-upload
              v-model="brand.image"
              url="/upload"
              :multiple="false"
              :pic-width="250"
              :pic-height="90"
            />
          </v-flex>
        </v-layout>
      </v-form>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn small @click="cancel()">取消</v-btn>
      <v-btn small color="primary" @click="submitForm()">确认</v-btn>
    </v-card-actions>
  </div>
</template>
<script>
export default {
  name: "MrBrandForm",
  props: {
    dialog: Boolean,//模态框状态设置为boolean类型
  },
  watch: {
    dialog(val) {
      if (val) this.$refs.form.reset();
    },
  },
  data() {
    //在js中 null == false , '' == false , undefined == false , 0 == false
    return {
      valid: true,
      nameRules: [
        (v) => !!v || "品牌名称不能为空",
        (v) => (v && v.length <= 10) || "品牌名称最多10个长度",
      ],
      brand: {
        name: "",
        image:'',
        categories: [],
      },
    };
  },
  methods: {
    cancel() {
      this.$emit("closeDialog");
    },
    submitForm() {
      if (!this.$refs.form.validate()) {
        return;
      }
      let formData = this.brand;
      let categoryIdArr = this.brand.categories.map((category) => category.id);
      formData.categories = categoryIdArr.join();

      this.$http
        .post("/brand/save", formData)
        .then((resp) => {
          if (resp.data.code != 200) {
            return;
          }
          //关闭模态框
          this.cancel();
          //刷新表单
        })
        .catch((error) => console.log(error));
    },
  },
};
</script>
```

##### 在MrBrand中添加新增属性

```vue
<template>
  <v-card>
    <v-card-title>
      <v-btn color="info" @click="dialog = true">新增</v-btn>//点击新增按钮打开模态框

      <div class="text-xs-center">
    <v-dialog
      v-model="dialog"
      width="500"
    >
      <v-card>
        <v-card-title
          class="headline grey lighten-2"
          primary-title
        >
          品牌新增
        </v-card-title>

        <mr-brand-form @closeDialog="dialog = false" :dialog="dialog"/>

      </v-card>
    </v-dialog>
  </div>

      <!-- 调按钮和输入框之间的间距 -->
      <v-spacer />

      <!--
            append-icon : 图标
            label : input默认值
        -->
      <v-text-field
        append-icon="search"
        label="品牌名称"
        @keyup.enter="getTableData()"
        v-model="search"
      ></v-text-field>
    </v-card-title>
    <!-- 表格组件 -->
    <v-data-table
      :headers="headers"
      :items="desserts"
      :pagination.sync="pagination"
      :total-items="total"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
        <td class="text-xs-center">
          <!-- src 是html标签的属性 :src="vue的属性" -->
          <img :src="props.item.image" />
        </td>
        <td class="text-xs-center">{{ props.item.letter }}</td>
      </template>
    </v-data-table>
  </v-card>
</template>
<script>
import MrBrandForm from './MrBrandForm';
export default {
  name: "MrBrand",
  components:{
    MrBrandForm
  },
  data() {
    return {
      pagination: {},
      dialog: false,
      total: 0,
      search: "",
      headers: [
        {
          text: "id",
          align: "center",
          value: "id",
        },
        {
          text: "品牌名称",
          align: "center",
          value: "name",
        },
        {
          text: "品牌logo",
          align: "center",
          value: "image",
        },
        {
          text: "首字母",
          align: "center",
          value: "letter",
        },
      ],
      desserts: [],
    };
  },
  mounted() {
    this.getTableData();
  },
  methods: {
    getTableData() {

      this.$http
        .get("/brand/list", {
          params: {
            page: this.pagination.page,
            rows: this.pagination.rowsPerPage,
            sort: this.pagination.sortBy,
            order: this.pagination.descending,
            name: this.search
          },
        })
        .then((resp) => {
          this.desserts = resp.data.data.list;
          this.total = resp.data.data.total;
        })
        .catch((error) => console.log(error));
    }
  },
  watch: {
    pagination() {
      this.getTableData();
    }
  }
};
</script>
```

#### 后台

##### 在common-core中的utils包下新建工具类BaiduBeanUtil

```java
package com.baidu.shop.utils;

import org.springframework.beans.BeanUtils;

/**
 * @ClassName BaiduBeanUtil
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/25
 * @Version V1.0
 **/
public class BaiduBeanUtil<T> {
    public static <T>T copyProperties(Object source,Class<T>clazz){
        try {
            T t= clazz.newInstance();
            BeanUtils.copyProperties(source,t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    return null;
    }
}
```

##### 在BrandDTO中添加属性

```java
@NotNull(message = "品牌分类不能为空",groups = {MingruiOperation.Add.class})
    @ApiModelProperty(value="品牌分类信息")
    private String categories;
```

#####  在entity下新建中间表的实体类CategoryBrandEntity

```java
package com.baidu.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * @ClassName CategoryBrandEntity
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/28
 * @Version V1.0
 **/
@Table(name="tb_category_brand")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBrandEntity {

    private Integer categoryId;

    private Integer brandId;
}

```

##### 在BrandService接口中添加新增方法

```java
	@PostMapping(value="brand/save")
    @ApiOperation(value="新增品牌")
    Result<JSONObject>saveBrand(@RequestBody BrandDTO brandDTO);
```

##### 在 mingrui-shop-service-xxx下的mapper中新建中间表的mapper  CategoryBrandMapper

```java
package com.baidu.shop.mapper;

import com.baidu.shop.entity.CategoryBrandEntity;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface CategoryBrandMapper extends InsertListMapper<CategoryBrandEntity>, Mapper<CategoryBrandEntity> {
}
```

#####  品牌首字母自动识别问题

###### 在common-core中的pom.xml文件添加依赖

```xml
<dependency> 
	<groupId>com.belerweb</groupId> 
		<artifactId>pinyin4j</artifactId>
	<version>2.5.1</version> 
</dependency>
```

###### 在utils包下新建PinYinUtil工具类

```java
package com.baidu.shop.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName PinyinUtil
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/28
 * @Version V1.0
 **/
public class PinyinUtil {
    public static final Boolean TO_FUUL_PINYIN = true;

    public static final Boolean TO_FIRST_CHAR_PINYIN = false;
    /**
     * 获取汉字首字母或全拼大写字母
     *
     * @param chinese 汉字
     * @param isFull  是否全拼 true:表示全拼    false表示：首字母
     * @return 全拼或者首字母大写字符窜
     */
    public static String getUpperCase(String chinese, boolean isFull) {
        return convertHanzi2Pinyin(chinese, isFull).toUpperCase();
    }

    /**
     * 获取汉字首字母或全拼小写字母
     *
     * @param chinese 汉字
     * @param isFull  是否全拼 true:表示全拼 false表示：首字母
     * @return 全拼或者首字母小写字符窜
     */
    public static String getLowerCase(String chinese, boolean isFull) {
        return convertHanzi2Pinyin(chinese, isFull).toLowerCase();
    }

    /**
     * 将汉字转成拼音
     * <p>
     * 取首字母或全拼
     *
     * @param hanzi  汉字字符串
     * @param isFull 是否全拼 true:表示全拼 false表示：首字母
     * @return 拼音
     */
    private static String convertHanzi2Pinyin(String hanzi, boolean isFull) {
        /***
         * ^[\u2E80-\u9FFF]+$ 匹配所有东亚区的语言
         * ^[\u4E00-\u9FFF]+$ 匹配简体和繁体
         * ^[\u4E00-\u9FA5]+$ 匹配简体
         */
        String regExp = "^[\u4E00-\u9FFF]+$";
        StringBuffer sb = new StringBuffer();
        if (hanzi == null || "".equals(hanzi.trim())) {
            return "";
        }
        String pinyin = "";
        for (int i = 0; i < hanzi.length(); i++) {
            char unit = hanzi.charAt(i);
            //是汉字，则转拼音
            if (match(String.valueOf(unit), regExp)) {
                pinyin = convertSingleHanzi2Pinyin(unit);
                if (isFull) {
                    sb.append(pinyin);
                } else {
                    sb.append(pinyin.charAt(0));
                }
            } else {
                sb.append(unit);
            }
        }
        return sb.toString();
    }

    /**
     * 将单个汉字转成拼音
     *
     * @param hanzi 汉字字符
     * @return 拼音
     */
    private static String convertSingleHanzi2Pinyin(char hanzi) {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String[] res;
        StringBuffer sb = new StringBuffer();
        try {
            res = PinyinHelper.toHanyuPinyinStringArray(hanzi, outputFormat);
            sb.append(res[0]);//对于多音字，只用第一个拼音
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }

    /***
     * 匹配
     * <P>
     * 根据字符和正则表达式进行匹配
     *
     * @param str 源字符串
     * @param regex 正则表达式
     *
     * @return true：匹配成功  false：匹配失败
     */
    private static boolean match(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static void main(String[] args) {
        String a =  "abcd";
        System.out.println(PinyinUtil.getUpperCase(a, false));
    }
}

```

##### 在BrandServiceImpl中重新新增方法

```java
	@Transactional
    @Override
    public Result<JSONObject> saveBrandInfo(BrandDTO brandDTO) {

        BrandeEntity brandeEntity = BaiduBeanUtils.copyProperties(brandDTO, BrandeEntity.class);
        brandeEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandeEntity.getName().toCharArray()[0]), false).toCharArray()[0]);
        brandMapper.insertSelective(brandeEntity);
		
		//调用封装的方法
        this.insertCategoryBrandList(brandDTO.getCategories(),brandeEntity.getId());

        return this.setResultSuccess();
    }
```

###### 封装批量新增方法

```Java
private void insertCategoryBrandList(String categories,Integer brandId){
        if (StringUtils.isEmpty(categories)) throw  new RuntimeException("商品分类信息不能为空");

        if (categories.contains(",")) {
            categoryBrandMapper.insertList(Arrays.asList(categories.split(","))
                            .stream()
                            .map(categoryIdStr -> new CategoryBrandEntity(Integer.valueOf(categoryIdStr), brandId))
                            .collect(Collectors.toList())
            );
        } else {
            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setBrandId(brandId);
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));
            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }
    }
```

#### 图片上传

##### 在mingrui-shop-basics下新建mingrui-shop-basic-upload-server并在pom.xml文件中添加依赖

```xml
	<dependencies>
        <!-- SpringBoot-整合Web组件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baidu</groupId>
            <artifactId>mingrui-shop-common-core</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

##### 新建com.baidu.shop.upload.controller包结构 并在该包下新建类UploadController

```java
package com.baidu.shop.upload.controller;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.status.HTTPStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName UploadController
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/29
 * @Version V1.0
 **/
@RestController
@RequestMapping(value = "upload")
public class UploadController extends BaseApiService {

    //window系统的上传目录
    //@Value(value = "${mingrui.upload.path.windows}")
    private String windowsPath;

    //linux系统的上传目录
    //@Value(value = "${mingrui.upload.path.linux}")
    private String linuxPath;

    //图片服务器的地址
    //@Value(value = "${mingrui.upload.img.host}")
    private String imgHost;

    //@PostMapping
    public Result<String> uploadImg(@RequestParam MultipartFile file) {

        if (file.isEmpty()) return this.setResultError("上传的文件为空");//判断上传的文件是否为空

        String filename = file.getOriginalFilename();//获取文件名

        String os = System.getProperty("os.name").toLowerCase();

        String  path = os.indexOf("win") != -1 ? windowsPath : os.indexOf("lin") != -1 ? linuxPath : "";

//        if (os.indexOf("win") != -1) {
//            path = windowsPath;
//        } else if (os.indexOf("lin") != -1) {
//            path = linuxPath;
//        }


        filename = UUID.randomUUID() + filename;//防止文件名重复

        //创建文件 路径+分隔符(linux和window的目录分隔符不一样)+文件名
        File dest = new File(path + File.separator + filename);

        //判断文件夹是否存在,不存在的话就创建
        if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();

        try {
            file.transferTo(dest);//上传
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this.setResult(HTTPStatus.OK, "upload success!!!", imgHost + "/" + filename);//将文件名返回页面用于页面回显
    }

}

```

##### 新建包结构com.baidu.gload并在该包下新建类 GlobalCorsConfig

```java
package com.baidu.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


/**
 * @ClassName GlobalCorsConfig
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/29
 * @Version V1.0
 **/
@Configuration
public class GlobalCorsConfig {

@Bean
public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true); // 允许cookies跨域
    config.addAllowedOrigin("*");// 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080 ,以降低安全风险。。
    config.addAllowedHeader("*");// 允许访问的头信息,*表示全部
    config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
    config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");// 允许Get的请求方法
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PATCH");
    source.registerCorsConfiguration("/**", config);
    //3.返回新的CorsFilter.
    return new CorsFilter(source);
    }
}


```



### 修改

#### 	前台

##### 修改MrBrand.vue页面

```vue
<template>
  <v-card>
    <v-card-title>
      <v-btn color="info" @click="addData()">新增</v-btn>

      <div class="text-xs-center">
        <v-dialog v-model="dialog" width="500">
          <v-card>
            <v-card-title class="headline grey lighten-2" primary-title>
              品牌{{ isEdit?'修改':'新增' }}//判断isEntity是否为true 为true执行修改 反之新增
            </v-card-title>
            <!--将模态框的状态传递到子组件
            @closeModel="closeModel" 关闭模态框的函数中需要重置this.brandDetail,所以
            需要新建closeModel方法
            :isEdit="isEdit" 将新增还是修改的状态传递给子级组件
            :brandDetail="brandDetail" 修改回显的数据
            -->
            <mr-brand-form @closeDialog="closeDialog" :dialog="dialog" :isEdit="isEdit" :brandDetail="brandDetail" />
          </v-card>
        </v-dialog>
      </div>

      <!-- 调按钮和输入框之间的间距 -->
      <v-spacer />

      <!--
            append-icon : 图标
            label : input默认值
        -->
      <v-text-field
        append-icon="search"
        label="品牌名称"
        @keyup.enter="getTableData()"
        v-model="search"
      ></v-text-field>
    </v-card-title>
    <!-- 表格组件 -->
    <v-data-table
      :headers="headers"
      :items="desserts"
      :pagination.sync="pagination"
      :total-items="total"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
        <td class="text-xs-center">
          <!-- src 是html标签的属性 :src="vue的属性" -->
          <img width="100" :src="props.item.image" />
        </td>
        <td class="text-xs-center">{{ props.item.letter }}</td>
        <td class="text-xs-center">
          <v-btn flat icon color="yellow" @click="editData(props.item)">
            <v-icon>edit</v-icon>
          </v-btn>
        </td>
      </template>
    </v-data-table>
  </v-card>
</template>
<script>
import MrBrandForm from "./MrBrandForm";
export default {
  name: "MrBrand",
  components: {
    MrBrandForm,
  },
  data() {
    return {
      brandDetail:{},
      isEdit:false,
      pagination: {},
      dialog: false,
      total: 0,
      search: "",
      headers: [
        {
          text: "id",
          align: "center",
          value: "id",
        },
        {
          text: "品牌名称",
          align: "center",
          value: "name",
        },
        {
          text: "品牌logo",
          align: "center",
          value: "image",
        },
        {
          text: "首字母",
          align: "center",
          value: "letter",
        },
        {
          text: "操作",
          align: "center",
          sortable: false,
          value: "id",
        },
      ],
      desserts: [],
    };
  },
  mounted() {
    this.getTableData();
  },
  methods: {
    closeDialog () {
      this.dialog = false;
      this.getTableData();
    },
    addData () {
      //this.brandDetail = {};
      this.isEdit = false;
      this.dialog = true;
    },
    editData (obj) {
      this.brandDetail = obj;
      this.isEdit = true;
      this.dialog = true;
    },
    getTableData() {
      this.$http
        .get("/brand/list", {
          params: {
            page: this.pagination.page,
            rows: this.pagination.rowsPerPage,
            sort: this.pagination.sortBy,
            order: this.pagination.descending,
            name: this.search,
          },
        })
        .then((resp) => {
          this.desserts = resp.data.data.list;
          this.total = resp.data.data.total;
        })
        .catch((error) => console.log(error));
    },
  },
  watch: {
    pagination() {
      this.getTableData();
    },
  },
};
</script>
```



#### 	后台

##### 需要在分类的CategroyService接口中添加查询方法

```java
	@ApiOperation(value = "通过品牌id查询商品分类")
    @GetMapping(value = "category/brand")
    Result<List<CategoryEntity>> getByBrand(Integer brandId);
```

##### CategoryServiceImpl实现类实现接口中的方法

```java
	@Override
    public Result<List<CategoryEntity>> getByBrand(Integer brandId) {
        List<CategoryEntity> categoryByBrandId = categoryMapper.getCategoryByBrandId(brandId);
        return this.setResultSuccess(categoryByBrandId);
    }
```

##### 在BrandService接口中添加修改方法

```java
	@PutMapping(value="brand/save")
    @ApiOperation(value="修改品牌")
    Result<JSONObject>editBrand(@RequestBody BrandDTO brandDTO);
```

##### 实现类实现BrandService当中新添加的修改方法

```Java
	//修改
    @Transactional
    @Override
    public Result<JSONObject> editBrand(BrandDTO brandDTO) {
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        brandMapper.updateByPrimaryKeySelective(brandEntity);
        //通过brandId删除中间表
        this.deleteCategoryBrandByBrandId(brandDTO.getId());
        //批量新增  新增
        this.saveOrUpdate(brandDTO.getCategories(),brandEntity.getId());
        return this.setResultSuccess();
    }
```



### 删除

#### 		前台

##### 						1. 给删除按钮绑定删除事件

```vue
			<v-btn flat icon color="red" @click="deleteData(props.item.id)">
              <v-icon>delete</v-icon>
            </v-btn>
```

##### 						2.写删除方法  发送请求	刷新页面

```js
	deleteData(id){ 
         this.$message.confirm('此操作将永久删除该品牌, 是否继续?').then(() => {
          // 发起删除请求
          this.$http.delete("/brand/delete?id=" + id).then(resp => {
              // 删除成功，重新加载数据
              if(resp.data.code != 200){
                this.$message.error("删除错误"+resp.data.message)
                return;
              }
              this.$message.success("删除成功！");
              this.getTableData();
            }).catch(error => console.log(error))
        }).catch(() => {this.$message.info("删除已取消！");
      });
    },
```

#### 		后台

##### 					1.后台添加接口  

```java
	@ApiOperation(value = "删除品牌")
    @DeleteMapping(value = "/brand/delete")
    Result<JSONObject>  deleteBrandInfo(Integer id);
```

##### 					2.实现类实现接口  添加删除方法

```java
	@Transactional
    @Override
    public Result<JSONObject> deleteBrandInfo(Integer id) {
        //删除品牌信息
        brandMapper.deleteByPrimaryKey(id);
        //删除根据品牌Id查询出来的分类信息
       	Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",id);
        categoryBrandMapper.deleteByExample(example);

        return this.setResultSuccess();
    }
```

# 商品规格管理

### 规格组查询

#### 	1.加载分类信息

​	  在index.js页面29行进行修改

```js
route("/item/specification",'/item/specification/Specification',"Specification"),
```

​	在specification下的Specification.vue进行修改   把url设置成我们自己的

```vue
<v-tree url="/category/list" :isEdit="false" @handleClick="handleClick"/>
```

#### 	2.规格组查询

##### 	2.1vue项目

​	在SpecGroup.vue查询方法进行修改  把请求改为我们自己的

```js
	loadData(){
          this.$http.get("/specgroup/getSpecGroupInfo",{
              params:{
                  cid:this.cid
              }
          })
          .then(({data}) => {
              this.groups = data.data;
          })
          .catch(() => {
              this.groups = [];
          })
      },
```

##### 2.2.后台

###### 	在mingrui-shop-service-api-xxxx下的entity包下新建SpecGroupEntity

```java
package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName SpecGroupEntity
 * @Description: TODO
 * @Author yyq
 * @Date 2021/1/4
 * @Version V1.0
 **/
@Table(name = "tb_spec_group")
@Data
public class SpecGroupEntity {
    @Id
    private Integer id;

    private Integer cid;

    private String name;
}

```

###### 在dto包下新建SpecGroupDTO

```java
package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName SpecGroupDTO
 * @Description: TODO
 * @Author yyq
 * @Date 2021/1/4
 * @Version V1.0
 **/
@ApiModel(value = "规格组数据输出DTO")
@Data
public class SpecGroupDTO extends BaseDTO {

    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "分类Id",example = "1")
    @NotNull(message = "分类Id不能为空",groups = {MingruiOperation.Update.class})
    private Integer cid;

    @ApiModelProperty(value = "规格组名称")
    @NotEmpty(message = "规格名称不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String name;
}

```

###### 在service包下新建SpecificationService

```java
package com.baidu.shop.service;

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
 }
```

###### 在 mingrui-shop-service-xxx下的mapper包下新建SpecGroupMapper

```java
package com.baidu.shop.mapper;

import com.baidu.shop.entity.SpecGroupEntity;
import tk.mybatis.mapper.common.Mapper;

public interface SpecGroupMapper extends Mapper<SpecGroupEntity> {
}

```

###### 在service.impl下新建SpecificationServiceImpl

```java
package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.mapper.SpecGroupMapper;
import com.baidu.shop.service.SpecificationService;
import com.baidu.shop.utils.ObjectUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.util.List;


import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SpecificationServiceImpl
 * @Description: TODO
 * @Author yyq
 * @Date 2021/1/4
 * @Version V1.0
 **/
@RestController
public class SpecificationServiceImpl extends BaseApiService implements SpecificationService {
    @Resource
    private SpecGroupMapper specGroupMapper;

    @Override
    public Result<List<SpecGroupEntity>> getSpecGroupInfo(SpecGroupDTO specGroupDTO) {

        Example example = new Example(SpecGroupEntity.class);
        if(ObjectUtil.isNotNull(specGroupDTO.getCid())){ example.createCriteria().andEqualTo("cid",BaiduBeanUtils.copyProperties(specGroupDTO,SpecGroupEntity.class).getCid());
}
        List<SpecGroupEntity> specGroupEntities = specGroupMapper.selectByExample(example);
        return this.setResultSuccess(specGroupEntities);
    }
}

```

### 规格组增删改

#### 1.vue项目

##### 在SpecGroup.vue下修改新增，删除方法

```js
 save(){
           this.$http({
            method: this.isEdit ? 'put' : 'post',
            url: '/specgroup/save',
            data: this.group
          }).then(() => {
            // 关闭窗口
            this.show = false;
            this.$message.success("保存成功！");
            this.loadData();
          }).catch(() => {
              this.$message.error("保存失败！");
            });
      },
      deleteGroup(id){
          this.$message.confirm("确认要删除分组吗？")
          .then(() => {
            this.$http.delete("/specgroup/delete?id=" + id)
                .then(resp => {
                    if (resp.data.code != 200) {
                         this.$message.error(resp.data.message);
                         return;
                    }
                    this.$message.success("删除成功");
                    this.loadData();
                })
          })
      },
```

#### 2.后台

##### 在SpecificationService下新增接口

```java
	@ApiOperation("新增规则数据")
    @PostMapping("specgroup/save")
    Result<JSONObject> saveSpecGroupInfo(@RequestBody  SpecGroupDTO specGroupDTO);

    @ApiOperation("修改规则数据")
    @PutMapping("specgroup/save")
    Result<JSONObject> editSpecGroupInfo(@RequestBody  SpecGroupDTO specGroupDTO);

    @ApiOperation("删除规则数据")
    @DeleteMapping("specgroup/delete")
    Result<JSONObject> deleteSpecGroupInfo(Integer id);
```

##### 实现类实现SpecificationService接口

```java
	@Transactional
    @Override
    public Result<JSONObject> deleteSpecGroupInfo(Integer id) {
        //判断规格组当中如果有参数不能删除
        Example example = new Example(SpecParamEntity.class);
        example.createCriteria().andEqualTo("groupId",id);
        List<SpecParamEntity> specParamEntities = specParamMapper.selectByExample(example);
        if (specParamEntities.size() > 0) return  this.setResultError("已有参数不能删除");
        
        specGroupMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> editSpecGroupInfo(SpecGroupDTO specGroupDTO) {
        specGroupMapper.updateByPrimaryKeySelective(BaiduBeanUtils.copyProperties(specGroupDTO,SpecGroupEntity.class));
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> saveSpecGroupInfo(SpecGroupDTO specGroupDTO) {
        specGroupMapper.insertSelective(BaiduBeanUtils.copyProperties(specGroupDTO,SpecGroupEntity.class));
        return this.setResultSuccess();
    }
```

### 规格参数查询

#### 	1.vue项目

##### 	在SpecParam.vue下的125行进行修改

```js
	loadData() {
      this.$http
        .get("/specparam/getSpecParamInfo",{
          params:{
            groupId:this.group.id
          }
        })
        .then((resp) => {
          resp.data.data.forEach(p => {
              p.segments = p.segments ? p.segments.split(",").map(s => s.split("-")) : [];
          })
          this.params = resp.data.data;
        })
        .catch(() => {
          this.params = [];
        });
    },
```

#### 	2.后台

##### 	在mingrui-shop-service-api-xxxx下的entity包下新建SpecParamEntity

```java
package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName SpecParamEntity
 * @Description: TODO
 * @Author yyq
 * @Date 2021/1/4
 * @Version V1.0
 **/
@Table(name = "tb_spec_param")
@Data
public class SpecParamEntity {
           /* `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
            `cid` bigint(20) NOT NULL COMMENT '商品分类id',
            `group_id` bigint(20) NOT NULL,
             `name` varchar(255) NOT NULL COMMENT '参数名',
            `numeric` tinyint(1) NOT NULL COMMENT '是否是数字类型参数，true或false',
            `unit` varchar(255) DEFAULT '' COMMENT '数字类型参数的单位，非数字类型可以为空',
            `generic` tinyint(1) NOT NULL COMMENT '是否是sku通用属性，true或false',
            `searching` tinyint(1) NOT NULL COMMENT '是否用于搜索过滤，true或false',
            `segments` varchar(1000) DEFAULT '' COMMENT '数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0',*/
        @Id
        private Integer id;

        private Integer cid;

        private Integer groupId;

        private String name;

        @Column(name = "`numeric`")
        private Boolean numeric;

        private String unit;

        private Boolean generic;

        private Boolean searching;

        private String segments;
}

```

##### 在dto包下新建SpecParamDTO

```java
package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName SpecParamDTO
 * @Description: TODO
 * @Author yyq
 * @Date 2021/1/4
 * @Version V1.0
 **/
@ApiModel("规格参数数据传输DTO")
@Data
public class SpecParamDTO {
    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "分类Id",example = "1")
    @NotNull(message = "分类Id不能为空",groups = {MingruiOperation.Update.class})
    private Integer cid;

    @ApiModelProperty(value = "规格组Id",example = "1")
    @NotNull(message = "规格组Id不能为空",groups = {MingruiOperation.Update.class})
    private Integer groupId;

    @ApiModelProperty(value = "规格参数名称")
    @NotEmpty(message = "规格参数名称不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String name;

    @ApiModelProperty(value = "是否是数字类型参数")
    @NotNull(message = "是否是数字类型参数不能为空s",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Boolean numeric;

    @ApiModelProperty(value = "数字类型参数的单位")
    @NotEmpty(message = "数字类型参数的单位不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String unit;

    @ApiModelProperty(value = "是否是sku通用属性")
    @NotNull(message = "是否是sku通用属性不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Boolean generic;

    @ApiModelProperty(value = "是否用于搜索过滤")
    @NotNull(message = "是否用于搜索过滤s不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Boolean searching;

    @ApiModelProperty(value = "数值类型参数")
    @NotEmpty(message = "数值类型参数不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String segments;
}

```

##### 在SpecificationService中添加

```java
	@ApiOperation("通过条件查询规则参数")
    @GetMapping("specparam/getSpecParamInfo")
    Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO);
```

##### 在mingrui-shop-service-xxx下的mapper包下新建SpecParamMapper

```java
package com.baidu.shop.mapper;

import com.baidu.shop.entity.SpecParamEntity;
import tk.mybatis.mapper.common.Mapper;

public interface SpecParamMapper extends Mapper<SpecParamEntity> {
}

```

##### 在SpecificationServiceImpl中添加

```Java
	@Resource
    private SpecParamMapper specParamMapper;、
    
     @Override
    public Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO) {
        SpecParamEntity specParamEntity = BaiduBeanUtils.copyProperties(specParamDTO, SpecParamEntity.class);
        Example example = new Example(SpecParamEntity.class);
        example.createCriteria().andEqualTo("groupId",specParamEntity.getGroupId());
        List<SpecParamEntity> specParamEntities = specParamMapper.selectByExample(example);
        return this.setResultSuccess(specParamEntities);
    }
```

### 规格参数增删改

#### 1.vue项目

##### 在SpecGroup.vue下修改新增，删除方法

```js
 	deleteParam(id) {
        this.$message.confirm("确认要删除该参数吗？")
        .then(() => {
            this.$http.delete("/specparam/delete?id=" + id)
            .then(() => {
                this.$message.success("删除成功");
                this.loadData();
            })
            .catch(() => {
                this.$message.error("删除失败");
            })
        })
    },
    formatBoolean(boo) {
      return boo ? "是" : "否";
    },
    save(){
        const p = {};
        Object.assign(p, this.param);
        p.segments = p.segments.map(s => s.join("-")).join(",")
        this.$http({
            method: this.isEdit ? 'put' : 'post',
            url: '/specparam/saveSpecParamInfo',
            data: p,
        }).then(() => {
            // 关闭窗口
            this.show = false;
            this.$message.success("保存成功！");
            this.loadData();
          }).catch(() => {
              this.$message.error("保存失败！");
            });
    }
```

#### 2.后台

##### 在mingrui-shop-service-api-xxx下的SpecificationService中添加接口

```java
	@ApiOperation("新增规则参数")
    @PostMapping("specparam/saveSpecParamInfo")
    Result<JSONObject>  saveSpecParamInfo(@RequestBody SpecParamDTO specParamDTO);

    @ApiOperation("修改规则参数")
    @PutMapping("specparam/saveSpecParamInfo")
    Result<JSONObject>  editSpecParamInfo(@RequestBody SpecParamDTO specParamDTO);

    @ApiOperation("删除规则参数")
    @DeleteMapping("specparam/delete")
    Result<JSONObject>  deleteSpecParamInfo(Iteger id);
```

##### 在SpecificationServiceImpl中添加

```java
 	@Transactional
    @Override
    public Result<JSONObject> deleteSpecParamInfo(Integer id) {
        specParamMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> editSpecParamInfo(SpecParamDTO specParamDTO) {
        SpecParamEntity specParamEntity = BaiduBeanUtils.copyProperties(specParamDTO, SpecParamEntity.class);
        specParamMapper.updateByPrimaryKeySelective(specParamEntity);
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> saveSpecParamInfo(SpecParamDTO specParamDTO) {
        SpecParamEntity specParamEntity = BaiduBeanUtils.copyProperties(specParamDTO, SpecParamEntity.class);
        if (null == specParamEntity.getName()) return this.setResultError("参数名不能为空");
        specParamMapper.insertSelective(specParamEntity);
        return this.setResultSuccess();
    }
```

### 项目当中遇到的问题

#### 1.规格参数修改完成后无法新增   

##### 修改SpecParam.vue文件    在新增方法中国添加this.isEdit = false;

```js
    addParam() {
      this.param = {
          cid: this.group.cid,
          groupId: this.group.id,
          segments:[],
          numeric:false,
          searching:false,
          generic:false}
      this.show = true;
      this.isEdit = false;
    },
```

# 商品管理增删改查

## 查询

### 前台

#### 修改前台Goods.vue

line : 8

```html
		<v-btn-toggle mandatory v-model="search.saleable">
          <v-btn flat :value="2">
            全部
          </v-btn>
          <v-btn flat :value="1">
            上架
          </v-btn>
          <v-btn flat :value="0">
            下架
          </v-btn>
        </v-btn-toggle>
```

line :	205

```js
	getDataFromApi() {
        this.loading = true;
        this.$http.get('/goods/getSpuInfo',{
          params: {
            page: this.pagination.page,
            rows: this.pagination.rowsPerPage,
            sort: this.pagination.sortBy,
            order: this.pagination.descending,
            saleable: this.search.saleable,
            title: this.search.key
          },
        }).then(resp =>{
          this.items = resp.data.data;
          this.totalItems = resp.data.message-0;
          this.loading = false;
        }).catch(error => console.log(error))

        // setTimeout(() => {
        //   // 返回假数据
        //   this.items = goodsData.slice(0, 4);
        //   this.totalItems = 25;
        //   this.loading = false;
        // }, 300)
      }
```

### 后台

#### mingrui-shop-service-api-xxx

##### 在下的entity包下新建SpuEntity

```java
package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName SpuEntity
 * @Description: TODO
 * @Author yyq
 * @Date 2021/1/5
 * @Version V1.0
 **/
@Table(name = "tb_spu")
@Data
public class SpuEntity {
             /*CREATE TABLE `tb_spu` (
            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'spu id',
            `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
            `sub_title` varchar(255) DEFAULT '' COMMENT '子标题',
            `cid1` bigint(20) NOT NULL COMMENT '1级类目id',
            `cid2` bigint(20) NOT NULL COMMENT '2级类目id',
            `cid3` bigint(20) NOT NULL COMMENT '3级类目id',
            `brand_id` bigint(20) NOT NULL COMMENT '商品所属品牌id',
            `saleable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否上架，0下架，1上架',
            `valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效，0已删除，1有效',
            `create_time` datetime DEFAULT NULL COMMENT '添加时间',
            `last_update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
             PRIMARY KEY (`id`)
             ) ENGINE=InnoDB AUTO_INCREMENT=234 DEFAULT CHARSET=utf8 COMMENT='spu表，该表描述的是一个抽象性的商品，比如 iphone8';*/
    @Id
    private Integer id;

    private String title;

    private String subTitle;

    private Integer cid1;

    private Integer cid2;

    private Integer cid3;

    private Integer brandId;

    private Integer saleable;

    private Integer valid;

    private Date createTime;

    private Date lastUpdateTime;
}

```

##### 在dto包下新建SpuDTO

```java
package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName SupDTO
 * @Description: TODO
 * @Author yyq
 * @Date 2021/1/5
 * @Version V1.0
 **/
@ApiModel("spu数据传输DTO")
@Data
public class SpuDTO extends BaseDTO {

    @ApiModelProperty(value = "主键", example = "1")
    @NotNull(message = "主键不能为空", groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "标题")
    @NotEmpty(message = "标题不能为空", groups = {MingruiOperation.Add.class})
    private String title;

    @ApiModelProperty(value = "子标题")
    private String subTitle;

    @ApiModelProperty(value = "1级类目Id", example = "1")
    @NotNull(message = "1级类目Id不能为空", groups = {MingruiOperation.Add.class})
    private Integer cid1;

    @ApiModelProperty(value = "2级类目Id", example = "1")
    @NotNull(message = "2级类目Id不能为空", groups = {MingruiOperation.Add.class})
    private Integer cid2;

    @ApiModelProperty(value = "3级类目Id", example = "1")
    @NotNull(message = "3级类目Id不能为空", groups = {MingruiOperation.Add.class})
    private Integer cid3;

    @ApiModelProperty(value = "品牌Id", example = "1")
    @NotNull(message = "品牌Id不能为空", groups = {MingruiOperation.Add.class})
    private Integer brandId;

    @ApiModelProperty(value = "是否上架，0下架，1上架", example = "1")
    private Integer saleable;

    @ApiModelProperty(value = "是否有效，0已删除，1有效", example = "1")
    private Integer valid;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdateTime;
    
    private String categoryName;

    private String brandName;
}

```

##### 在service包下新建GoodsService

```java
package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpuEntity;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Api(tags = "商品接口")
public interface GoodsService {

    @ApiOperation(value = "获取商品信息")
    @GetMapping(value = "goods/getSpuInfo")
    Result<PageInfo<SpuDTO>>  getSpuInfo(SpuDTO spuDTO);
}

```

#### mingrui-shop-service-xxx

##### 在mapper包下新建SpuMapper

```java
package com.baidu.shop.mapper;

import com.baidu.shop.entity.SpuEntity;
import tk.mybatis.mapper.common.Mapper;

public interface SpuMapper extends Mapper<SpuEntity> {
}

```

##### 修改CategoryMapper

```java
package com.baidu.shop.mapper;

import com.baidu.shop.entity.CategoryEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface CategoryMapper extends Mapper<CategoryEntity>,SelectByIdListMapper<CategoryEntity,Integer> {

    @Select("select id,name from tb_category where id in (select category_id from tb_category_brand where brand_id=#{brandId} )")
    List<CategoryEntity> getCategoryByBrandId(Integer brandId);
}

```

##### 在service.impl包下新建GoodsServiceImpl

```java
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
            strings.add(0,"");
            categoryEntities.stream().forEach(categoryEntity -> {
                strings.add(0,strings.get(0) + categoryEntity.getName() + "/");
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

```

## 新增

### 前台

#### 修改GoodsForm.vue

line : 21

```html
			 <v-flex xs5>
                <!--商品分类-->
                <v-cascader
                  url="/category/list"
                  required
                  showAllLevels
                  v-model="goods.categories"
                  label="请选择商品分类"/>
              </v-flex>
```

line : 282

```js
			this.$http
            .get("/brand/getBrandByCategoryId",{
              params:{
                cid:this.goods.categories[2].id
              }
            })
            .then((resp) => {
              this.brandOptions = resp.data.data;
            });
```

line : 291

```js
 		// 根据分类查询规格参数
          this.$http
            .get("/specparam/getSpecParamInfo",{
              params:{
                cid:this.goods.categories[2].id
              }
            })
            .then((resp) => {
              let specs = [];
              let template = [];
              if (this.isEdit){
                specs = JSON.parse(this.goods.spuDetail.genericSpec);
                template = JSON.parse(this.goods.spuDetail.specialSpec);
              }
              // 对特有规格进行筛选
              const arr1 = [];
              const arr2 = [];
              resp.data.data.forEach(({id, name,generic, numeric, unit }) => {
                if(generic){
                  const o = { id, name, numeric, unit};
                  if(this.isEdit){
                    o.v = specs[id];
                  }
                  arr1.push(o)
                }else{
                  const o = {id, name, options:[]};
                  if(this.isEdit){
                    o.options = template[id];
                  }
                  arr2.push(o)
                }
              });
```

line : 57 

```vue
<v-editor v-model="goods.spuDetail.description" upload-url="/upload"/>
```

line ： 119

```vue
 <v-upload v-model="props.item.images" url="/upload"/>
```

line : 206

```js
 	images: images ? images.map(image =>{return image.data}).join(",") : '', // 图片
```

#### 修改form包下的Editor.vue

line : 93

```js
		this.$http.post(this.uploadUrl, data)
          .then(res => {
            if (res.data.data) {
              this.editor.insertEmbed(this.editor.getSelection().index, 'image', res.data.data)
            }
          })			
```



### 后台

#### 在BrandService接口中添加方法

```java
	@ApiOperation(value = "根据分类Id查询品牌信息")
    @GetMapping(value = "/brand/getBrandByCategoryId")
    Result<List<BrandeEntity>>  getBrandByCategoryId(Integer cid);
```

#### 修改mapp包下的BrandMapper

```java
package com.baidu.shop.mapper;

import com.baidu.shop.entity.BrandeEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName BrandMapper
 * @Description: TODO
 * @Author yyq
 * @Date 2020/12/25
 * @Version V1.0
 **/
public interface BrandMapper extends Mapper<BrandeEntity> {

    @Select(value = "SELECT b.* FROM tb_brand b WHERE b.id in (SELECT brand_id FROM tb_category_brand cb where cb.category_id = #{cid})")
    List<BrandeEntity> getBrandByCategoryId(Integer cid);
}

```

#### 实现类BrandServiceImpl实现接口中新增的方法

```java
	@Override
    public Result<List<BrandeEntity>> getBrandByCategoryId(Integer cid) {
       List<BrandeEntity> list =  brandMapper.getBrandByCategoryId(cid);
        return this.setResultSuccess(list);
    }
```

#### 修改SpecificationServiceImpl查询方法

```java
	@Override
    public Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO) {
        SpecParamEntity specParamEntity = BaiduBeanUtils.copyProperties(specParamDTO, SpecParamEntity.class);
        Example example = new Example(SpecParamEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if (ObjectUtil.isNotNull(specParamEntity.getGroupId()))
            criteria.andEqualTo("groupId",specParamEntity.getGroupId());
        if (ObjectUtil.isNotNull(specParamEntity.getCid()))
            criteria.andEqualTo("cid",specParamEntity.getCid());

        List<SpecParamEntity> specParamEntities = specParamMapper.selectByExample(example);
        return this.setResultSuccess(specParamEntities);
    }
```

## 修改

### 前台

#### 修改Goods.vue的修改方法 editItem（）  知道怎么写  思路整理不出来  拷贝就行了

```js
editItem(item) {
        //this.selectedGoods = item;
        // const names = item.categoryName.split("/");
        // this.selectedGoods.categories = [
        //   {id: item.cid1, name: names[0]},
        //   {id: item.cid2, name: names[1]},
        //   {id: item.cid3, name: names[2]}
        // ];

        //保证isEdit的状态为true
        this.isEdit = true;
        this.show = true;

        let obj = item;
        //根据spuId查询spuDetail信息
        this.$http.get("/goods/getSpuDetailByspuId",{
          params:{
            spuId:item.id
          }
        }).then(resp => {
            //console.log(resp)
            //重点   设置categories不为Undefined
            obj.categories = [];
            obj.spuDetail = resp.data.data;
            console.log(resp.data.data)
            // this.selectedGoods.spuDetail.specTemplate = JSON.parse(resp.data.data.specialSpec);//特殊规格参数
            // this.selectedGoods.spuDetail.specifications = JSON.parse(resp.data.data.genericSpec);//通用规格参数
            obj.spuDetail.specTemplate = JSON.parse(resp.data.data.specialSpec);//特殊规格参数
            obj.spuDetail.specifications = JSON.parse(resp.data.data.genericSpec);//通用规格参数
          })
          //通过spuId查询skus
          this.$http.get("/goods/getSkusByspuId",{
              params:{
                spuId : item.id
              }
          }).then(resp =>{
            //console.log(resp.data.data)
           obj.skus = resp.data.data
           this.selectedGoods = obj
          }).catch(error => console.log(error))

      },

```

#### 修改GoodsForm.vue的监听方法

```js
 watch: {
    oldGoods: {
      deep: true,
      handler(val) {
        if (!this.isEdit) {
          Object.assign(this.goods, {
            categories: null, // 商品分类信息
            brandId: 0, // 品牌id信息
            title: "", // 标题
            subTitle: "", // 子标题
            spuDetail: {
              packingList: "", // 包装列表
              afterService: "", // 售后服务
              description: "" // 商品描述
            }
          });
          this.specs = [];
          this.specialSpecs = [];
        } else {
          this.goods = Object.deepCopy(val);

          
          // 组织商品分类数据
          //延时函数
          setTimeout(()=>{
            // 先得到分类名称
          const names = val.categoryName.split("/");

            this.goods.categories = [
            { id: val.cid1, name: names[0] },
            { id: val.cid2, name: names[1] },
            { id: val.cid3, name: names[2] }
          ];
          },50)

          // 将skus处理成map
          const skuMap = new Map();
          this.goods.skus.forEach(s => {
            skuMap.set(s.indexes, s);
          });
          this.goods.skus = skuMap;
        }
      }
    },
```

### 后台

#### 在GoodsService中添加修改方法

```java
	@ApiOperation(value = "修改商品信息")
    @PutMapping(value = "goods/save")
    Result<JSONObject> updateGoods(@RequestBody SpuDTO spuDTO);
```

#### 在GoodsServiceImpl中实现GoodsService接口中添加的修改方法

```java
	@Override
    @Transactional
    public Result<JSONObject> updateGoods(SpuDTO spuDTO) {
        final Date date = new Date();
        //修改spu
        SpuEntity spuEntity = BaiduBeanUtils.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setLastUpdateTime(date);
        spuMapper.updateByPrimaryKeySelective(spuEntity);
        
        //修改spuDetail
        SpuDetailEntity spuDetailEntity = BaiduBeanUtils.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class);
        spuDetailMapper.updateByPrimaryKeySelective(spuDetailEntity);

        //根据spuId查询sku信息
        this.deleteSkuAndStockInfo(spuDTO.getId());
        //删除sku信息后   进行批量新增
        //新增sku
        this.saveSkuAndStockInfo(spuDTO,spuEntity.getId(),date);

        return this.setResultSuccess();
    }
```

##### 在根据spuId查询sku信息的时候发现没有删除Id集合的方法  修改SkuMapper 和  StockMapper  添加DeleteByIdListMapper

```java
public interface SkuMapper extends Mapper<SkuEntity>, InsertListMapper<SkuEntity>, DeleteByIdListMapper<SkuEntity,Long> {

    @Select(value = "SELECT k.*,t.stock FROM tb_sku k,tb_stock t where k.id = t.sku_id  and k.spu_id = #{spuId}")
    List<SkuDTO> getSkusAndStockByspuId(Integer spuId);
}
```

```java
public interface StockMapper extends Mapper<StockEntity>, DeleteByIdListMapper<StockEntity,Long> {
}
```

##### 发现修改方法中的新增Sku代码与新增方法中的代码一部分相同  将他们提取出来

```java
	//封装新增sku和stock方法啊
    private void saveSkuAndStockInfo(SpuDTO spuDTO,Integer spuId,Date date){
        List<SkuDTO> skus  = spuDTO.getSkus();
        skus.stream().forEach(skuDTO -> {
            SkuEntity skuEntity = BaiduBeanUtils.copyProperties(skuDTO, SkuEntity.class);
            skuEntity.setSpuId(spuId);
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            skuMapper.insertSelective(skuEntity);

            //新增tock
            StockEntity stockEntity = new StockEntity();
            stockEntity.setSkuId(skuEntity.getId());
            stockEntity.setStock(skuDTO.getStock());
            stockMapper.insertSelective(stockEntity);
        });
    }
```

## 删除

### 前台

#### 修改Goods.vue中的删除方法

```js
deleteItem(id) {
        this.$message.confirm('此操作将永久删除该商品, 是否继续?')
          .then(() => {
            // 发起删除请求
            this.$http.delete("/goods/delete?spuId=" + id)
              .then(() => {
                // 删除成功，重新加载数据
                this.getDataFromApi();
                this.$message.info('删除成功!');
              })
          })
          .catch(() => {
            this.$message.info('已取消删除');
          });

      },
```

### 后台

#### 在GoodsService中添加删除方法

```java
	@ApiOperation(value = "删除商品")
    @DeleteMapping(value = "goods/delete")
    Result<JSONObject>  delete(Integer spuId);
```

#### 在GoodsServiceImpl中实现GoodsService中添加的删除方法

```java
	@Override
    @Transactional
    public Result<JSONObject> delete(Integer spuId) {
        //删除spu信息
        spuMapper.deleteByPrimaryKey(spuId);
        //删除spuDetail信息
        spuDetailMapper.deleteByPrimaryKey(spuId);
        //删除sku和stock信息
        this.deleteSkuAndStockInfo(spuId);

        return this.setResultSuccess();
    }
```

##### 在删除sku和stock信息的时候发现删除代码和修改里的一部分代码相同  把他提取出来

```java
	//封装删除sku和stock信息
    private void deleteSkuAndStockInfo(Integer spuId){
        Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuId);
        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);

        List<Long> skuIdList = skuEntities.stream().map(skuEntity ->
                skuEntity.getId()
        ).collect(Collectors.toList());
        skuMapper.deleteByIdList(skuIdList);
        stockMapper.deleteByIdList(skuIdList);
    }
```

## 完善项目

### 上架下架

#### 前台

##### 在上架下架按钮上添加单击事件

line : 53

```jsp
		  <v-btn icon small v-if="props.item.saleable" @click="xiajia(props.item)">下架</v-btn>
          <v-btn icon v-else @click="xiajia(props.item)">上架</v-btn>
```

##### 在method中添加上下架方法

```js
	xiajia(resp){
          console.log(resp)
            this.$http({
          method:"put",
          url: "goods/updateSaleable",
          data: resp
        }).then(() =>{
          this.getDataFromApi();
        }).catch(error => console.log(error))
      },
```

#### 后台

##### 在GoodsService中添加上下架方法

```java
	@ApiOperation(value = "上下架")
    @PutMapping(value = "goods/updateSaleable")
    Result<JSONObject>  updateSaleable(@RequestBody  SpuDTO spuDTO);
```

##### 在GoodsServiceImpl中实现GoodsService中添加的上下架方法

```java
	@Override
    @Transactional
    public Result<JSONObject> updateSaleable(SpuDTO spuDTO) {
        SpuEntity spuEntity = BaiduBeanUtils.copyProperties(spuDTO, SpuEntity.class);
        if (ObjectUtil.isNotNull(spuEntity.getSaleable()) && spuDTO.getSaleable() < 2){
            if (spuEntity.getSaleable() == 1){
                spuEntity.setSaleable(0);
            }else {
                spuEntity.setSaleable(1);
            }
            spuMapper.updateByPrimaryKeySelective(spuEntity);
        }

        return this.setResultSuccess();
    }
```














