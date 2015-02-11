package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.util.CommandHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class HelpListener extends ListenerAdapter {

    private String m_command = ".help";
    
    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if (event.getMessage().startsWith(m_command)) {
            if(CommandHelper.checkForAmountOfArgs(event.getMessage(), 0)) {
                event.respond("Available commands are .t, .g, .hello, .v");
            } else {
                event.respond("Ex: "+m_command+"");
            }
        }
    }
    
}
