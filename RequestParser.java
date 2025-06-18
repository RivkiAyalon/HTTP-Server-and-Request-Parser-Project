package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null || line.isEmpty())
            return null;

        String[] parts = line.split(" ");
        String command = parts[0];
        String uri = parts[1];

        // Parse URI
        String uriOnly = uri;
        String paramString = null;
        if (uri.contains("?")) {
            int idx = uri.indexOf('?');
            uriOnly = uri.substring(0, idx);
            paramString = uri.substring(idx + 1);
        }

        String[] uriSegments = Arrays.stream(uriOnly.split("/"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        Map<String, String> params = new HashMap<>();
        if (paramString != null) {
            for (String pair : paramString.split("&")) {
                String[] kv = pair.split("=");
                if (kv.length == 2) {
                    params.put(kv[0], kv[1]);
                }
            }
        }

        // Read headers
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("content-length:")) {
                // ignore it – do not rely on Content-Length
            }
        }

        // Read parameters from body (optional)
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] kv = line.split("=");
            if (kv.length == 2) {
                params.put(kv[0].trim(), kv[1].trim());
            }
        }

        // Read remaining body content
        StringBuilder contentBuilder = new StringBuilder();
        while (reader.ready()) {
            contentBuilder.append((char) reader.read());
        }

        byte[] content = contentBuilder.toString().getBytes();

        return new RequestInfo(command, uri, uriSegments, params, content);
    }

    public static class RequestInfo {
        private final String httpCommand;
        private final String uri;
        private final String[] uriSegments;
        private final Map<String, String> parameters;
        private final byte[] content;

        public RequestInfo(String httpCommand, String uri, String[] uriSegments,
                           Map<String, String> parameters, byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriSegments = uriSegments;
            this.parameters = parameters;
            this.content = content;
        }

        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriSegments;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
