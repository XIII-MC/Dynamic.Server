package com.xiii.dynamic.server.commands.proxy;

import com.xiii.dynamic.server.DynamicServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class ToggleIPForward extends Command {

    private final DynamicServer instance;

    public ToggleIPForward(final String name, final DynamicServer instance) {
        super(name);
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender.hasPermission("Dynamic.Server.ToggleIPForward")) {

            if (args.length == 1) {

                //Backup the file in case something fails
                final Path path = Paths.get("config.yml"); //proxy config file
                String content = null;
                try {
                    content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                } catch (final IOException ignored) {}

                final long startTime = System.currentTimeMillis();
                instance.getLogger().log(Level.INFO, "Changing IP forwarding! Please wait...");

                try {
                    instance.getProxyManager().toggleIPForward(Boolean.parseBoolean(args[0]));
                } catch (final IOException e) {
                    e.printStackTrace();
                    instance.getLogger().log(Level.SEVERE, "Process exited due to an exception! Rolling back changes...");
                    new File("config.yml").delete();
                    try {
                        final BufferedWriter backupConfigFile = new BufferedWriter(new FileWriter("config.yml"));
                        backupConfigFile.write(content);
                        backupConfigFile.flush();
                        backupConfigFile.close();
                    } catch (final IOException ignored) {}
                }

                instance.getLogger().log(Level.INFO, "Process finished in " + (System.currentTimeMillis() - startTime) + "ms!");
            } else instance.getLogger().log(Level.SEVERE, "Wrong arguments! Usage: dcs.toggleIPForward <true/false>");
        }
    }
}
