package server;

import java.io.IOException;

/**
 * @author fanxiaofeng
 * @version 1.0
 * @date 2023/7/26
 * @description
 */
public abstract class HttpServlet implements Servlet{

    public abstract void doGet(Request request, Response response) throws IOException;
    public abstract void doPost(Request request, Response response) throws IOException;


    @Override
    public void service(Request request, Response response) throws Exception {
        Thread.sleep(1000000);
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        }else {
            doPost(request,response);
        }

    }
}
