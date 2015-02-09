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
            m_iniConfig.load(new FileReader("config.ini"));
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

}
