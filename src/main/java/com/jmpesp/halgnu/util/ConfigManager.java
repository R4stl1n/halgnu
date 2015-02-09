package com.jmpesp.halgnu.util;


import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigManager {
    private final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static ConfigManager m_instance;
    private static Ini m_iniConfig;

    private int m_ircServerPort;
    private String m_ircServer;
    private String m_ircChannel;
    private String m_ircNick;
    private String m_ircPassword;

    protected ConfigManager() {
        m_iniConfig = new Ini();
        logger.info("Starting Configuration Manager");
    }

    public static ConfigManager getInstance() {
        if(m_instance == null) {
            m_instance = new ConfigManager();
        }
        return m_instance;
    }

    public boolean checkConfigExist() {
        File configFile = new File("config.ini");
        if(configFile.exists()) {
            logger.info("Configuration file found");
            return true;
        } else {
            logger.info("No configuration file located");
            return false;
        }
    }

    public boolean loadConfigurationFile() {
        try {
            logger.info("Loading configuration file");
            m_iniConfig.load(new FileReader("config.ini"));

            // Load the connection specific information
            Ini.Section connection = m_iniConfig.get("connection");
            m_ircServer = connection.get("server");
            m_ircServerPort = Integer.parseInt(connection.get("port"));
            m_ircChannel = connection.get("channel");

            Ini.Section credentials = m_iniConfig.get("credentials");
            // Load the credential specific information
            m_ircNick = credentials.get("nick");
            m_ircPassword = credentials.get("password");

            logger.info("Loaded configuration file");
            return true;
        } catch (FileNotFoundException ex) {
            logger.info("Failed to load configuration file");
            return false;
        } catch (InvalidFileFormatException e) {
            logger.info("Failed to load configuration file");
            return false;
        } catch (IOException e) {
            logger.info("Failed to load configuration file");
            return false;
        }
    }

    public int getIrcPort() {
        return m_ircServerPort;
    }

    public String getIrcServer() {
        return m_ircServer;
    }

    public String getIrcPassword() {
        return m_ircPassword;
    }

    public String getIrcChannel() {
        return m_ircChannel;
    }

    public String getIrcNick() {
        return m_ircNick;
    }
}
