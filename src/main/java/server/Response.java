package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * HTTP 响应对象，用于处理服务端返回的HTTP响应信息。
 *
 * @version 1.0
 * @date 2023/7/25
 */
public class Response {

    private OutputStream outputStream;

    /**
     * 构造一个 Response 对象，并指定输出流。
     *
     * @param outputStream 响应输出流，用于向客户端发送响应内容
     */
    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 默认构造方法。
     */
    public Response() {
    }

    /**
     * 将指定的内容作为HTTP响应输出到客户端。
     *
     * @param content 响应内容
     * @throws IOException 如果输出过程中发生错误，则抛出IOException
     */
    public void output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }

    /**
     * 输出指定路径的HTML静态资源文件内容。
     * 如果资源文件存在，则将其作为HTTP响应输出到客户端；
     * 如果资源文件不存在，则返回404 NOT FOUND错误页面。
     *
     * @param path 静态资源文件的路径
     * @throws IOException 如果输出过程中发生错误，则抛出IOException
     */
    public void outputHtml(String path) throws IOException {
        // 获取静态资源文件的绝对路径
        String absoluteResourcePath = StaticResourceUtil.getAbsolutePath(path);

        // 输入静态资源文件
        File file = new File(absoluteResourcePath);
        if (file.exists() && file.isFile()) {
            // 输出静态资源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file),outputStream);
        } else {
            // 输出404 NOT FOUND错误页面
            output(HttpProtocolUtil.getHttpHeader404());
        }
    }
}
