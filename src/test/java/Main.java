import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        final Path path = Paths.get("D:\\Bureau\\Dynamic.Server\\config.yml"); //proxy config file
        final String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        final String servers = content.substring(content.indexOf("servers:"));
        final Pattern r = Pattern.compile("(?<=address: ).*");
        final Matcher m = r.matcher(servers);

        while (m.find()) {
            final String port = m.group().substring(m.group().indexOf(":")).replace(":", "");
            final String ip = m.group().substring(0, m.group().indexOf(":"));
            System.out.println("ip=" + ip + " port=" + port);
        }
    }
}
