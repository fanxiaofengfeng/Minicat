package server;

import java.io.IOException;
import java.io.InputStream;

/**
 * HTTP 请求对象，用于封装客户端发送的HTTP请求信息。
 * 包含请求方式、URL和请求输入流。
 *
 * @version 1.0
 * @date 2023/7/25
 */
public class Request {
    private String method; // 请求方式，例如 GET 或 POST

    private String url; // 请求的URL，例如 /index.html

    private InputStream inputStream; // 请求输入流，用于读取请求数据


    public Request(InputStream inputStream) throws IOException {
        this.inputStream =inputStream;

        //从输入流中获取请求信息
        int count= 0;
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] bytes = new byte[count];
        inputStream.read(bytes);

        String inputStr = new String(bytes);
        String firstLineStr = inputStr.split("\\n")[0]; //GET /index.html HTTP/1.1
        String[] strings = firstLineStr.split(" ");
        this.method = strings[0];
        this.url = strings[1];

        System.out.println("=====>>>method:"+ method);
        System.out.println("=====>>>url:"+ url);

    }

    /**
     * 获取请求方式。
     *
     * @return 请求方式，例如 GET 或 POST
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置请求方式。
     *
     * @param method 请求方式，例如 GET 或 POST
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 获取请求的URL。
     *
     * @return 请求的URL，例如 /index.html
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置请求的URL。
     *
     * @param url 请求的URL，例如 /index.html
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取请求输入流。
     *
     * @return 请求输入流，用于读取请求数据
     */
    public InputStream getInputStream() {
        return inputStream;
    }

}
