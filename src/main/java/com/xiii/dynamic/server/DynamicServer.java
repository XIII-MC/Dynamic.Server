package com.xiii.dynamic.server;

import com.xiii.dynamic.server.commands.proxy.*;
import com.xiii.dynamic.server.commands.server.CreateServer;
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

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SetDefaultServer("dcs.setDefaultServer", this));

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ToggleIPForward("dcs.toggleIPForward", this));

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ToggleOnlineMode("dcs.toggleOnlineMode", this));

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ChangeProxyPort("dcs.changeProxyPort", this));

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new RemoveForcedSettings("dcs.removeForcedSettings", this));

        this.getLogger().log(Level.INFO, "Plugin loaded!");
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO, "Unloaded Dynamic.Server, thank you and goodbye!");
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public ProxyManager getProxyManager() {
        return proxyManager;
    }
}
