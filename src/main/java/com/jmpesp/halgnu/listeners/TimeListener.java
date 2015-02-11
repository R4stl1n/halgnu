package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.util.CommandHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeListener extends ListenerAdapter {
    private String m_command = ".time";

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if (event.getMessage().startsWith(m_command)) {
            if(CommandHelper.checkForAmountOfArgs(event.getMessage(), 0)) {

                event.respond("Current Date/Time: " + getDateTime());
            } else {
                event.respond("Ex: "+m_command+"");
            }
        }
    }

    public String getDateTime() {
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();

        return df.format(dateobj);
    }
}
