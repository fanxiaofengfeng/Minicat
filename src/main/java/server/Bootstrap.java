package server;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author fanxiaofeng
 * @version 1.0
 * @date 2023/7/25
 * @description
 */
public class Bootstrap {

    private final Map<String, HttpServlet> servletMap = new HashMap<>();

    private int port = 8080;

    /**
     * Minicat 的程序启动入口
     *
     * @param args
     */
    public static void main(String[] args) {
        //启动 Minicat
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void start() {

        /**
         * 完成Mincat 1.0版本
         * 需求：浏览器请求http://localhost:8080,返回⼀个固定的字符串到⻚⾯"Hello Minicat!"
         */
        try {
            //创建一个 ServerSocket 实例，监听指定端口
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("======>>>Minicat start on port " + port);
            //不断监听客户端请求
            /*  while (true) {
                Socket socket = serverSocket.accept();
                //获取输出流，向客户端发送响应
                OutputStream outputStream = socket.getOutputStream();
                String data = "hello MInicat";
                //将构建好的HTTP响应头部和响应体通过输出流 outputStream 发送给客户端（浏览器）
                // 浏览器收到这个响应后，会根据响应头部中的Content-Type来解析并显示相应的内容
                String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
                outputStream.write(responseText.getBytes());
                //关闭与客户端的连接
                socket.close();
            }*/
            /**
             * 完成Minicat 2.0版本
             * 需求：封装Request和Response对象，返回html静态资源文件
             */

            loadServlet();

            /**
             * 线程池优化性能
             */
            ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(50);
            RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 50, 100L,
                    TimeUnit.SECONDS, queue, Executors.defaultThreadFactory(), handler);


            while (true) {
                Socket socket = serverSocket.accept();
                RequestProcessor requestProcessor = new RequestProcessor(servletMap, socket);
                //requestProcessor.start();
                threadPoolExecutor.execute(requestProcessor);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadServlet() throws Exception {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();

        Document document = saxReader.read(resourceAsStream);
        Element rootElement = document.getRootElement();
        List<Element> selectNodes = rootElement.selectNodes("//servlet");

        for (Element element : selectNodes) {
            Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
            String servletName = servletNameElement.getStringValue();
            Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
            String servletClass = servletClassElement.getStringValue();

            Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");

            String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
            servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
        }

    }
}
