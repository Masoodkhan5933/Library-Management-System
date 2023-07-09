/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

/**
 *
 * @author Mukhtiar-HPC
 */
//public enum MessageType {
//    Information,
//    Warning,
//    Error,
//    Exception
//}

public enum MessageType {
    SUCCESS("Success"),
    NOTIFICATION("Notification"),
    WARNING("Warning"),
    ERROR("Error"),
    EXCEPTION("Exception");

    private final String displayName;

    MessageType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
