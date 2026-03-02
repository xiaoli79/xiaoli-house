# 建议修改后的版本
FROM eclipse-temurin:17-jre-alpine

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 设置工作目录
WORKDIR /workspace

# 原来的 MAINTAINER 已经过时，现在流行用 LABEL
LABEL maintainer="bitejiuyeke"

# 复制 JAR 包
COPY target/*.jar /workspace/app.jar

# 启动命令
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar