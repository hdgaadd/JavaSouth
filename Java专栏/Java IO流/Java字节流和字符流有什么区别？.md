## 1. IO的理解

> ***面试官：你说下对`Java IO`的理解？***

好的面试官，Java IO有两个参与对象，一个是**IO源端**，一个是想要和`IO`源端通信的各种**接收端**如控制台、文件等，我们程序要保证的就是顺利的**读取**和顺利的写入。

## 2. 输入流

### 2.1 字节输入流读取方法

> ***面试官：那要怎么读取字节流？***

读取字节的抽象基类是**InputStream**，这个基类提供了3个方法可以来读取字节流。

1. 从输入流读取下一个数据字节，值字节以0到255范围内的`int`返回。

   ```java
   public abstract int read() throws IOException
   ```
2. 从输入流读取一定数量的字节并将它们存储到缓冲区数组`b`中。

   ```java
   public int read(byte b[]) throws IOException
   ```
3. 从输入流读取最多`len`个字节的数据到字节数组中。

   ```java
   public int read(byte b[], int off, int len) throws IOException
   ```
   

### 2.2 字节输入流读取实例

> ***面试官：你说的这些不是实例，我要的是能真正读取的？***

那可以用抽象基类的子类实现来读取，例如**文件流FileInputStream**。

```java
new FileInputStream(SOURCE_PATH)
```

我们还可以在外面加一层**缓存字节流**来提高读取效率。

```java
new BufferedInputStream(new FileInputStream(SOURCE_PATH))
```

如果要把缓存流**换成字符流来承接**，方便使用**readLine()**读取某一行的话，可以使用**转换流**把字节输入流转换成字符输入流。

```java
new BufferedReader(new InputStreamReader(new FileInputStream(SOURCE_PATH)))
```

### 2.3 缓存流提高读取效率

> ***面试官：为什么加一层缓存流就能提高读取效率？***

是这样的。因为直接使用 `FileInputStream` 读取文件的话，每次调用 `read()` 都是从**磁盘读取一个字节**，而每次读取都是一次系统调用。**系统调用是操作系统层面的调用**，涉及到用户空间和内核空间之间的上下文切换，这些切换是很昂贵的。

而如果使用缓存流，一次性从文件里读取多个字节到缓存中，可以减少系统调用同时也减少了磁盘读取，提高了读取的效率。

### 2.4 字节输入流末尾

> ***面试官：读取之后呢，我怎么知道文件读取到末尾了？***

例如我刚刚说到的字节流基类`InputStream`的3个方法，当他们**返回-1**，就表明输入流到达了末尾。

### 2.5 字符输入流

> ***面试官：字符流读取呢？***

字符流的**抽象基类是Reader**，同样是提供了3个方法来支持字符流读取。

1. 读取单个字符。

   ```java
   public int read() throws IOException
   ```
2. 将字符读入数组。

   ```java
   public int read(char cbuf[]) throws IOException
   ```
3. 将字符读入数组的一部分。

   ```java
   abstract public int read(char cbuf[], int off, int len) throws IOException

然后字符流读取的实例是`FileReader`，同样可以使用缓存字符流提高读取效率。

```java
new BufferedReader(new FileReader(new File(SOURCE_PATH)))
```

## 3. 输出流

> ***面试官：输出流你也讲一讲？***

好的面试官。

字节输出流的**抽象基类是OutputStream**，字符输出流的**抽象基类是Writer**。他们分别提供了以下方法。

字节输出流`OutputStream`：

1. 将指定字节写入此输出流。

   ```java
   public abstract void write(int b) throws IOException
   ```
2. 将指定字节数组中的`b.length`字节写入此输出流。

   ```java
   public void write(byte b[]) throws IOException 
   ```
3. 将指定字节数组中从偏移量`off`开始的`len`个字节写入此输出流。

   ```java
   public void write(byte b[], int off, int len) throws IOException
   ```

字符输出流`Writer`：

1. 写入单个字符。

   ```java
   public void write(int c) throws IOException
   ```
2. 写入字符数组。

   ```java
   public void write(char cbuf[]) throws IOException
   ```
3. 写入字符数组的一部分。

   ```java
   abstract public void write(char cbuf[], int off, int len) throws IOException
   ```

另外字符输出流是使用**字符来操作数据**，所以可以用**字符串来代替字符数组**，JDK还支持以下**入参是字符串**的方法。

1. 写入一个字符串。

   ```java
   public void write(String str) throws IOException
   ```

2. 写入字符串的一部分。

   ```java
   public void write(String str, int off, int len) throws IOException
   ```

## 4. 字节流和字符流区别

> ***面试官：那字节流和字符流有什么区别？***

有这些不同之处，主要是3个方面。

- **基本单位不同**。字节流以字节（8位二进制数）为基本单位来处理数据，字符流以字符为单位处理数据。
- **使用场景不同**。字节流操作可以所有类型的数据，包括文本数据，和**非文本数据**如图片、音频等；而字符流只适用于处理文本数据。
- **关于性能方面**。因为字节流不处理字符编码，所以处理大量文本数据时可能不如字符流高效；而字符流使用到**内存缓冲区**处理文本数据可以优化读写操作。

## 5. 转换流

> ***面试官：你刚刚提到转换流把字节输入流转换成字符输入流，可不可以倒过来？***

hhh，JDK没有提供这样的方法。

是这样的，虽然字节流比字符流的**使用范围更广些**，但字符流比字节流**操作方便**，所以字符流是一个很方便的流了，没有必要把一个方便的流转换成一个不方便的流。
