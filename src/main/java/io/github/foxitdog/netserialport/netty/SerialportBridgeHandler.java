package io.github.foxitdog.netserialport.netty;

import io.github.foxitdog.netserialport.BridgeData;
import io.github.foxitdog.netserialport.utils.BitUtils;
import io.github.foxitdog.netserialport.utils.Constants;
import io.github.foxitdog.netserialport.utils.eventbus.impl.NormalAbsReceiver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.CustomLog;

/**
 * @author 
 */
@CustomLog
public class SerialportBridgeHandler extends SimpleChannelInboundHandler<ByteBuf> {

    NormalAbsReceiver<String, BridgeData> receiver;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        int size = msg.readableBytes();
        byte[] data = new byte[size-1];
        int no = msg.readByte();
        msg.readBytes(data);
        BridgeData bd = new BridgeData(no, data);
        if(log.isDebugEnabled()){
            log.debug("接收到服务器发往串口{}的数据：{}", bd.type, BitUtils.toHexString(bd.data));
        }
        Constants.PUBLISHER.publish(Constants.EVENT_TO_SERIALPORT, bd);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        log.info("连接建立：{}",ctx.channel().remoteAddress().toString());
        super.channelActive(ctx);
        receiver = new NormalAbsReceiver<String, BridgeData>(Constants.PUBLISHER) {
            @Override
            public void handle(String type, BridgeData bridgeData) {
                if(log.isDebugEnabled()){
                    log.debug("发送串口{}数据到服务器，数据：{}", bridgeData.type,BitUtils.toHexString(bridgeData.data));
                }
                ctx.writeAndFlush(ctx.alloc().buffer(bridgeData.data.length+1).writeByte(bridgeData.type).writeBytes(bridgeData.data));
            }
        };
        receiver.subscribe(Constants.EVENT_TO_SERVER);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接断开：{}",ctx.channel().remoteAddress().toString());
        super.channelInactive(ctx);
        receiver.disSubscribe();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error(cause.getMessage(),cause);
    }
}
