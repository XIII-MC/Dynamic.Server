package com.xiii.dynamic.server.managers;

import com.xiii.dynamic.server.DynamicServer;
import com.xiii.dynamic.server.utils.FileUtils;
import com.xiii.dynamic.server.utils.HTTPUtils;
import com.xiii.dynamic.server.utils.MiscUtils;
import org.zeroturnaround.zip.ZipUtil;
import sun.net.ConnectionResetException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;

public class ServerManager {

    private final DynamicServer instance;

    private boolean isVanillaProxy = false;

    public ServerManager(final DynamicServer instance) {
        this.instance = instance;
    }

    public void createServer(final int xmxRamMB, final String serverName, final String serverSoftware, final String serverVersion, final boolean autoConfig, final boolean autoStart, String distantIP, int startPort) throws IOException {

        if (Objects.equals(distantIP, "DEFAULT")) {
            distantIP = "localhost";
            instance.getLogger().log(Level.WARNING, "No distant IP specified, defaulting to localhost.");
        }
        if (startPort == -1) {
            startPort = 25565;
            instance.getLogger().log(Level.WARNING, "No start port specified, defaulting to 25565");
        }
        final Path serverFolder = Paths.get(serverName);
        final Charset charset = StandardCharsets.UTF_8;
        String downloadURL = "UNKNOWN_VERSION";

        String content = new String(Files.readAllBytes(Paths.get("config.yml")), charset);
        final String servers = content.substring(content.indexOf("servers:"));

        //Check if serverSoftware exists and download it
        switch (serverSoftware) {

            case "Paper":
                downloadURL = HTTPUtils.getPaperDownload(serverVersion);

                break;

            case "Spigot":
                downloadURL = HTTPUtils.getSpigotDownload(serverVersion);

                break;

            case "Vanilla":
                downloadURL = HTTPUtils.getVanillaDownload(serverVersion);

                break;

            case "Bukkit":
                downloadURL = HTTPUtils.getBukkitDownload(serverVersion);

                break;

            case "Custom":
                downloadURL = serverVersion;

                break;
        }

        if (downloadURL.equalsIgnoreCase("UNKNOWN_VERSION")) {
            instance.getLogger().log(Level.SEVERE, serverVersion + " does not exist as a " + serverSoftware + " version! Exiting...");
            return;
        }

        if (servers.contains(serverName + ":")) {
            instance.getLogger().log(Level.SEVERE, "A server with the same name is already registered in the config! Exiting...");
            return;
        }

        if (!MiscUtils.containsLetters(serverName)) {
            instance.getLogger().log(Level.SEVERE, "The server name must contain at least 1 letter! Exiting...");
            return;
        }

        //Create server's folder -> final String serverName
        try {
            Files.createDirectory(serverFolder);
        } catch (final IOException e) {
            endSetup(serverName, "A folder with that same name already exists! Exiting...");
            return;
        }

        if (serverSoftware.equalsIgnoreCase("Vanilla") && autoConfig) {
            isVanillaProxy = true;
            downloadURL = "https://dev.me1312.net/jenkins/job/VanillaCord/lastSuccessfulBuild/artifact/artifacts/VanillaCord.jar";
            instance.getLogger().log(Level.WARNING, "Normal Vanilla cannot be linked to a proxy. Using VanillaCord.");
        }

        instance.getLogger().log(Level.INFO, "File found! Downloading...");
        try {
            HTTPUtils.downloadFileByURL(downloadURL, new File(serverName + "/server.jar"));
        } catch (final ConnectionResetException e) {
            endSetup(serverName, "Could not connect to the distant server (is the firewall blocking it?) (Connection Reset). Exiting...");
        }
        instance.getLogger().log(Level.INFO, "File downloaded! Installing...");

        //Create the out folder for VanillaCord
        if (isVanillaProxy) new File(serverName + "/out").mkdir();

        //Pre setup EULA file to boot up on first start
        final BufferedWriter eula = new BufferedWriter(new FileWriter(isVanillaProxy ? serverName + "/out/eula.txt" : serverName + "/eula.txt"));

        eula.write("eula=true");

        eula.flush();

        eula.close();

        //Setup run.bat/sh files
        String startUpCommand;

        //Vanilla needs a little special run.bat/sh
        if (isVanillaProxy) {
            startUpCommand = "@echo off" + System.lineSeparator() + "java -jar -Xmx" + xmxRamMB + "M" + " server.jar " + serverVersion;
        } else startUpCommand = "@echo off" + System.lineSeparator() + "java -jar -Xmx" + xmxRamMB + "M" + " server.jar";

        final BufferedWriter runBat = new BufferedWriter(new FileWriter(serverName + "/run.bat"));
        final BufferedWriter runSh = new BufferedWriter(new FileWriter(serverName + "/run.sh"));

        runBat.write(isVanillaProxy ? startUpCommand + System.lineSeparator() + "cd out" + System.lineSeparator() + "java -jar -Xmx" + xmxRamMB + "M " + serverVersion + "-bungee.jar" : startUpCommand);
        runSh.write(isVanillaProxy ? startUpCommand + System.lineSeparator() + "cd out" + System.lineSeparator() + "java -jar -Xmx" + xmxRamMB + "M " + serverVersion + "-bungee.jar" : startUpCommand);

        runBat.flush();
        runSh.flush();

        runBat.close();
        runSh.close();

        //Pre cache config file
        final Path path = Paths.get("config.yml"); //proxy config file
        content = new String(Files.readAllBytes(path), charset);

        //Pre gen a server.properties file, only online mode and port are required to link it to the proxy
        instance.getLogger().log(Level.INFO, "Installation finished! Configuring server...");

        //Find an available port
        boolean availablePort = false;
        int portNumber = startPort;
        for (int port = startPort; !availablePort; port++) {
            try (Socket ignored = new Socket(distantIP, port)) {
            } catch (final ConnectException e) {
                if (!content.contains(distantIP + ":" + port)) {
                    availablePort = true;
                    portNumber = port;
                }
            }
        }

        if (!availablePort) endSetup(serverName, "No available port found! Exiting...");

        final BufferedWriter serverPropertiesBw = new BufferedWriter(new FileWriter(isVanillaProxy ? serverName + "/out/server.properties" : serverName + "/server.properties"));

        serverPropertiesBw.write("server-port:" + portNumber + (autoConfig ? System.lineSeparator() + "online-mode=false" : ""));

        serverPropertiesBw.flush();

        serverPropertiesBw.close();

        //We won't go further if autoConfig is false, meaning they chose not to configure the server with their proxy
        if (autoConfig && (!serverSoftware.equalsIgnoreCase("Bukkit"))) {

            //Enable bungeecord in spigot.yml
            if (!serverSoftware.equalsIgnoreCase("Vanilla") && !serverSoftware.equalsIgnoreCase("Bukkit")) {
                final BufferedWriter spigotYml = new BufferedWriter(new FileWriter(serverName + "/spigot.yml"));

                spigotYml.write("settings:" + System.lineSeparator() + "  bungeecord: true");

                spigotYml.flush();

                spigotYml.close();
            }

            instance.getLogger().log(Level.INFO, "Server configuration done! Linking server to the proxy...");

            final String addServer = content.replaceAll("servers:", "servers:" + System.lineSeparator() + "  " + serverName + ":" + System.lineSeparator() + "    " + "motd: 'A Dynamic.Server!'" + System.lineSeparator() + "    " + "address: " + distantIP + ":" + portNumber + System.lineSeparator() + "    " + "restricted: false");

            Files.write(path, addServer.getBytes(charset));

            instance.getLogger().log(Level.INFO, "Proxy linked to the server!");
            instance.getLogger().log(Level.WARNING, "You will need to restart your Proxy in order to finish linking your server!");
        }

        if (autoConfig && (serverSoftware.equalsIgnoreCase("Bukkit"))) instance.getLogger().log(Level.WARNING, serverSoftware + " does not support BungeeCord! Please use Spigot, Paper or Vanilla.");

        //Zip folder to export it to the distant server
        if (distantIP != "localhost") {

            instance.getLogger().log(Level.INFO, "Exporting server to a zip file! Please wait...");

            if (Files.notExists(Paths.get("exported_servers"))) Files.createDirectory(Paths.get("exported_servers"));

            ZipUtil.pack(new File(serverName), new File("exported_servers/" + serverName + ".zip"));

            FileUtils.deleteDirectory(new File(serverName));

            instance.getLogger().log(Level.INFO, "Exporting done! The file was saved in the 'exported_servers' folder at the root of your proxy.");
        } else {

            if (autoStart) {

                instance.getLogger().log(Level.INFO, "Setup finished! Starting server...");

                startServer(serverName);
            } else instance.getLogger().log(Level.INFO, "Setup finished!");
        }
    }

    public void startServer(final String serverName) {
        try {
            Runtime.
                    getRuntime().
                    exec("cmd.exe /c start \"\" run.bat", null, new File(serverName));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void endSetup(final String serverName, final String errorMessage) {
        instance.getLogger().log(Level.SEVERE, errorMessage);
        new File(serverName).delete();
    }
}
