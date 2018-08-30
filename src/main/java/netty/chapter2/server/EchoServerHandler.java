package netty.chapter2.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * ChannelHandler  用于接受客户端信息,并实现需要处理的业务逻辑
 */
@ChannelHandler.Sharable    //标识一个 ChannelHandler 可以被多个 channel 安全共享
public class EchoServerHandler extends ChannelHandlerAdapter{

    /**
     * channelRead() 对于每个传入的消息都要调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received:" + in.toString(CharsetUtil.UTF_8));
        //将接受的消息写给发送者,而不是冲刷出站消息
        ctx.write(in);
    }

    /**
     * 通知 ChannelInboundHandler 最后一次对channel-Read() 的调用是当前批量读取的最后一条消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将目前暂存于 ChannelOutboundBuffer 中的消息 冲刷到远程节点,并且关闭该 Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在读取操作期间,有异常时会调用
     * 如果没有实现该方法,跑出的异常会被丢到下一个 ChannelHandler,或者传递到ChannelPipeline的尾端并被记录
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常栈的跟踪
        cause.printStackTrace();
        //关闭该channel
        ctx.close();
    }
}
