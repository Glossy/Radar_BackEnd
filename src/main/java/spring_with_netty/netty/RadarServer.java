package spring_with_netty.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import spring_with_netty.netty.handler.RadarTCPHandler;

/**
 * @Author: Wu
 * @Date: 2019/3/24 9:23 PM
 */
public class RadarServer implements Runnable{
    public static Logger LOG = Logger.getLogger("RadarServer");
    private Thread t;
    private String threadName = "RadarServer-Thread";
    private int listen_port = 5100;  //旧雷达5100 新雷达 50000
    private volatile static int packsNum = 0;

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
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler( new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RadarTCPHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);


            ChannelFuture cf = b.bind(listen_port).sync();
            LOG.info("Radar Server Started at Port: " + listen_port);
            cf.channel().closeFuture().sync();
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

    public static void main(String[] args){
        RadarServer server = new RadarServer();
        server.start();
    }


}
