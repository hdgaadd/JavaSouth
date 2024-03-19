package org.codeman.aspects;

public class LikeAspect extends SimpleAspect{

    public void before(){
        System.out.println("------likeDouDou------");
    }

    public void after(){
        System.out.println("------likeWeiWei------");
    }

}
