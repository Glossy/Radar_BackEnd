package spring_with_netty.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import org.apache.log4j.Logger;
import spring_with_netty.netty.UDPRadarServer;
import spring_with_netty.netty.util.DataProcessor;


/**
 * 新雷达 UDP解析Handler
 * @Date: 2019/4/9 9:41 PM
 */
public class RadarUDPHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = UDPRadarServer.LOG;
    private static DataProcessor processor = new DataProcessor();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!UDPRadarServer.isConnected){
            UDPRadarServer.LOG.info("Radar Connected!");
            UDPRadarServer.isConnected = true;
        }
        UDPRadarServer.addPacksNum();
        processor.parse((DatagramPacket)msg);

    }


    @Override
    public void channelActive(final ChannelHandlerContext ctx){
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx){
        UDPRadarServer.isConnected = false;
    }


}
