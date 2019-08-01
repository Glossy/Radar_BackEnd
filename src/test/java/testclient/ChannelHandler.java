package testclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Author: Wu
 * @Date: 2019/3/10 12:06 AM
 */
@io.netty.channel.ChannelHandler.Sharable
public class ChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{

        //ctx.writeAndFlush("55aa1234");
        System.out.println("connectin establish !");
//        ctx.writeAndFlush(Unpooled.copiedBuffer(" {\"dispatch\": \"workers\", \"emit\": {\"event\": \"dot\", \"args\": {\"x\": -0.2082559548668595, \"y\": 0.5213636838198589}}}", //2
//                CharsetUtil.UTF_8));
        //ctx.channel().close().sync();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             ByteBuf in) {
        System.out.println("HttpClient received: " + in.toString(CharsetUtil.UTF_8));    //3
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {                    //4
        cause.printStackTrace();
        ctx.close();
    }
}
