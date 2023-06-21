package com.xiii.dynamic.server.commands;

import com.xiii.dynamic.server.DynamicServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;
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

        if (sender.hasPermission("Dynamic.Server.ServerCreation")) {

            if (args.length >= 5) {

                final long startTime = System.currentTimeMillis();
                if (args.length >= 7) tempStartPort = Integer.parseInt(args[6]);
                if (args.length >= 6) tempDistantIP = args[5];

                try {
                    instance.getServerManager().createServer(Integer.parseInt(args[0]), args[1], args[2], args[3], Boolean.parseBoolean(args[4]), tempDistantIP, tempStartPort);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                instance.getLogger().log(Level.INFO, "Process finished in " + (System.currentTimeMillis() - startTime) + "ms!");

            } else sender.sendMessage("Wrong arguments. Usage: dcs.createServer <Xmx> <Name> <Server_software> <Server_version> <auto_config> <distant_ip> <start_port>");
        }
    }
}
