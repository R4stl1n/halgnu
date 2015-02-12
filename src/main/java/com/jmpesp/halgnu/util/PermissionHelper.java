package com.jmpesp.halgnu.util;

import com.jmpesp.halgnu.models.MemberModel;

import java.util.List;

public class PermissionHelper {

    private static boolean HasPermissionFromList(List<MemberModel.MemberStatus> permissions, String username) {
        return false;
    }

    private static boolean HasPermission(MemberModel.MemberStatus permission, String username) {
        return false;
    }
}
