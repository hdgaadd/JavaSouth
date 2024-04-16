## 1. Java NIO

> 了解过NIO吗？

了解的面试官。NIO的出现在于提高IO的速度，它相比传统的输入/输出流速度更快。

NIO通过管道Channel和缓冲器Buffer来处理数据，可以把管道当成一个矿藏，缓冲器就是矿藏里的卡车。

程序通过管道里的缓冲器进行数据交互，而**不直接处理数据**。程序要么从缓冲器获取数据，要么输入数据到缓冲器。

### 1.1 通道和缓冲器

> 那NIO为什么速度快？

是这样的，NIO提供了通道和缓冲器这两个核心对象。

（1）管道Channel：

与传统的IO流只能只读或只写的单向流不同，NIO通道是**双向的**，也就是说读写操作可以同时进行，使得数据的处理效率也更高。

（2）缓冲器Buffer：

传统的输入/输出流一次只处理一个字节，而每一次字节读取都是一次系统调用，涉及到用户空间和内核空间之间的上下文切换，通常来说效率不高。

而NIO采用内存映射文件方式来处理输入/输出，Channel通过map()方法把**一块数据**映射到内存中。程序通过Buffer进行数据交互，减少了与原始数据源的直接访问。NIO**面向块**的处理方式使得效率更高。

### 1.2 非阻塞IO模型

> 还有吗？

有的。

传统的输入/输出流是同步阻塞IO模型，如果数据源没有数据了，此时程序将进行阻塞。而NIO是I/O多路复用模型，线程可以**询问**通道有没可用的数据，而不需要在没有数据时阻塞掉线程。

### 1.3 字符流处理字符？

> 你刚刚说输入/输出流是处理字节？字符流不是处理字符吗

不是的。所有数据包括文本数据最终都是以**字节形式**存储的，因为计算机底层只能理解二进制数据。

字符最终也是要转换成字节形式，之所以可以在文本文件看到字符是因为系统会将底层的二进制序列转换成了字符。

### 1.4 其他IO模型

> 还了解其他IO模型吗



## 2. 通道和缓冲器使用

### 2.1 缓冲器

> 你具体介绍下Buffer

好的，Buffer里有3个**关键变量**。

![微信截图_20240416155027](D:\code\z-mine\universe\Java系列\Java NIO\微信截图_20240416155027.png)

1. capcity：表示缓冲器Buffer的最大数据容量。
2. position：用来指出下一个可以读出/写入Buffer的索引位置，也就是记录指针的作用。
3. limit：用来表示在Buffer里第一个不能被读出/写入的索引位置。

![微信截图_20240416155509](D:\code\z-mine\universe\Java系列\Java NIO\微信截图_20240416155509.png)

另外Buffer还提供了get、put方法来供我们操作数据，而使用get/put后，position的指针位置也会随之移动。

```java
public abstract byte get();

public abstract ByteBuffer put(byte b);
```

### 2.2 通道

> Channel呢？

Channel有常见的3个方法，map()、read()和write()。

```java
// 将通道文件的区域直接映射到字节缓冲区中
public abstract MappedByteBuffer map(MapMode mode, long position, long size)

// 从此Channel通道读取字节序列到给定缓冲区dst
public abstract int read(ByteBuffer dst)
    
// 将给定缓冲区中src的字节序列写入此Channel通道
public abstract int write(ByteBuffer src)
```

以下是Channel的简单使用代码。

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

