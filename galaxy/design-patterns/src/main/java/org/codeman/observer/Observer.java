package org.codeman.observer;
import java.util.ArrayList;
import java.util.List;

//观察者：实现注册与通知
class Child{
    String name;
    String message="null";
    public void setMessage(String message) {
        this.message = message;
    }
    public String toString(){
        return name+"接收到妈妈的消息有："+message;
    }
};

class A extends Child{public A(){name="孩子A";}};

class B extends Child{public B(){name="孩子B";}};

class C extends Child{public C(){name="孩子C";}};

class D extends Child{public D(){name="孩子D";}};

public class Observer {
    public static void main(String[] args) {
        A a=new A();  //孩子A未注册，不会接收到通知
        List<Child> list= new ArrayList<Child>(){{  //孩子B、孩子C在List集合里注册
            add(new B());
            add(new C());
        }};
        for(Child child:list){
            child.setMessage("吃饭了");   //妈妈有新消息，即可通过遍历List集合，确保在在List集合里注册过的孩子都会接收到通知
        }
        D d = new D();
        list.add(d);//孩子D在妈妈通知后再注册，先前的消息接收不到
        System.out.println(list);
    }
}
