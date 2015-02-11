package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.util.CommandHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

/**
 * Created by r4stl1n on 2/10/2015.
 */
public class VersionListener extends ListenerAdapter {
    
    private String m_command = ".v";

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if (event.getMessage().startsWith(m_command)) {
            if(CommandHelper.checkForAmountOfArgs(event.getMessage(), 0)) {
                event.respond("HalGNU V1.0 - Daisy");
            } else {
                event.respond("Ex: "+m_command+"");
            }
        }
    }
}
