package com.gfplugins.masks.managers;

import java.util.List;

public class MessageManager {

    private String noPermission, reloadSuccess, noMaskFound, notANumber, gaveAllMask, gaveMask, receivedMask, noPermissionMask;
    private List<String> help;

    public MessageManager(String noPermission, String reloadSuccess, String noMaskFound, String notANumber, String gaveAllMask, String gaveMask, String receivedMask, String noPermissionMask, List<String> help) {
        this.noPermission = noPermission;
        this.reloadSuccess = reloadSuccess;
        this.noMaskFound = noMaskFound;
        this.notANumber = notANumber;
        this.gaveAllMask = gaveAllMask;
        this.gaveMask = gaveMask;
        this.receivedMask = receivedMask;
        this.noPermissionMask = noPermissionMask;
        this.help = help;
    }

    public String getNoPermission() {
        return noPermission;
    }

    public String getReloadSuccess() {
        return reloadSuccess;
    }

    public String getNoMaskFound() {
        return noMaskFound;
    }

    public String getNotANumber() {
        return notANumber;
    }

    public String getGaveAllMask() {
        return gaveAllMask;
    }

    public String getGaveMask() {
        return gaveMask;
    }

    public String getReceivedMask() {
        return receivedMask;
    }

    public String getNoPermissionMask() {
        return noPermissionMask;
    }

    public List<String> getHelp() {
        return help;
    }
}
