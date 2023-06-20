# Dynamic.Server
Dynamically create servers for your Bungeecord proxy under 10 seconds!

## What is Dynamic.Server ?

Dynamic.Server is a plugin made for Bungeecord (not Velocity nor Waterfall for now...), it can help you create servers and automaticaly configure them to be linked to your proxy, no need to pre download the server software you want or waste time searching for a version you want, Dynamic.Server can automaticaly download, install and configure (or not) a majority of server softwares and version like Vanilla, Spigot, Bukkit or even Paper.

## How to use Dynamic.Server ?

First you will need to have a Bungeecord proxy with a graphical interface (e.g. through a terminal or cmd)
Simply drop the jar file in the plugins folder of your proxy and restart it (**RESTART Not greload!**)
After that Dynamic.Server is ready for use, no configuration required!

As of Dynamic.Server 1.1.0 there's only 1 command:

`dcs.createServer <Xmx> <Name> <Server_software> <Server_version> <auto_config>`

Xmx is the maximum memory you will allocate to the server, the value to enter is in MB.
Name is the name you wanna give you server, it will be used to connect to your server but also as the folder name to store your server.
Server_software is the software you wanna use for your server. (e.g. Vanilla, Spigot, Bukkit or Paper)
Server_version is the version you want. (e.g. 1.8.8, 1.17, 1.20.1) Alpha, pre-releases and old builds are supported for Vanilla.
auto_config is a boolean (true/false), this will decide if you want or not to configure and link the server to your proxy. If set to false, basic configuration will be done such as the EULA however if set to true the server's online-mode and server-port will be configured, the server will also be added to the proxy's config.yml.

### Example:

`dcs.createServer 1024 Survival Paper 1.12.2 true`

This command will create a server named "Survival" with a maximum RAM allocation of 1024 MB running on Paper version 1.12.2 and linked to the local proxy on any port available after 25565.

### Custom server softwares

Since Dynamic.Server v1.0.1 you can now use your own server softwares! To do so you simply need to set the server_software argument to "Custom", followed by a **direct** download link to your server's software jar file. Example:

`dcs.createServer 512 Purpur_1.20.1 Custom https://api.purpurmc.org/v2/purpur/1.20.1/latest/download true`

This will create a new server with a maximum RAM allocation of 512 MB, the server will be named "Purpur_1.20.1", we'll ask Dynamic.Server to download our custom server jar by putting "Custom" as the server software that is then followed by the direct download link, finally the server will automaticaly be configured to be added to our proxy.

### Distant servers & Ports

With Dynamic.Server v1.1.0 you can now link servers that are on a different network, you can now also choose from which port should the scan start. Example:

`dcs.createServer 2048 Vanilla_1.7.10 Vanilla 1.7.10 true 206.190.175.42 10`

This will create a server with 2048 MB of maximum RAM allocation called "Vanilla_1.7.10" running on Vanilla v1.7.10, the server will be automaticaly configured to be linked to the proxy, the **public** IP of the distant server will be 206.190.175.42 and it will start searching for available ports starting at port 10 until finding an available one.

## Future features

- Zip distant servers folder and don't autostart them
- Add an autostart after install option
- Add a server.properties preset option
- Support Velocity & Waterfall
- Control and manage the different servers connected to the proxy
- Access servers terminal on the same terminal as the proxy's
- Add/Remove an existing server from the proxy
- Delete/update servers on the proxy
