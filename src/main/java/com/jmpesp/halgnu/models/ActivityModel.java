package com.jmpesp.halgnu.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ActivityModel")
public class ActivityModel {

    @DatabaseField(id = true)
    private String m_userName;

    @DatabaseField
    private String m_lastActive;

    @DatabaseField
    private String m_lastMessage;

    @DatabaseField
    private boolean m_exempt;

    public ActivityModel() {
        // ORMLite needs a no-arg constructor
    }

    public void setUsername(String username) { m_userName = username; }
    public String getUsername() { return m_userName; }

    public void setLastActive(String lastActive) { m_lastActive = lastActive; }
    public String getLastActive() { return m_lastActive; }

    public void setLastMessage(String lastMessage) { m_lastMessage = lastMessage; }
    public String getLastMessage() { return m_lastMessage; }

    public void setExempt(boolean exempt) { m_exempt = exempt; }
    public boolean getExempt() { return m_exempt; }

}