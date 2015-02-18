HalGNU IRC Bot 1.0
============

### Description
HalGNU is a irc bot designed to be a helpful irc bot for member only irc channels.

### Why java
Needed a project that would force me to use a language I purposely avoid.

### Modules

#### Admin
Admin module offers basic admin level functions

```
.kick <user> - Kick user from channel
.topic <topic> - Change room topic
```

#### Bouncer
Features for member only rooms. Bouncer is capable of maintaining a white list of users and removing unwanted users.

```
.invite <user> - Used to invite user to room
.whoinvited <user> - Returns who invited the user
.memberstatus - Returns your member status
.statusofmember <member> - Returns status of desired member
.enforce - Enables/Disables bouncer enforcement
.changestatus <user> <status> - Change users membership status
.removemember <user> - Removes user from the room
```

#### Google
Simple google module that allows for querying.

```
.google <query> - Google query to execute
```

#### HelloWorld
Useless module.

```
.hello - Useless just send hello world
```

#### Help
Shows a useful help menu.

```
.help - Shows generic help message
.help <module> - Shows module specific help message
```

#### Time
Time module.

```
.time - Shows current server time
```

#### Twitter
Twitter integration allows for tweeting and receiving from the twitter api.

```
.tweet <tweet> - Used to send out a tweet under group account
```

#### Version
Shows current version number and name.

```
.version - Returns the current version
```

#### Website
Basic website url module

```
.title - Returns the title of the last link posted.
```
