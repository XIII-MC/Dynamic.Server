package com.xiii.dynamic.server;

import com.xiii.dynamic.server.commands.ServerCreation;
import com.xiii.dynamic.server.managers.ServerManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;

public final class DynamicServer extends Plugin {

    private ServerManager serverManager;

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "Starting initialization...");

        serverManager = new ServerManager(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ServerCreation("dcs.createServer", this));

        this.getLogger().log(Level.INFO, "Plugin loaded!");
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO, "Unloaded Dynamic.Server, GoodBye!");
    }

    public ServerManager getServerManager() {
        return serverManager;
    }
}
