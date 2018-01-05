# css-code-loaded
关于代码热加载技术:springloaded与spring-boot-devtools
## 一.Spring-Loaded

### 1.工作原理
```
gg
```
### 2.Maven配置
```xml
<dependency>
    <groupId>org.springframework</groupId>
	<artifactId>springloaded</artifactId>
    <version>1.2.8.RELEASE</version>
</dependency>
```
### 3.部署方式
**1）Spring-Boot部署方式**
```
java -javaagent://10.100.96.42/Repository\org\springframework\springloaded\1.2.8.RELEASE/springloaded-1.2.8.RELEASE.jar -noverify
```
**2)中间件（Tomcat）部署方式**
```
JAVA_OPTS=%JAVA_OPTS% -javaagent://10.100.96.42/Repository\org\springframework\springloaded\1.2.8.RELEASE/springloaded-1.2.8.RELEASE.jar -noverify
```
### 4.Hotspot支持方式
<img url="_img/spring-loaded.png" weight=541 heigh=209/>

