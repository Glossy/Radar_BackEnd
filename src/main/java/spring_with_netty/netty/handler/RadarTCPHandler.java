package spring_with_netty.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import spring_with_netty.netty.TCPRadarServer;
import spring_with_netty.netty.util.DataProcessor;


/**
 * @Author: Wu
 * @Date: 2019/3/5 3:58 PM
 * RadarServer的消息解码Handler
 */
public class RadarTCPHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = TCPRadarServer.LOG;
    private static DataProcessor processor = new DataProcessor();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//        ByteBuf in = (ByteBuf) msg;
//        String message = in.toString(CharsetUtil.UTF_8);
//        System.out.println(message);
        TCPRadarServer.addPacksNum();
        processor.parse((ByteBuf)msg);
    }


    @Override
    public void channelActive(final ChannelHandlerContext ctx){
        logger.info("雷达地址:"+ctx.channel().remoteAddress());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
