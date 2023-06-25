package com.xiii.dynamic.server.commands.server;

import com.xiii.dynamic.server.DynamicServer;
import com.xiii.dynamic.server.utils.FileUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class CreateServer extends Command {

    private final DynamicServer instance;
    private int tempStartPort = -1;
    private String tempDistantIP = "DEFAULT";

    public CreateServer(final String name, final DynamicServer instance) {
        super(name);
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender.hasPermission("Dynamic.Server.CreateServer")) {

            if (args.length >= 5) {

                if (args[4].equalsIgnoreCase("false") || args[4].equalsIgnoreCase("true")) {

                    try {
                        if (args.length >= 7) tempStartPort = Integer.parseInt(args[6]);
                    } catch (final NumberFormatException e) {
                        instance.getLogger().log(Level.SEVERE, "Wrong arguments! Usage: dcs.createServer <Xmx> <Name> <Server_software> <Server_version> <auto_config> (distant_ip) !>>>(start_port)<<<!");
                    }
                    if (args.length >= 6) tempDistantIP = args[5];

                    final long startTime = System.currentTimeMillis();

                    try {
                        instance.getServerManager().createServer(Integer.parseInt(args[0]), args[1], args[2], args[3], Boolean.parseBoolean(args[4]), tempDistantIP, tempStartPort);
                    } catch (final NumberFormatException e) {
                        instance.getLogger().log(Level.SEVERE, "Wrong arguments! Usage: dcs.createServer !>>><Xmx><<<! <Name> <Server_software> <Server_version> <auto_config> (distant_ip) (start_port)");
                    } catch (final IOException e) {
                        e.printStackTrace();
                        instance.getLogger().log(Level.SEVERE, "Process exited due to an exception! Canceling server creation...");

                        try {
                            FileUtils.deleteDirectory(new File(args[1]));
                        } catch (final IOException ex) {}
                    }
                    instance.getLogger().log(Level.INFO, "Process finished in " + (System.currentTimeMillis() - startTime) + "ms!");
                } else instance.getLogger().log(Level.SEVERE, "Wrong arguments! Usage: dcs.createServer <Xmx> <Name> <Server_software> <Server_version> !>>><auto_config><<<! (distant_ip) (start_port)");
            } else instance.getLogger().log(Level.SEVERE, "Wrong arguments! Usage: dcs.createServer <Xmx> <Name> <Server_software> <Server_version> <auto_config> (distant_ip) (start_port)");
            tempStartPort = -1;
            tempDistantIP = null;
        }
    }
}
