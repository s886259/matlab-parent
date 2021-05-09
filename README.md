# matlab

### 项目结构
```
matlab-parent
|
├──matlab-extension --程序扩展
|  |
|  ├──acceleration --加速度程序
|
├──matlab-kernel --核心算法及工具类
|
├──matlab-web --对外提供api
|  |
|  |──logs --日志目录
|  |
|  |──start.sh --启动脚本

```

### 编译
```shell
cd matlab-parent
mvn clean install
```

### 启动
```shell
cd matlab-parent
cd matlab-web
sh start.sh start
```

### 日志
```shell
cd matlab-parent
cd matlab-web
cd logs
```

### 接口文档
启动项目并访问项目根路径 + /doc.html
比如本次测试访问路径为http://127.0.0.1:8080/doc.html

### 示例
![image](https://github.com/s886259/matlab-parent/blob/master/image/1417_time_domain_1.jpg)