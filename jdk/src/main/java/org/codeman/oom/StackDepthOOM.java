package org.codeman.oom;

/**
 * @author hdgaadd
 * created on 2022/12/23
 *
 * description: 栈深度溢出
 */
public class StackDepthOOM {

    public static void main(String[] args) {
        OverFlow.overFlow();
    }

    static class OverFlow {
        public static void overFlow() {
            OverFlow.overFlow();
        }
    }
}
