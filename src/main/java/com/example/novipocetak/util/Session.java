package com.example.novipocetak.util;

import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
public class Session {
    private static String loggedInEmail;
    private static int loggedInID;

    public static String getLoggedInEmail() {
        return loggedInEmail;
    }

    public static void setLoggedInEmail(String email) {
        loggedInEmail = email;
    }

    public static int getLoggedInID() {
        return loggedInID;
    }

    public static void setLoggedInID(int id) {
        loggedInID = id;
    }
}
