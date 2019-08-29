
## JORK

Java 开发的高性能内网穿透工具。

### 运行

```
# 编译
mvn clean package

# 运行服务端
java -Djork.run.model=server -jar ./target/jork-1.0-SNAPSHOT-jar-with-dependencies.jar

# 运行客户端
java -Djork.run.model=client -Djork.client.config=./config.json -jar ./target/jork-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### 功能

+ [x] 80端口内网穿透
+ [ ] 443端口内网穿透
+ [ ] TCP端口内网穿透
+ [ ] Auth集成MySQL,Redis的Authentication验证
+ [ ] SSL加密传输
+ [ ] TCP连接池提高性能

### 协议

本软件遵循 **MIT** 协议。