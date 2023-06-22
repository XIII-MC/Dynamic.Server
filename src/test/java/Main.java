
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final String ipForward = "true";

    public static void main(String[] args) throws IOException {
        final Path path = Paths.get("D:\\Bureau\\Dynamic.Server\\config.yml"); //proxy config file
        final String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        final String post = content.replaceAll("ip_forward: " + !Boolean.parseBoolean(ipForward), "ip_forward: " + String.valueOf(Boolean.parseBoolean(ipForward)));
        Files.write(path, post.getBytes(StandardCharsets.UTF_8));
    }
}
