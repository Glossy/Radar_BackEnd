package spring_with_netty.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import spring_with_netty.netty.ForwardingServer;

import java.util.Iterator;
import java.util.Map;

/**
 * @Author: Wu
 * @Date: 2019/3/25 9:14 AM
 * 具体的转发类
 */
public class ForwardingThread {
    protected static void send2WebSocket(ByteBuf temp) {
        synchronized(ForwardingServer.getCh_map()) {
            for(Iterator<Map.Entry<String, Channel>> item = ForwardingServer.getCh_map().entrySet().iterator(); item.hasNext();) {
                Map.Entry<String,Channel> entry = item.next();
                //System.out.println("Key:"+entry.getKey());
                byte[] bytes = new byte[temp.readableBytes()];
                temp.getBytes(temp.readerIndex(), bytes);
                String str = new String(bytes, 0, temp.readableBytes());
                ForwardingServer.LOG.info("Send to "+entry.getKey()+ " message : " + str);

                ByteBuf temp1 = temp.copy();
                ChannelFuture future = entry.getValue().pipeline().writeAndFlush(temp1);
                future.addListener(new ChannelFutureListener(){
                    public void operationComplete(ChannelFuture f) {
                        if(!f.isSuccess()) {
                            f.cause().printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public static void main(String[] args){
        String message = "{'dispatch': 'workers', 'emit': {'event': 'transection', 'args': {'area': 'A8', 'time': '2019-03-24 00:10:29.272017', 'action': 1}}}\n";
        ForwardingThread.send2WebSocket(Unpooled.wrappedBuffer(message.getBytes()));
    }
}

