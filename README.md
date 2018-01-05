# css-code-loaded
关于代码热加载技术:springloaded与spring-boot-devtools
## 一.Spring-Loaded

1.工作原理
----------------------------
```
gg
```
2.Maven配置
----------------------------

```xml
<dependency>
    <groupId>org.springframework</groupId>
	<artifactId>springloaded</artifactId>
    <version>1.2.8.RELEASE</version>
</dependency>
```

3.部署方式
----------------------------
```

```
4.Hotspot支持方式
----------------------------

类型      | 属|性  |方|法    |构造|函数
--------|----|---|----|---|----|--
--------|新增|修改|新增|修改|新增|修改
--------|---|---|---|---|---|---
新增普通类|√  |√   |√  |√  | √ | √
已有普通类|ⅹ  |ⅹ     |√  |√  |ⅹ      |ⅹ

