package org.codeman.servlet;

import org.codeman.http.Request;
import org.codeman.http.Response;

/**
 * @author hdgaadd
 * created on 2022/04/27
 */
public class FirstServlet extends Servlet {
    @Override
    public void doGet(Request xcRequest, Response xcResponse) throws Exception {
        this.doPost(xcRequest, xcResponse);
    }

    @Override
    public void doPost(Request xcRequest, Response xcResponse) throws Exception {
        xcResponse.write("this is the first servlet from nio");
    }
}
