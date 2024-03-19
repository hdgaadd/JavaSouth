package org.codeman;

import org.codeman.component.RpcServer;
import org.codeman.impl.RpcInterfaceImpl;

public class ServiceApplication {

    public static void main(String[] args) {
        RpcInterface helloService = new RpcInterfaceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService, 9939);
    }

}
