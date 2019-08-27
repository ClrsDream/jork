
## JORK

Java 开发的高性能内网穿透工具。

### 运行

```
# 编译
mvn clean install

# 运行服务端
java -Djork.run.model=server -cp ./target/jork-1.0-SNAPSHOT.jar com.xiaoteng.jork.App

# 运行客户端
java -Djork.run.model=client -Djork.client.config=/home/xiaoteng/jork.json -cp ./target/jork-1.0-SNAPSHOT.jar com.xiaoteng.jork.App
```

### 协议

本软件遵循 **MIT** 协议。