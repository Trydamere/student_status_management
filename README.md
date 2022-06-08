# student_status_management



## 项目介绍

基于区块链的教学成绩管理系统（本科毕设）

技术栈：vue+springboot+hyperledger fabric

教学成绩管理系统参考博客:[教学成绩管理系统](https://blog.csdn.net/symuamua/article/details/107528360)

将成绩和补考成绩的状态数据记录在区块中，实现成绩防篡改。

视频展示: https://www.bilibili.com/video/BV1hF411V7dd?share_source=copy_web

## 部署教程



- 根据hyperledger fabric教程安装docker镜像，二进制文件，示例文件

- 使用 startFabric.sh 脚本启动网络。

```
cd fabric-samples/fabcar
./startFabric.sh go
cd fabric-samples/test-network

cd organizations/peerOrganizations/org1.example.com/users/User1@org1.example.com/msp/keystore/
mv *_sk pri_sk
cd organizations/peerOrganizations/org2.example.com/users/User1@org2.example.com/msp/keystore/
mv *_sk pri_sk

docker-compose up -d
```

- 打包前端

```
npm run build
cp dist/ s
```

- 打包后端

```
mvn package clean -Dmaven.test.skip=true
mvn package -Dmaven.test.skip=true
java -jar your_project_name_version.jar
mv target/your_project_name_version.jar your_project_name_version.jar
```

- 关闭网络

```
cd fabric-samples/fabcar
./networkDown.sh
cd fabric-samples/test-network
docker-compose down -v
```

- 访问8080 8081


docker查看日志

```shell
$ docker logs --since 30m CONTAINER_ID
```

