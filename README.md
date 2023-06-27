# Dynamic.Server
Dynamically create servers for your Bungeecord proxy under 10 seconds!

## What is Dynamic.Server ?


Dynamic.Server is a plugin made for Bungeecord (not Velocity nor Waterfall for now...), it can do multiple tasks on your proxy such as: Configure your proxy, create new servers (Vanilla, Spigot, Bukkit, Paper and even Custom server jars!) and automaticaly download, install and configure them. It can automaticaly link new servers to the proxy (even Vanilla!), you can even create servers for distant non-local IPs!

## How to use Dynamic.Server ?

First you will need to have a Bungeecord proxy with a graphical interface (e.g. through a terminal or cmd)
Simply drop the jar file in the plugins folder of your proxy and restart it (**RESTART not greload!**)
After that Dynamic.Server is ready for use, no configuration required!

### Create a new server *(dcs.createServer)*<br /><br />

`dcs.createServer <Xmx> <Name> <Server_software> <Server_version> <auto_config> (distant_ip) (start_port)` <br /><br />

**Xmx**: The maximum memory allocation you will give to the server, the value is in megabytes (only the value needs to be entered, no need to put M or G). <br />
**Name**: The name you will give to the server, this name will be used for the server folder but also in the configuration files (e.g. Survival, My_Awesome_Server). <br />
**Server Software**: The server software you would like to you use (Vanilla, Bukkit, Spigot, Paper), if you want to use a jar that is not in that list put 'Custom'. <br />
**Server Version**: The version of the server software (e.g. 1.7.10, 1.8.9, 1.16.5...), if you are using the 'Custom' argument for the server software please put a direct download link to the jar file here. <br />
**Auto Config**: If you want your server to be automaticaly linked to your proxy (true/false) (**NOTE: Currently Bukkit servers are not linkable to the proxy however Vanilla is (Thanks to [VanillaCord](https://github.com/ME1312/VanillaCord/tree/master)!)**) <br />

(Optional) *Distant IP*: The public IP of the machine the server will be running (e.g. 192.168.0.1) <br />
(Optional) *Start Port*: From which port should Dynamic.Server try getting an available port (if the port is taken, it will try a higher port) <br />

### Configure your proxy *(dcs.configureProxy, dcs.setDefaultServer, dcs.toggleIPForward, dcs.toggleOnlineMode, dcs.changeProxyPort, dcs.removeForcedSettings)*<br /><br />

`dcs.configureProxy <default_server> (ip_forward) (online_mode) (start_port)`<br /><br />

**Default Server**: The name of the server you should first get connected to when connecting to the proxy (e.g. Survival, My_Awesome_Server). <br />

(Optional) *IP Forward*: If you want IP Forward to be enabled or not (true/false). <br />
(Optional) *Online Mode*: If you want your proxy to be on online mode or not (accept cracked accounts or not) (true/false). <br />
(Optional) *Start Port*: From which port should Dynamic.Server try getting an available port (if the port is taken, it will try a higher port) (this changes both query and host ports). <br /><br />

`dcs.setDefaultServer <default_server>`

**Default Server**: The name of the server you should first get connected to when connecting to the proxy (e.g. Survival, My_Awesome_Server).<br /><br />

`dcs.toggleIPForward <true/false>`

**IP Forward**: If you want IP Forward to be enabled or not (true/false).<br /><br />

`dcs.toggleOnlineMode <true/false>`

**Online Mode**: If you want your proxy to be on online mode or not (accept cracked accounts or not) (true/false).<br /><br />

`dcs.changeProxyPort <start_port>`

**Start Port**: From which port should Dynamic.Server try getting an available port (if the port is taken, it will try a higher port) (this changes both query and host ports).<br /><br />

`dcs.removeForcedSettings`

This simply removes all 'forced' default BungeeCord settings such as md_5 being in the admin group, or md_5's pvp server as a forced host. This is done automaticaly when doing dcs.configureProxy.<br /><br />

## Future features

- Add an autostart after install option
- Add a server.properties preset option
- Support Velocity & Waterfall
- Control and manage the different servers connected to the proxy
- Access servers terminal on the same terminal as the proxy's
- Add/Remove an existing server from the proxy
- Delete/update servers on the proxy
