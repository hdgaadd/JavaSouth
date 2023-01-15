package org.codeman.build;

import lombok.var;
import org.codeman.build.builder.LinuxBuilder;
import org.codeman.build.builder.WindowsBuilder;
import org.codeman.build.component.Server;
import org.codeman.build.component.ServerDirector;

/**
 * @author hdgaadd
 * created on 2022/10/04
 *
 * process
 * - 通过建造者模式，实现组件可以自定义组装
 */
public class Client {
    public static void main(String[] args) {
        ServerDirector director = new ServerDirector();

        var windows = director.construct(new WindowsBuilder());
        var linux = director.construct(new LinuxBuilder());
        System.out.println(windows);
        System.out.println(linux);
    }
}
