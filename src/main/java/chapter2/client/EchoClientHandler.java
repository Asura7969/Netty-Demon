package chapter2.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuffer> {

    /**
     * 每当接收到数据都会调用该方法,注：一个完整的数据可能被多次接受
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuffer msg) throws Exception {
        //记录已接受消息的转储
        System.out.println("Client received: " + msg.toString());
    }

    /**
     * 在到服务器的连接已经建立之后将被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知 channel 是最活跃的时候,发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常时,记录错误并关闭 channel
        cause.printStackTrace();
        ctx.close();
    }
}
