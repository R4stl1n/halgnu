package com.jmpesp.halgnu.tasks;

import com.jmpesp.halgnu.managers.ConfigManager;
import com.jmpesp.halgnu.managers.DatabaseManager;
import com.jmpesp.halgnu.managers.IRCConnectionManager;
import com.jmpesp.halgnu.models.ActivityModel;
import com.jmpesp.halgnu.util.AdminCmdHelper;

import java.sql.SQLException;
import java.util.*;

public class InactivityTask extends TimerTask {

    @Override
    public void run() {
        try {

            List<ActivityModel> models = DatabaseManager.getInstance().getActivityDao().queryForAll();
            for(ActivityModel model: models) {

                Date modelDate = new Date(model.getLastActive());
                Calendar c = new GregorianCalendar();
                c.setTime(modelDate);
                c.add(Calendar.DATE, 14);
                Date inactiveDate =c.getTime();

                if(!model.getExempt()) {
                    if (new Date().after(inactiveDate)) {

                        if (DatabaseManager.getInstance().removeMemberByUsername(model.getUsername())) {

                            AdminCmdHelper.kickUserFromRoom(model.getUsername(), "Revoked_For_Inactivity");

                            // Notify the channel
                            String completeMessage = "Member: <" + model.getUsername() + ">" + " removed for inactivity";

                            IRCConnectionManager.getInstance().getBotConnection().sendIRC()
                                    .message(ConfigManager.getInstance().getIrcChannel(), completeMessage);

                        } else {
                            // Notify channel something went wrong
                            // Notify the channel
                            String completeMessage = "Error while removing member <"
                                    + model.getUsername() + ">" + "for inactivity";

                            IRCConnectionManager.getInstance().getBotConnection().sendIRC()
                                    .message(ConfigManager.getInstance().getIrcChannel(), completeMessage);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
