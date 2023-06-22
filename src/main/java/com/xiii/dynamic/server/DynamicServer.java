package com.xiii.dynamic.server;

import com.xiii.dynamic.server.commands.ConfigureProxy;
import com.xiii.dynamic.server.commands.CreateServer;
import com.xiii.dynamic.server.managers.ProxyManager;
import com.xiii.dynamic.server.managers.ServerManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;

public final class DynamicServer extends Plugin {

    private ServerManager serverManager;
    private ProxyManager proxyManager;

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "Starting initialization...");

        serverManager = new ServerManager(this);

        proxyManager = new ProxyManager(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CreateServer("dcs.createServer", this));

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ConfigureProxy("dcs.configureProxy", this));

        this.getLogger().log(Level.INFO, "Plugin loaded!");
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO, "Unloaded Dynamic.Server, GoodBye!");
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public ProxyManager getProxyManager() {
        return proxyManager;
    }
}
