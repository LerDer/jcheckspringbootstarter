# jcheckspringbootstarter

#### 介绍
SpringBoot 参数校验框架
- `spring-boot-starter-web`: web应用，引入这个starter后，可以不用再引入`spring-boot-starter-web`
- `spring-boot-starter-aop`: 主要用到了AOP，面向切面编程。
- `aviator`: 谷歌的规则引擎，主要用来校验参数
- `fastjson`: 阿里Json工具
- `commons-lang3`: apache工具
- `lombok`: 工具
- `swagger2`: 生成接口文档

#### 软件架构
软件架构说明


#### 安装教程

1. mvn clean install
2. 引入依赖
```xml
<dependency>
   <groupId>com.ler</groupId>
   <artifactId>jcheck-spring-boot-starter</artifactId>
   <version>1.0.0-SNAPSHOT</version>
</dependency>
```

#### 使用说明

见 <a href="https://www.yunask.cn/" target="_blank">南诏Blog</a>
JCheck参数校验框架之创建自己的SpringBoot-Starter

# 使用方法

# 依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.ler</groupId>
    <artifactId>checkdemo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>checkdemo</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!--自定义的starter-->
        <dependency>
            <groupId>com.ler</groupId>
            <artifactId>jcheck-spring-boot-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```
- `spring-boot-starter-test`这个打包进去的话，作用域不好弄，就不打包了

# application.properties
```properties
#环境
ler.con.env=gray
#swagger标题，中文会乱码
ler.swagger.title=JCheck Demo
#swagger版本
ler.swagger.version=1.0.0
#swagger要扫描的controller包
ler.swagger.controller-package-name=com.ler.checkdemo.controller
```

# 实体类

```java
package com.ler.checkdemo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lww
 * @date 2019-09-05 01:05
 */
@Data
@ApiModel(value = "测试对象", description = "测试")
public class User {

	@ApiModelProperty("姓名")
	private String name;

	@ApiModelProperty("年龄")
	private Integer age;

}
```

# 启动类

```java
package com.ler.checkdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lww
 */
@SpringBootApplication
public class CheckdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckdemoApplication.class, args);
	}

}
```

# Controller

```java
package com.ler.checkdemo.controller;

import com.ler.checkdemo.domain.User;
import com.ler.jcheckspringbootstarter.annation.Check;
import com.ler.jcheckspringbootstarter.config.HttpResult;
import com.ler.jcheckspringbootstarter.util.Checks;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lww
 * @date 2019-09-03 17:26
 */
@Api("检查测试")
@RestController
public class ExampleController {

	@ApiOperation("普通参数检查")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "姓名"),
			@ApiImplicitParam(name = "age", value = "年龄"),
	})
	@GetMapping("/param")
	@Check(ex = "name != null", msg = "name为空！")
	@Check(ex = "age != null", msg = "age为空！")
	public String input(String name, Integer age) {
		Checks.isTrue(false, "Checks检查异常，类似Assert断言");
		return "Success";
	}

	@ApiOperation("对象参数检查")
	@PostMapping("/body")
	@Check(ex = "user.name != null", msg = "name为空！")
	@Check(ex = "user.age != null", msg = "age为空！")
	public HttpResult b(@RequestBody User user, HttpServletRequest request) {
		Assert.isTrue(false, "断言异常灰度不报！");
		return HttpResult.success();
	}

}
```
# 项目结构如下

<br>
<img src="https://yunask.cn/static/e5ffc594be0f4326bace01f9bda5b33b.jpg" width="100%">
<br>

# 启动后，swagger已经配好了

<br>
<img src="https://yunask.cn/static/b0ca61573555480f8b2e5c3254f84d29.jpg" width="100%">
<br>

# 普通参数校验，name是形参名字

`@Check(ex = "name != null", msg = "name为空！")` 按照注解顺序依次校验
<br>

<img src="https://yunask.cn/static/10fba6cbe0fe4d78b8616573e1a71686.jpg" width="100%">
<br>

`@Check(ex = "age != null", msg = "age为空！")`
<br>

<img src="https://yunask.cn/static/134495bc5c6f4a48877286a111a4706d.jpg" width="100%">
<br>

`Checks.isTrue(false, "Checks检查异常，类似Assert断言");`
<br>

<img src="https://yunask.cn/static/421988fb690f48609edcd03d7eccc164.jpg" width="100%">
<br>

# Json对象校验，使用RequestBody注解的。user是形参名字

`@Check(ex = "user.name != null", msg = "name为空！")`
<br>

<img src="https://yunask.cn/static/1bb715a7f3564d6888647f24e017ed02.jpg" width="100%">
<br>

`@Check(ex = "user.age != null", msg = "age为空！")`
<br>

<img src="https://yunask.cn/static/dd1a4c9708bd4d98a0a655523b4c1cae.jpg" width="100%">
<br>

`Assert.isTrue(false, "断言异常灰度不报！");`因为配置的环境是灰度，所以不显示`断言异常灰度不报!`
<br>
<img src="https://yunask.cn/static/f0ded0193e2844bcb94f836cf0be0f22.jpg" width="100%">
<br>

starter项目源码
<a href="https://gitee.com/github-26359270/checkdemo.git" target="_blank">测试项目源码</a>

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request


#### 码云特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. 码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5. 码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. 码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)