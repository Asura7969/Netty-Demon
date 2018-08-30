package netty.chapter4;

import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChannelOperationExamples {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();


    //写出到Channel
    public static void writingToChannel(){
        final Channel channel = CHANNEL_FROM_SOMEWHERE;
        //创建持有要写数据的 ByteBuf
        final ByteBuf buf = Unpooled.copiedBuffer("your data",CharsetUtil.UTF_8);
        ChannelFuture cf = channel.writeAndFlush(buf);
        //添加 ChannelFutureListener 以便在写操作完成后接收通知
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    //写操作完成,并且没有错误发生
                    System.out.println("Write successful");
                }else{
                    //记录错误
                    System.err.println("write error");
                    future.cause().printStackTrace();
                }
            }
        });
    }
    //多个线程使用同一个 Channel
    public static void writingToChannelFromManyThreads() {
        final Channel channel = CHANNEL_FROM_SOMEWHERE;
        //创建持有要写数据的 ByteBuf
        final ByteBuf buf = Unpooled.copiedBuffer("your data",CharsetUtil.UTF_8);
        //创建将数据写到 Channel的 Runnable
        Runnable writer = new Runnable() {
            @Override
            public void run() {
                channel.write(buf.duplicate());
            }
        };

        //获取线程池 Executor的引用
        final ExecutorService executor = Executors.newCachedThreadPool();

        //递交写任务给线程池 以便在某个线程中执行
        //write in another thread
        executor.execute(writer);

        //递交写任务给线程池 以便在某个线程中执行
        //write in another thread
        executor.execute(writer);
        //.....
    }

}
