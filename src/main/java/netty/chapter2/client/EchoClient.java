package netty.chapter2.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //创建bootstrap
            Bootstrap b = new Bootstrap();
            //指定 EventLoopGroup 以处理客户端书剑;需要适用NIO的实现
            b.group(group)
                    //适用于NIO 传输的Channel
                    .channel(NioSocketChannel.class)
                    //设置服务器的 InetSocketAddress
                    .remoteAddress(new InetSocketAddress(host,port))
                    //在创建channel时,向 ChannelPipeline 中添加一个 EchoClientHandler
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //连接到远程节点,阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            //阻塞,直到Channel 关闭
            f.channel().closeFuture().sync();
        } finally {
            //关闭线程池并且释放所有的资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
//        if(args.length != 2){
//            System.err.println("Usage: " + EchoClient.class.getSimpleName() + "<host><port>");
//        }

        final String host = "127.0.0.1";//args[0];
        final int port = 8080;//Integer.valueOf(args[1]);
        new EchoClient(host,port).start();
    }


}
