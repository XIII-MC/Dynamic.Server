package com.xiii.dynamic.server.commands.proxy;

import com.xiii.dynamic.server.DynamicServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class FixProxy extends Command {

    private final DynamicServer instance;

    public FixProxy(final String name, final DynamicServer instance) {
        super(name);
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {


        if (sender.hasPermission("Dynamic.Server.FixProxy")) {

            try {
                instance.getProxyManager().fixProxy();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
