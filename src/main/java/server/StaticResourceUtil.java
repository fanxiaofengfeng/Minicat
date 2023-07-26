package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 静态资源工具类
 * 提供获取静态资源的绝对路径以及输出静态资源内容的方法
 *
 * @version 1.0
 * @date 2023/7/25
 * @since 1.0
 */
public class StaticResourceUtil {

    /**
     * 获取静态资源的绝对路径
     *
     *
     * 用于处理 HTTP 响应中的静态资源的实用工具类。
     * 该类提供了将静态资源输出到输出流的方法。
     * 它还演示了刷新输出流的用法。
     *
     * 在输出静态资源内容时，读取输入流的数据并输出到输出流。如果输出的数据大小较大，为了避免
     * 一次性读取大量数据，可以使用缓冲区分批次读取。每次读取缓冲区大小的数据并写入输出流，
     * 然后刷新输出流，以确保数据及时发送给客户端。当输出的数据量较小时，刷新输出流可能并不
     * 明显，但在输出大数据时，刷新输出流会显著影响数据发送的实时性。
     *
     *
     * @param path 相对路径
     * @return 静态资源的绝对路径
     */
    public static String getAbsolutePath(String path) {
        // 获取类所在的包的绝对路径
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        //将Windows路径分隔符转换为URL路径分隔符
        //在Windows系统中，文件路径使用反斜杠 \ 作为分隔符，而URL路径使用正斜杠 / 作为分隔符。
        //在Java中，字符串中的反斜杠 \ 需要进行转义，因此在使用字符串表示路径时，需要使用两个反斜杠 \\。
        //假设 absolutePath 是 C:\\path\\to\\file，那么经过 replaceAll("\\\\", "/") 转换后，absolutePath 将变为 C:/path/to/file，符合URL路径的规范。
        //这样做在处理文件路径时更加方便，可以避免因操作系统差异而导致的路径问题。
        return absolutePath.replaceAll("\\\\", "/") + path;
    }

    /**
     * 输出静态资源内容到输出流
     *
     * @param inputStream 静态资源的输入流
     * @param outputStream 输出流，用于写入静态资源内容
     * @throws IOException 如果读取或写入静态资源内容时发生IO异常
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }

        int resourceSize = count;

        // 输出 HTTP 响应头部，指定静态资源的大小和内容类型
        outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes());

        // 读取内容输出
        long written = 0;
        int byteSize = 1024;
        byte[] bytes = new byte[byteSize];

        while (written < resourceSize) {
            if (written + byteSize > resourceSize) {
                byteSize = (int) (resourceSize - written);
                bytes = new byte[byteSize];
            }
            inputStream.read(bytes);
            outputStream.write(bytes);

            // 刷新输出流，将已写入的数据发送给客户端
            /**
             * 注意：在调用 `flush()` 方法后，输出流将把数据发送给客户端，并且内部缓冲区将被清空。
             * 输出流保持打开状态，如果需要，可以继续写入数据。记得在所有输出操作完成后关闭输出流，
             * 以释放相关资源。
             */
            outputStream.flush();

            written += byteSize;
        }
    }}
