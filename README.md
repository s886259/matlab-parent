# matlab

### 项目结构
```
matlab-parent
|
├──matlab-extension-starter --提供matlab-extension-starter.jar,只需引用此jar
|
├──matlab-extension --程序扩展(公式更新在此处)
|  |
|  |──time --时域程序
|     | 
|     |──time-acceleration --加速度时域
|     |
|     |──time-envolope --包络时域
|     |
|     |──time-velocity --速度时域
|        |
|        |──time-velocity-common --速度时域公共类
|        |
|        |──time-velocity-gear --速度时域(齿轮)
|        |
|        |──time-velocity-vibrator --速度时域(激振器)
| 
|  |──frequency --频谱图程序
|     | 
|     |──frequency-acceleration --加速度频谱(激振器)
|     | 
|     |──frequency-envolope --包络频谱(轴承)
|     | 
|     |──frequency-velocity --速度频谱
|        |
|        |──frequency-velocity-common --速度频谱公共类
|        |
|        |──frequency-velocity-gear --速度频谱(齿轮)
|        |
|        |──frequency-velocity-vibrator --速度频谱(激振器)
|     
|  |──displacement --位移(单轴加速度传感器使用)单值
|
|  |──vector-amplitude --矢量振幅和轴心轨迹
|
├──matlab-kernel --核心算法及工具类(公共包)
|
├──matlab-web --对外提供api(废弃)
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
![image](https://github.com/s886259/matlab-parent/blob/master/image/1417_time_domain_2.jpg)
