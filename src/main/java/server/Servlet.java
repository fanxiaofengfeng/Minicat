package server;

/**
 * @author fanxiaofeng
 * @version 1.0
 * @date 2023/7/26
 * @description
 */
public interface Servlet {
    void init() throws Exception;

    void destroy() throws Exception;

    void service(Request request, Response response) throws Exception;

}
