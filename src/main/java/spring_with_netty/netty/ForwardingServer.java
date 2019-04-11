package spring_with_netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spring_with_netty.netty.handler.ForwardingTCPHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Wu
 * @Date: 2019/3/24 8:01 AM
 * 转发服务器
 */
public class ForwardingServer implements Runnable{
    private Thread t;
    private String threadName = "Forwarding-Server";
    private int listenPort = 6666;
    public Channel ch = null;
    public static Logger LOG = LogManager.getLogger(ForwardingServer.class.getName());
    private volatile static Map<String,Channel> ch_map = new ConcurrentHashMap<String,Channel>();//存储WebSocket连接的通道<PC[num],channel>

    public void setListenPort(int port){
        this.listenPort = port;
        LOG.info("Listen Port for WebSocket : " + port);
    }

    public void setThreadName(String name){
        this.threadName = name;
    }

    public static Map<String,Channel> getCh_map(){
        return ch_map;
    }

    public int getChannelAmount(){
        return ch_map.size();
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
                            ch.pipeline().addLast(new ForwardingTCPHandler());
                            //ch.pipeline().addLast(new TCP_ServerHandler4PC());//这样会有两个TCP_ServerHandler4PC处理器
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture cf = b.bind(listenPort).sync();
            LOG.info("Forwarding Server Started at Port: " + listenPort);

            ch = cf.channel();
            ch.closeFuture().sync();
        } catch (Exception e){
            LOG.error(e.getMessage());
        } finally {
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
        ForwardingServer server = new ForwardingServer();
        server.run();
    }

}
