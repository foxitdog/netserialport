package io.github.foxitdog.netserialport.netty;

import java.util.concurrent.TimeUnit;

import io.github.foxitdog.netserialport.utils.Constants;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.CustomLog;

/**
 * @Description
 * @Author wei
 * @Date 2021/5/12 11:03
 */
@CustomLog
public class SerialportBridgeClient implements Runnable {

    public static ChannelFuture future;
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    public void connect() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定IO类型为NIO
                .channel(NioSocketChannel.class)
                // 3.IO处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new LengthFieldPrepender(2)).addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2)).addLast(new SerialportBridgeHandler());
                    }
                });
        // 4.建立连接
        future = bootstrap.connect(Constants.HOST, Constants.PORT).sync();
        log.info("netty client connect success, host:{} port:{}", Constants.HOST, Constants.PORT);
        future.channel().closeFuture().sync();
    }

    @Override
    public void run() {
        while(true){
            try {
                connect();
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }
}

