## 1. Java NIO

> ***面试官：了解过`NIO`吗？***

了解的面试官。NIO的出现在于提高`IO`的速度，它相比传统的输入/输出流速度更快。

NIO通过管道`Channel`和缓冲器`Buffer`来处理数据，可以把管道当成一个矿藏，缓冲器就是矿藏里的卡车。

程序通过管道里的缓冲器进行数据交互，而**不直接处理数据**。程序要么从缓冲器获取数据，要么输入数据到缓冲器。

### 1.1 通道和缓冲器

> ***面试官：那`NIO`为什么速度快？***

是这样的，NIO提供了通道和缓冲器这两个核心对象。

（1）管道`Channel`：

与传统的`IO`流只能只读或只写的**单向流**不同，NIO通道是**双向的**，也就是说读写操作可以同时进行，使得数据的处理效率也更高。

（2）缓冲器`Buffer`：

传统的输入/输出流一次只处理一个字节，而每一次字节读取都是一次系统调用，涉及到用户空间和内核空间之间的上下文切换，通常来说效率不高。

而`NIO`采用内存映射文件方式来处理输入/输出，Channel通过`map()`方法把**一块数据**映射到内存中。程序通过`Buffer`进行数据交互，减少了与原始数据源的直接访问。NIO**面向块**的处理方式使得效率更高。

### 1.2 非阻塞IO模型

> ***面试官：还有吗？***

有的。

传统的输入/输出流是同步阻塞`IO`模型，如果数据源没有数据了，此时程序将进行阻塞。

而`NIO`是I/O多路复用模型，线程可以**询问**通道有没可用的数据，而不需要在没有数据时阻塞掉线程。

### 1.3 字符流处理字符？

> ***面试官：你刚刚说输入/输出流是处理字节？字符流不是处理字符吗？***

不是的。所有数据包括文本数据最终都是以**字节形式**存储的，因为计算机底层只能理解二进制数据。

字符最终也是要转换成字节形式，之所以可以在文本文件看到字符，是因为系统将底层的二进制序列转换成了字符。

## 2. Channel和Buffer使用

### 2.1 Buffer

> ***面试官：你具体介绍下Buffer？***

好的，Buffer里有3个**关键变量**。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/d37341999383489896eeb066b43b3531.jpeg#pic_center)

1. capcity：表示缓冲器`Buffer`的最大数据容量。
2. position：用来指出下一个可以读出/写入`Buffer`的索引位置，也就是记录指针的作用。
3. limit：用来表示在`Buffer`里第一个不能被读出/写入的索引位置。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/e0b0d54d3cfb4c7fbc39819fae1bfe30.png#pic_center)


另外`Buffer`还提供了`get`、`put`方法来供我们操作数据，而使用`get/put`后，position的指针位置也会随之移动。

```java
public abstract byte get();

public abstract ByteBuffer put(byte b);
```

### 2.2 Channel

> ***面试官：Channel呢？***

Channel有常见的3个方法，map()、read()和write()。

```java
// 将通道文件的区域直接映射到字节缓冲区中
public abstract MappedByteBuffer map(MapMode mode, long position, long size)

// 从此Channel通道读取字节序列到给定缓冲区dst
public abstract int read(ByteBuffer dst)
    
// 将给定缓冲区中src的字节序列写入此Channel通道
public abstract int write(ByteBuffer src)
```

以下是`Channel`的简单使用代码。

```java
public class TestFileChannel {
    public static void main(String[] args) {
        File f = new File("D:\\JavaGetOffer\\TestFileChannel.java");
        try {
            FileChannel inChannel = new FileInputStream(f).getChannel();
            FileChannel outChannel = new FileOutputStream("a.txt").getChannel();
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, f.length());

            outChannel.write(buffer);
            buffer.clear();
            CharBuffer charBuffer = StandardCharsets.UTF_8.newDecoder().decode(buffer);
            System.out.println(charBuffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
```



## 3. NIO零拷贝

> ***面试官：知道`NIO`零拷贝吗？***

是这样的，在`NIO`零拷贝出现之前，一个I/O操作会将同一份数据进行**多次拷贝**。可以看下图，一次I/O操作对数据进行了四次复制，同时来伴随两次内核态和用户态的上下文切换，众所周知上下文切换是很耗费性能的操作。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/275097f4798a43ed89b88678857ebd59.png#pic_center)


而零拷贝技术改善了上述的问题。可以对比下图，零拷贝技术**减少**了对一份数据的拷贝次数，不再需要将数据在**内核态和用户态**之间进行拷贝，也意味不再进行上下文切换，让数据传输变得更加高效。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/fed9e965d29c48caa4206e88aea11fa0.png#pic_center)