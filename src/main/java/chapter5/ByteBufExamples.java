package chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;

public class ByteBufExamples {

    private final static Random random = new Random();

    private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

//    public static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE;

    private static void handleArray(byte[] array, int offset, int len) {}


    /**
     * 代码清单 5-1 支撑数组
     */
    public static void headBuffer(){
        ByteBuf headBuf = BYTE_BUF_FROM_SOMEWHERE;
        //检查ByteBuf是否是一个支撑数组
        if(headBuf.hasArray()){
            //如果有,则获取对该数组的引用
            final byte[] array = headBuf.array();
            //计算第一个字节的偏移量
            final int offset = headBuf.arrayOffset() + headBuf.readerIndex();
            //获得可读字节数
            final int length = headBuf.readableBytes();
            //使用数组、偏移量和长度作为参数调用你的方法
            handleArray(array,offset,length);
        }
    }

    /**
     * 代码清单 5-2 访问直接缓冲区的数据
     */
    public static void directBuffer(){
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE;
        //检查ByteBuf是否由数组支撑,如果不是,则是一个直接缓冲区
        if(!directBuf.hasArray()){
            //获取可读字节数
            final int length = directBuf.readableBytes();
            //分配一个新的数组来保存具有改长度的字节数据
            byte[] array = new byte[length];
            //将字节复制到该数组
            directBuf.getBytes(directBuf.readerIndex(),array);
            //使用数组、偏移量和长度作为参数调用你的方法
            handleArray(array,0,length);
        }
    }

    /**
     * 代码清单 5-3 使用 ByteBuffer 的复合缓冲区模式
     */
    public static void byteBufferComposite(ByteBuffer header,ByteBuffer body){
        ByteBuffer[] message = new ByteBuffer[]{header,body};

        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }


    /**
     * 代码清单 5-4 使用 CompositeByteBuf 的复合缓冲区模式
     */
    public static void byteBufComposite() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE; // can be backing or direct
        ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE;   // can be backing or direct
        //将 ByteBuf 实例追加到 CompositeByteBuf
        messageBuf.addComponents(headerBuf, bodyBuf);
        //...
        //删除位于索引位置为 0（第一个组件）的 ByteBuf
        messageBuf.removeComponent(0); // remove the header
        //循环遍历所有的 ByteBuf 实例
        final Iterator<ByteBuf> iterator = messageBuf.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }
    }

    /**
     * 代码清单 5-5 访问 CompositeByteBuf 中的数据
     */
    public static void byteBufCompositeArray(){
        final CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        //获得可读字节数
        final int length = compBuf.readableBytes();
        //分配一个具有可读字节数长度的新数组
        byte[] array = new byte[length];
        //将字节读到该数组中
        compBuf.getBytes(compBuf.readerIndex(),array);
        //使用偏移量和长度作为参数使用该数组
        handleArray(array,0,array.length);
    }

    /**
     * 代码清单 5-6 访问数据
     */
    public static void byteBufRelativeAccess() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }
    }

    /**
     * 代码清单 5-7 读取所有数据
     */
    public static void readAllData() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
    }

    /**
     * 代码清单 5-8 写数据
     */
    public static void write() {
        // Fills the writable bytes of a buffer with random integers.
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        while (buffer.writableBytes() >= 4) {
            buffer.writeInt(random.nextInt());
        }
    }


    /**
     * 代码清单 5-9 使用 ByteBufProcessor 来寻找\r
     *
     * use {@link io.netty.buffer.ByteBufProcessor in Netty 4.0.x}
     */
    public static void byteProcessor() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        int index = buffer.forEachByte(ByteBufProcessor.FIND_CR);
    }

    /**
     * 代码清单 5-9 使用 ByteBufProcessor 来寻找\r
     *
     * use {@link io.netty.util.ByteProcessor in Netty 4.1.x}
     */
    public static void byteBufProcessor() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        int index = buffer.forEachByte(ByteBufProcessor.FIND_CR);
    }

    /**
     * 代码清单 5-10 对 ByteBuf 进行切片
     */
    public static void byteBufSlice() {
        Charset utf8 = Charset.forName("UTF-8");
        //创建一个用于保存给定字符串的字节的 ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //创建该 ByteBuf 从索引 0 开始到索引 15 结束的一个新切片
        ByteBuf sliced = buf.slice(0, 15);
        //将打印“Netty in Action”
        System.out.println(sliced.toString(utf8));
        //更新索引 0 处的字节
        buf.setByte(0, (byte)'J');
        //将会成功，因为数据是共享的，对其中一个所做的更改对另外一个也是可见的
        assert buf.getByte(0) == sliced.getByte(0);
    }

    /**
     * 代码清单 5-11 复制一个 ByteBuf
     */
    public static void byteBufCopy() {
        Charset utf8 = Charset.forName("UTF-8");
        //创建 ByteBuf 以保存所提供的字符串的字节
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //创建该 ByteBuf 从索引 0 开始到索引 15 结束的分段的副本
        ByteBuf copy = buf.copy(0, 15);
        //将打印“Netty in Action”
        System.out.println(copy.toString(utf8));
        //更新索引 0 处的字节
        buf.setByte(0, (byte)'J');
        //将会成功，因为数据不是共享的
        assert buf.getByte(0) != copy.getByte(0);
    }

    public static void main(String[] args) {
        byteBufCopy();
    }



}
