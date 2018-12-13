# CaptainChat
This plugin uses deprecated events, ***do not use it*** if you are not using a plugin that requires it.
This is only available to provide compatibility support for [MYOA](https://github.com/gochuonpire/MYOA)'s chat functions.
Releases are available [here](https://gochuonpire.us/releases/). There will most likely be no updates.

## Config
The config file is located at plugins/CaptainChat/config.yml. The default file looks like this:
```
Global:
  Format: '&7[&a+senderName&7]: +message'
  AccessNode: captainchat.global
Local:
  Format: '&7[L] +senderName: +message'
  AccessNode: captainchat.local
  Range: 50
```
Global/local nodes should probably be given to every player. If you are running a small server no additional setup is required. Variables are defined below in channel configuration.

## Channel Config
If you want extra channels, create a file at plugins/CaptainChat/channels.yml. The format for these channels is similar to the config.yml
```
Admin:
  Name: 'Admin'
  Format: '&7[&4+senderName&7]: +message'
  AccessNode: captainchat.admin
```
You can create as many channels as you want. Players with the given permissions node will be able to join/leave the channel as they wish. The variables that can be specified in the config are as follows:
```
+senderName - Player's display name
+worldName - World's name that the chatting player is currently in
+channelName - Channel's name
+prefix - Player's prefix defined based on their group in PeX
+message - Chat message
&[a-f0-9] - Minecraft chat colors
```
