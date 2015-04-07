package com.jmpesp.halgnu.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MemberModel")
public class MemberModel {
    
    public enum MemberStatus {
        PROSPECT,
        MEMBER,
        ADMIN,
        OG
    }
    
    @DatabaseField(id = true)
    private String m_userName;
    
    @DatabaseField
    private String m_dateInvited;

    @DatabaseField
    private String m_invitedBy;
    
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    MemberStatus m_memberStatus;

    @DatabaseField
    private String m_twitterHandle;

    public MemberModel() {
        // ORMLite needs a no-arg constructor
    }
    
    public String getUserName() {
        return m_userName;
    }
    
    public void setUserName(String m_userName) {
        this.m_userName = m_userName;
    }
    
    public String getDateInvited() {
        return m_dateInvited;
    }
    
    public void setDateInvited(String date) {
        this.m_dateInvited = date;
    }
    
    public String getInvitedBy() {
        return m_invitedBy;
    }
    
    public void setInvitedBy(String invitedBy) {
        this.m_invitedBy = invitedBy;
    }
    
    public MemberStatus getMemberStatus() {
        return m_memberStatus;        
    }
    
    public void setMemberStatus(MemberStatus memberStatus) {
        this.m_memberStatus = memberStatus;
    }

    public String getTwitterHandle() {
        return m_twitterHandle;
    }
    
    public void setTwitterHandle(String m_twitterHandle) {
        this.m_twitterHandle = m_twitterHandle;
    }
}
