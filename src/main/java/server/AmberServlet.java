package server;

import java.io.IOException;

/**
 * @author fanxiaofeng
 * @version 1.0
 * @date 2023/7/26
 * @description
 */
public class AmberServlet extends HttpServlet {

    @Override
    public void doGet(Request request, Response response) throws IOException {
        String content = "<h1>AmberServlet get</h1>";
        response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length) + content);

    }

    @Override
    public void doPost(Request request, Response response) throws IOException {
        String content = "<h1>AmberServlet post</h1>";
        response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length) + content);


    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }
}
