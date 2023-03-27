package cn.wulin.brace.examples.http.proxy;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ReverseProxy {
    private static final String TARGET_URL = "https://www.haohaoxiaohua.com/";
    private static final int PROXY_PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PROXY_PORT), 0);
        server.createContext("/", new ProxyHandler());
        server.start();
    }

    static class ProxyHandler implements HttpHandler {
        private final OkHttpClient client = new OkHttpClient();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                Request request = createRequest(exchange);
                Response response = client.newCall(request).execute();
                sendResponse(exchange, response);
            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0);
                exchange.close();
            }
        }

        private Request createRequest(HttpExchange exchange) {
            try {
				String requestUri = TARGET_URL + exchange.getRequestURI().toString();
				String method = exchange.getRequestMethod();

				Headers headers = Headers.of(exchange.getRequestHeaders().entrySet().stream()
				    .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().get(0))));

				Request.Builder requestBuilder = new Request.Builder().url(requestUri).headers(headers).method(method, null);

				if (exchange.getRequestMethod().equalsIgnoreCase("POST") || exchange.getRequestMethod().equalsIgnoreCase("PUT")) {
				    MediaType contentType = MediaType.parse("application/octet-stream");
				    byte[] bodyBytes = new byte[exchange.getRequestBody().available()];
				    exchange.getRequestBody().read(bodyBytes);
				    requestBuilder = requestBuilder.post(RequestBody.create(contentType, bodyBytes));
				}

				return requestBuilder.build();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
        }
        
        private void sendResponse(HttpExchange exchange, Response response) throws IOException {
            for (String key : response.headers().names()) {
                for (String value : response.headers().values(key)) {
                    exchange.getResponseHeaders().add(key, value);
                }
            }

            exchange.sendResponseHeaders(response.code(), 0);

            try (ResponseBody responseBody = response.body()) {
                if (responseBody != null) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    InputStream inputStream = responseBody.byteStream();
                    OutputStream outputStream = exchange.getResponseBody();

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }

            exchange.close();
        }


//        private void sendResponse(HttpExchange exchange, Response response) throws IOException {
//            for (String key : response.headers().names()) {
//                for (String value : response.headers().values(key)) {
//                    exchange.getResponseHeaders().add(key, value);
//                }
//            }
//
//            exchange.sendResponseHeaders(response.code(), 0);
//
//            try (ResponseBody responseBody = response.body()) {
//                if (responseBody != null) {
//                    responseBody.byteStream().transferTo(exchange.getResponseBody());
//                }
//            }
//
//            exchange.close();
//        }
    }
}
