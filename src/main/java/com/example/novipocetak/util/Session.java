package com.example.novipocetak.util;

import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
public class Session {
    private static String loggedInEmail;

    public static String getLoggedInEmail() {
        return loggedInEmail;
    }

    public static void setLoggedInEmail(String email) {
        loggedInEmail = email;
    }
}
