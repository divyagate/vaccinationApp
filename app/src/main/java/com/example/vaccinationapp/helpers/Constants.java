package com.example.vaccinationapp.helpers;

import java.util.Calendar;
import java.util.Date;

public class Constants {
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String CHILD_NAME = "child_name";
    public static final String CHILD_ID = "child_id";
    public static final String EMAIL_ID = "mail_id";
    public static final String USER_TYPE = "user_type";
    public static final String PASSWORD = "password";
    public static final String CITY = "city";
    public static final String CHILD_DOB = "dob";


    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}

