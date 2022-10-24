package org.codeman.build;

import org.codeman.build.builder.LinuxBuilder;
import org.codeman.build.builder.WindowsBuilder;
import org.codeman.build.component.ServerDirector;

/**
 * @author hdgaadd
 * created on 2022/10/04
 */
public class Client {
    public static void main(String[] args) {
        ServerDirector director = new ServerDirector();

        System.out.println(director.construct(new WindowsBuilder()));
        System.out.println(director.construct(new LinuxBuilder()));
    }
}
