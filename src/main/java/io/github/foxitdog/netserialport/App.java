package io.github.foxitdog.netserialport;

import io.github.foxitdog.netserialport.netty.SerialportBridgeClient;
import io.github.foxitdog.netserialport.utils.BitUtils;
import io.github.foxitdog.netserialport.utils.Constants;
import io.github.foxitdog.netserialport.utils.eventbus.impl.NormalAbsReceiver;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import lombok.CustomLog;

/**
 * @author
 */
@CustomLog
public class App {
    public void run(String[] args) {
        Thread serialportBridgeThread = new Thread(new SerialportBridgeClient(), "SerialportBridgeThread");
        serialportBridgeThread.start();
        for (Constants.SerialportConfig config : Constants.serialportConfigs) {
            if(config.enable){
                startSerialPort(config);
            }else{
                log.debug("配置{}未启用", config.id);
            }
        }
    }

    void startSerialPort(Constants.SerialportConfig serialportConfig){
        try {
            SerialPort serialPort = new SerialPort(serialportConfig.port);
            serialPort.openPort();
            serialPort.setParams(serialportConfig.baudrate, serialportConfig.databit, serialportConfig.stopbit, serialportConfig.parity);
            NormalAbsReceiver<String,BridgeData> normalAbsReceiver = new NormalAbsReceiver<String,BridgeData>(Constants.PUBLISHER){
                @Override
                public void handle(String type, BridgeData bridgeData) {
                    if(serialportConfig.no==bridgeData.type){
                        if(log.isDebugEnabled()){
                            log.debug("发送服务器数据到串口{}，数据：{}", bridgeData.type,BitUtils.toHexString(bridgeData.data));
                        }
                        try {
                            serialPort.writeBytes(bridgeData.data);
                        } catch (SerialPortException e) {
                            log.error(e.getMessage(),e);
                        }
                    }
                }
            };
            normalAbsReceiver.subscribe(Constants.EVENT_TO_SERIALPORT);
            serialPort.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    try {
                        if (serialPortEvent.getEventType() == SerialPortEvent.RXCHAR) {
                            byte[] bs = serialPort.readBytes(serialPortEvent.getEventValue());
                            BridgeData bd = new BridgeData(serialportConfig.no, bs);
                            if(log.isDebugEnabled()){
                                log.debug("串口{}接收到发往服务器的数据：{}", bd.type,BitUtils.toHexString(bd.data));
                            }
                            Constants.PUBLISHER.publish(Constants.EVENT_TO_SERVER, bd);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(),e);
                    }
                }

            });
        } catch (SerialPortException e) {
            e.printStackTrace();
        }

    }
}
