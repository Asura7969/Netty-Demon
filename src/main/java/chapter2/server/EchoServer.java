package chapter2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public void start() throws Exception{
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // (1) 创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // (2) 创建ServerBootstrap  负责引导服务器和客户端
            ServerBootstrap b = new ServerBootstrap();
            // (3) 指定所使用的 NIO 传输 Channel
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    // (4) 指定所使用的端口以及套接字地址
                    .localAddress(new InetSocketAddress(port))
                    // (5) 添加一个 EchoChannelHandler 到 Channel 的 channelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {     //这个channelHandler 将会受到有关入站消息的通知
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // (6) EchoServerHandler 被标注为 @Sharable,所以我们可以总是使用同样的实例
                            //这里对于所有的客户端连接来说,都会使用同一个 EchoServerHandler ,因为其被标注为 @Sharable
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            // (6) 异步地绑定服务器: 调用 sync() 方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() +
                    "started and listening for connections on " + f.channel().localAddress());
            // (7) 获取 channel 的 closeFuture ,并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            // (8) 关闭 EventLoopGroup , 释放所有的资源
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws Exception {
//        if(args.length != 1){
//            System.err.println("Usage:" + EchoServer.class.getSimpleName() + "<port>");
//            return;
//        }

        int port = 8080;
        new EchoServer(port).start();
    }
}
