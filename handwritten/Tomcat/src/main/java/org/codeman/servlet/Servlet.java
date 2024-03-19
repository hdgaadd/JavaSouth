package org.codeman.servlet;

import org.codeman.http.Request;
import org.codeman.http.Response;

/**
 * @author hdgaadd
 * created on 2022/04/27
 */
public abstract class Servlet {

    public void service(Request xcRequest, Response xcResponse) throws Exception{
        if ("GET".equalsIgnoreCase(xcRequest.getMethod())) {
            doGet(xcRequest, xcResponse);
        } else {
            doPost(xcRequest, xcResponse);
        }
    }

    public abstract void doGet(Request xcRequest, Response xcResponse) throws Exception;

    public abstract void doPost(Request xcRequest, Response xcResponse) throws Exception;
}
