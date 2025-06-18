package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import test.RequestParser.RequestInfo;

public class MyHTTPServer extends Thread implements HTTPServer {
    int port;
    int nThreads;
    volatile boolean stop = false;

    ServerSocket serverSocket;
    ExecutorService pool;
    Map<String, Servlet> getServlets = new ConcurrentHashMap<>();
    Map<String, Servlet> postServlets = new ConcurrentHashMap<>();
    Map<String, Servlet> deleteServlets = new ConcurrentHashMap<>();

    public MyHTTPServer(int port, int nThreads) {
        this.port = port;
        this.nThreads = nThreads;
    }

    @Override
    public void addServlet(String httpCommand, String uri, Servlet s) {
        switch (httpCommand.toUpperCase()) {
            case "GET":
                getServlets.put(uri, s);
                break;
            case "POST":
                postServlets.put(uri, s);
                break;
            case "DELETE":
                deleteServlets.put(uri, s);
                break;
        }
    }

    @Override
    public void removeServlet(String httpCommand, String uri) {
        switch (httpCommand.toUpperCase()) {
            case "GET":
                getServlets.remove(uri);
                break;
            case "POST":
                postServlets.remove(uri);
                break;
            case "DELETE":
                deleteServlets.remove(uri);
                break;
        }
    }

    @Override
    public void start() {
        pool = Executors.newFixedThreadPool(nThreads);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.start();
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                serverSocket.setSoTimeout(1000); 
                Socket clientSocket = serverSocket.accept();
                pool.execute(() -> handleClient(clientSocket));
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                if (!stop)
                    e.printStackTrace();
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream toClient = clientSocket.getOutputStream()
        ) {
            RequestInfo ri = RequestParser.parseRequest(fromClient);
            Servlet servlet = findServlet(ri.getHttpCommand(), ri.getUri());
            if (servlet != null) {
                servlet.handle(ri, toClient);
            } else {
                toClient.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Servlet findServlet(String httpCommand, String uri) {
        Map<String, Servlet> map;
        switch (httpCommand.toUpperCase()) {
            case "GET":
                map = getServlets;
                break;
            case "POST":
                map = postServlets;
                break;
            case "DELETE":
                map = deleteServlets;
                break;
            default:
                return null;
        }

        return map.entrySet().stream()
            .filter(entry -> uri.startsWith(entry.getKey()))
            .max(Comparator.comparingInt(e -> e.getKey().length()))
            .map(Map.Entry::getValue)
            .orElse(null);
    }

    @Override
    public void close() {
        stop = true;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (pool != null) {
            pool.shutdownNow();
        }

        getServlets.values().forEach(this::safeClose);
        postServlets.values().forEach(this::safeClose);
        deleteServlets.values().forEach(this::safeClose);
    }

    private void safeClose(Servlet s) {
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

