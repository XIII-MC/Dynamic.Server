
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        final Charset charset = StandardCharsets.UTF_8;
        final Path path = Paths.get("D:\\Bureau\\Dynamic.Server\\config.yml"); //proxy config file
        final String content = new String(Files.readAllBytes(path), charset);
        System.out.println(content);
        System.out.println("Contains: " + content.contains("localhost:25565"));
    }
}
