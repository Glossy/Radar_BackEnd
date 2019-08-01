package spring_with_netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.apache.log4j.Logger;
import spring_with_netty.netty.handler.RadarUDPHandler;

/**
 * New Radar UDP Server
 * @Author: Wu
 * @Date: 2019/4/9 9:20 PM
 */
public class UDPRadarServer implements Runnable{
    public static Logger LOG = Logger.getLogger("UDPRadarServer");
    private Thread t;
    private String threadName = "UDPRadarServer-Thread";
    private int listen_port = 50000;  //旧雷达5100 新雷达 50000
    private volatile static int packsNum = 0;
    public static boolean isConnected = false;

//    @PostConstruct
//    public void initLog(){
//
//    }

    public void setThreadName(String name){
        this.threadName = name;
    }

    public void setListen_port(int port){
        this.listen_port = port;
    }

    public static void resetPacksNum(){
        packsNum = 0;
    }

    public static void addPacksNum(){
        packsNum++;
    }

    public static int getPacksNum(){
        return packsNum;
    }

    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            EventLoopGroup group = new NioEventLoopGroup();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new RadarUDPHandler());

            LOG.info("Radar UDP Server Started at Port: " + listen_port);

            b.bind(listen_port).sync().channel().closeFuture().await();


        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void start () {
        LOG.info("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }



}
