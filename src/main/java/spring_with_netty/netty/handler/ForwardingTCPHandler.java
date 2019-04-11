package spring_with_netty.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import spring_with_netty.netty.ForwardingServer;

import java.util.Map;

/**
 * @Author: Wu
 * @Date: 2019/3/24 8:23 AM
 * ForwardingServer的TCP转发Handler
 */
public class ForwardingTCPHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf in = (ByteBuf)msg;
            ForwardingServer.LOG.info("Recv from WebSocket:" + in.toString(CharsetUtil.UTF_8));
            //ctx.writeAndFlush(Unpooled.copiedBuffer(in));

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ForwardingServer.LOG.info("WebSocket " + ctx.channel().remoteAddress() + " connected!");
        Map<String, Channel> map_temp = ForwardingServer.getCh_map();
        synchronized(map_temp) {
            map_temp.put(ctx.channel().remoteAddress().toString(), ctx.channel());
        }

        //ctx.writeAndFlush(Unpooled.copiedBuffer("Connected!", CharsetUtil.UTF_8));
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Map<String,Channel> map_temp = ForwardingServer.getCh_map();

        map_temp.remove(ctx.channel().remoteAddress().toString());
        ForwardingServer.LOG.info("WebSocket " + ctx.channel().remoteAddress().toString() + " disconnected!");
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
