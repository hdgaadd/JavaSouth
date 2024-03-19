package org.codeman.utils;

import org.codeman.aspects.Aspect;
import org.codeman.handler.MyInvocationHandler;
import org.codeman.handler.MyInvocationHandlerImpl;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// 创建替代类
public class ProxyUtil {
    static String rt = "\r\t";

    // 把代理类实现和切面封装到JdkInterceptor
    public static <T> Object proxy(T target, Class<? extends Aspect> aspect) throws IllegalAccessException, InstantiationException {
        return ProxyUtil.newProxyInstance(new JavaClassLoader(), new MyInvocationHandlerImpl(target, aspect.newInstance()), target.getClass().getInterfaces()[0]);//传递target的接口方法
    } // JdkInterceptor的构造方法是对象，所以要把.class变为对象：aspect.newInstance()


    // 创建代替类
    public static <T> T newProxyInstance(JavaClassLoader javaClassLoader, MyInvocationHandler invocationHandler, Class<?> classInfo) {
        try {

            // 1.创建代理类$Proxy0.java源码文件,写入到硬盘中..
            Method[] methods = classInfo.getMethods();
            String proxyClass = "package org.codeman.utils;" + rt
                    + "import java.lang.reflect.Method;" + rt
                    + "import org.codeman.handler.MyInvocationHandler;" + rt
                    + "public class $Proxy0 implements " + classInfo.getName() + "{" + rt
                    + "MyInvocationHandler h;" + rt
                    + "public $Proxy0(MyInvocationHandler h)" + "{" + rt
                    + "this.h= h;" + rt + "}"
                    + getMethodString(methods, classInfo) + rt + "}";
            // 2. 写入到到本地文件中..
            File file = new File("d:/code");
            if (!file.exists()) {
                file.mkdirs();
            }
            String filename = "d:/code/$Proxy0.java";
            File f = new File(filename);
            FileWriter fw = new FileWriter(f);
            fw.write(proxyClass);
            fw.flush();
            fw.close();
            // 3. 将源代码编译成class文件
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);
            Iterable units = fileMgr.getJavaFileObjects(filename);
            JavaCompiler.CompilationTask t = compiler.getTask(null, fileMgr, null, null, null, units);
            t.call();
            fileMgr.close();
            // 4.使用classLoader 加载到内存中..
            Class<?> $Proxy0 = javaClassLoader.findClass("$Proxy0");
            // 5.指明初始化有参数构造函数
            Constructor<?> constructor =
                    $Proxy0.getConstructor(MyInvocationHandler.class);
            //把MyExtJdkInvocationHandler封装到$Proxy0的class文件中
            Object o = constructor.newInstance(invocationHandler);
            return (T) o;//返回$Proxy0对象

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMethodString(Method[] methods, Class intf) {
        String proxyMe = "";
        for (Method method : methods) {
            proxyMe += "public void " + method.getName() + "() throws Throwable {" + rt
                    + "Method md= " + intf.getName() + ".class.getMethod(\"" + method.getName()
                    + "\",new Class[]{});" + rt
                    + "this.h.invoke(this,md,null);" + rt + "}" + rt;
        }
        return proxyMe;
    }
}
