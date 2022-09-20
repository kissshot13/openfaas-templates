FROM maven:3.6.3-openjdk-8 as builder

WORKDIR  /home/app

COPY . /home/app

RUN  cd function &&  mvn clean package -U -DskipTests --settings ./m2/settings.xml

RUN cp /home/app/function/target/*.jar ./application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM openfaas/of-watchdog:0.7.6 as watchdog

FROM openjdk:8-jre-slim as ship

RUN addgroup --system app \
    && adduser --system --ingroup app app

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog

RUN chmod +x /usr/bin/fwatchdog

WORKDIR /home/app

# 前面提取命令执行成功后取得的文件，用于镜像中启动应用所需
COPY --from=builder /home/app/dependencies/ ./
COPY --from=builder /home/app/spring-boot-loader/ ./
COPY --from=builder /home/app/snapshot-dependencies/ ./
COPY --from=builder /home/app/application/ ./

USER app

# 指定容器的工作目录
WORKDIR /home/app/

# fwatchdog收到web请求后的转发地址，java进程监听的就是这个端口
ENV upstream_url="http://127.0.0.1:8082"

# 运行模式是http
ENV mode="http"

# 拉起业务进程的命令，这里就是启动java进程
ENV fprocess="java org.springframework.boot.loader.JarLauncher"

# 容器对外暴露的端口，也就是fwatchdog进程监听的端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=5s CMD [ -e /tmp/.lock ] || exit 1

# 容器启动命令，这里是执行二进制文件fwatchdog
CMD ["fwatchdog"]


