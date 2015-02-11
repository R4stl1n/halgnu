package com.jmpesp.halgnu;

import com.jmpesp.halgnu.managers.ConfigManager;
import com.jmpesp.halgnu.managers.DatabaseManager;
import com.jmpesp.halgnu.managers.IRCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HalGNUMain {

    private static Logger m_logger = LoggerFactory.getLogger(HalGNUMain.class);

    public static void main(String[] args) throws Exception {

        if(ConfigManager.getInstance().checkConfigExist()) {

            if(ConfigManager.getInstance().loadConfigurationFile()) {
                IRCConnectionManager.getInstance();
                if(IRCConnectionManager.getInstance().startConnection()) {
                    m_logger.info("Connection established successfully");
                } else {
                    m_logger.error("Error occured when connecting");
                }


            }
        }

        DatabaseManager.getInstance();
    }
}
