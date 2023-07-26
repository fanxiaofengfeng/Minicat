package server;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @author fanxiaofeng
 * @version 1.0
 * @date 2023/7/26
 * @description
 */
public class RequestProcessor extends Thread {
    private Map<String, HttpServlet> servletMap;

    private Socket socket;

    public RequestProcessor(Map<String, HttpServlet> servletMap, Socket socket) {
        this.servletMap = servletMap;
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            if (servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            } else {
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request, response);
            }

            response.outputHtml(request.getUrl());
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
