package com.xiii.dynamic.server.commands;

import com.xiii.dynamic.server.DynamicServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;
import java.util.logging.Level;

public class ServerCreation extends Command {

    private final DynamicServer instance;

    public ServerCreation(final String name, final DynamicServer instance) {
        super(name);
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender.hasPermission("Dynamic.Server.ServerCreation")) {

            if (args.length >= 5) {

                try {
                    instance.getServerManager().createServer(Integer.parseInt(args[0]), args[1], args[2], args[3], Boolean.parseBoolean(args[4]));
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                instance.getLogger().log(Level.INFO, "Done!");

            } else sender.sendMessage("Not enough arguments!");
        }
    }
}
