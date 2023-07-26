package server;

/**
 * @author fanxiaofeng
 * @version 1.0
 * @date 2023/7/25
 * @description
 */
public class HttpProtocolUtil {
    public static String getHttpHeader200(long contentLength) {
        return "HTTP/1.1 200 OK \n" +
                "Content-Type: text/html \n" +
                "Content-Length: "+contentLength+ "\n" +
                "\r\n";}

    public static String getHttpHeader404() {
        String str404 = "<h1>404 Not Found</h1>";
        return "HTTP/1.1 404 NOT Found \n" +
                "Content-Type: test/html \n" +
                "Content-Length: "+str404.getBytes().length+ "\n" +
                "\r\n" + str404;}
}
