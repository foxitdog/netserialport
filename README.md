### 网络串口
1. 支持485，232串口
1. 支持linux,mac_os_x,windows,solaris
1. 最大转发255个串口的数据
1. 与服务断开5s重连
1. 架构：
```
tcpserver-------netserialport-------serialport
    |---------------->|---------------->|v
   ^|<----------------|<----------------|
server按照协议发送数据到netserialport，netserialport根据协议中的串口号将数据发送到指定串口
串口获取到数据后让netserialport按照协议将数据发送到tcpserver.
```


-----------------------------------------------

### tcp协议
|类型|长度|描述|备注|
|--|--|--|--|
|hex|2|数据长度|串口加上数据的长度|
|hex|1|串口编号|
|hex|n|数据|
```
示例：00 03 01 02 03  
     00 03：数据长度为3
     01：串口号1
     02 03：数据
```

---

### 串口参数配置
```
int BAUDRATE_110 = 110;
int BAUDRATE_300 = 300;
int BAUDRATE_600 = 600;
int BAUDRATE_1200 = 1200;
int BAUDRATE_4800 = 4800;
int BAUDRATE_9600 = 9600;
int BAUDRATE_14400 = 14400;
int BAUDRATE_19200 = 19200;
int BAUDRATE_38400 = 38400;
int BAUDRATE_57600 = 57600;
int BAUDRATE_115200 = 115200;
int BAUDRATE_128000 = 128000;
int BAUDRATE_256000 = 256000;

int DATABITS_5 = 5;
int DATABITS_6 = 6;
int DATABITS_7 = 7;
int DATABITS_8 = 8;

int STOPBITS_1 = 1;
int STOPBITS_2 = 2;
int STOPBITS_1_5 = 3;

int PARITY_NONE = 0;
int PARITY_ODD = 1;
int PARITY_EVEN = 2;
int PARITY_MARK = 3;
int PARITY_SPACE = 4;
```



配置文件：config.properties
```
# 网络串口服务地址
server.host = 192.168.3.59
# 网络串口服务端口
server.port = 8079

# 转发串口的数量
# sp.num = x 
sp.num = 3

# id = 1..x
# 这份配置是否启用
# sp[id].enable = false
# 串口号
# sp[id].port = /dev/ttymxc1
# 波特率 参考上面配置
# sp[id].baudrate = 2400
# 数据位 参考上面配置
# sp[id].databit = 8
# 停止位 参考上面配置
# sp[id].stopbit = 1
# 校验位 参考上面配置
# sp[id].parity = 0
# 给平台发送数据时的串口标志位,有效值1-255
# sp[id].no = 1

sp1.enable = true
sp1.port = /dev/ttymxc1
sp1.baudrate = 2400
sp1.databit = 8
sp1.stopbit = 1
sp1.parity = 0
sp1.no = 1

sp2.enable = true
sp2.port = /dev/ttymxc3
sp2.baudrate = 9600
sp2.databit = 8
sp2.stopbit = 1
sp2.parity = 1
sp2.no = 2

sp3.enable = true
sp3.port = /dev/ttymxc6
sp3.baudrate = 2400
sp3.databit = 8
sp3.stopbit = 1
sp3.parity = 0
sp3.no = 3
```
日志文件：log.properties