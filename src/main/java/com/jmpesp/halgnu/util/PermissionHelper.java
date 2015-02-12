package com.jmpesp.halgnu.util;

import com.jmpesp.halgnu.managers.DatabaseManager;
import com.jmpesp.halgnu.models.MemberModel;

import java.sql.SQLException;
import java.util.List;

public class PermissionHelper {

    public static boolean HasPermissionFromList(List<MemberModel.MemberStatus> permissions, String username) {
        try {
            MemberModel member = DatabaseManager.getInstance().getMemberByUsername(username);
            if(member == null) {
                System.out.println("SHIT WAS NULL");
                return false;
            } else {
                for (MemberModel.MemberStatus status : permissions) {
                    if (member.getMemberStatus().equals(status)){
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean HasPermission(MemberModel.MemberStatus permission, String username) {
        try {
            MemberModel member = DatabaseManager.getInstance().getMemberByUsername(username);

            if(member.getMemberStatus() == permission) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
