# Dynamic.Server
Dynamically create servers for your Bungeecord proxy under 10 seconds!

## What is Dynamic.Server ?

Dynamic.Server is a plugin made for Bungeecord (not Velocity nor Waterfall for now...), it can help you create servers and automaticaly configure them to be linked to your proxy, no need to pre download the server software you want or waste time searching for a version you want, Dynamic.Server can automaticaly download, install and configure (or not) a majority of server softwares and version like Vanilla, Spigot, Bukkit or even Paper.
**For now Dynamic.Server only works if the proxy and the server run on the same machine, tested under Windows 10.**

## How to use Dynamic.Server ?

First you will need to have a Bungeecord proxy with a graphical interface (e.g. through a terminal or cmd)
Simply drop the jar file in the plugins folder of your proxy and restart it (**RESTART Not greload!**)
After that Dynamic.Server is ready for use, no configuration required!

As of Dynamic.Server 1.0.0 there's only 1 command:

`dcs.createServer <Xmx> <Name> <Server_software> <Server_version> <auto_config>`

Xmx is the maximum memory you will allocate to the server, the value to enter is in MB.
Name is the name you wanna give you server, it will be used to connect to your server but also as the folder name to store your server.
Server_software is the software you wanna use for your server. (e.g. Vanilla, Spigot, Bukkit or Paper)
Server_version is the version you want. (e.g. 1.8.8, 1.17, 1.20.1) Alpha, pre-releases and old builds are supported for Vanilla.
auto_config is a boolean (true/false), this will decide if you want or not to configure and link the server to your proxy. If set to false, basic configuration will be done such as the EULA however if set to true the server's online-mode and server-port will be configured, the server will also be added to the proxy's config.yml.

### Example:

`dcs.createServer 1024 Survival Paper 1.12.2 true`

This command will create a server named "Survival" with a maximum RAM allocation of 1024 MB running on Paper version 1.12.2 and linked to the proxy.

## Future features

- Custom server software possibility
- Custom server IP to link servers on different machines
- Check if port is not already used based on proxy's config
- Support Velocity & Waterfall
- Control and manage the different servers connected to the proxy
- Access servers terminal on the same terminal as the proxy's
