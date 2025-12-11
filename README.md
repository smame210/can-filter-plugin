# CAN Filter Plugin for Graylog

____ 
Graylog plugin to check JSON messages in the pipeline feature

**Required Graylog version:** 5.0 and later

Installation
------------

[Download the plugin](https://github.com/smame210/can-filter-plugin/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.

Restart `graylog-server` and you are done.

Development
-----------

You can improve your development experience for the web interface part of your plugin
dramatically by making use of hot reloading. To do this, do the following:

* `git clone https://github.com/Graylog2/graylog2-server.git`
* `cd graylog2-server/graylog2-web-interface`
* `ln -s $YOURPLUGIN plugin/`
* `npm install && npm start`

Usage
-----
*Function Prototype:*
![img.png](img.png)  
*Function usage in Graylog rules:*

```
rule "graylog_rule"
when
    can_filter(to_string($message), "0x1,0x4")
then
    drop_message();
end
```
____


Getting started
---------------

This project is using Maven 3 and requires Java 8 or higher.

* Clone this repository.
* Run `mvn package -P '!web-interface-build' -DskipTests=true -Denforcer.skip=true` to build a JAR file.
* Optional: Run `mvn jdeb:jdeb` and `mvn rpm:rpm` to create a DEB and RPM package respectively.
* Copy generated JAR file in target directory to your Graylog plugin directory.
* Restart the Graylog.

Plugin Release
--------------

We are using the maven release plugin:

```
$ mvn release:prepare
[...]
$ mvn release:perform
```

This sets the version numbers, creates a tag and pushes to GitHub.
