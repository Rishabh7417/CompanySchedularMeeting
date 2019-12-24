package android.example.companymeetingschedular;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String convertTimeToShow(String responseTime)
    {
        int hourOfDay;
        String ampm;
        String formattedTime="";

        if(responseTime != null && !responseTime.isEmpty()) {
            int colonIndex = responseTime.indexOf(':');
            hourOfDay = Integer.parseInt(responseTime.substring(0, colonIndex));
            int minute = Integer.parseInt(responseTime.substring(colonIndex + 1, colonIndex + 3));

            if (hourOfDay > 12) {
                hourOfDay = hourOfDay - 12;
                ampm = "PM";
            }
            else
            {
                if(hourOfDay == 12)
                    ampm = "PM";
                else {
                    if(hourOfDay == 0)hourOfDay = 12;
                    ampm = "AM";
                }
            }

            formattedTime = (((hourOfDay < 10) ? ("0" + hourOfDay) : hourOfDay) + ":" + ((minute < 10) ? ("0" + minute) : minute) + " " + ampm);
        }
        return formattedTime;
    }

    public static String getNextDateInString(Date curDate) {
        final SimpleDateFormat format =  new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar calendar = Calendar.getInstance();
        try {

            calendar.setTime(curDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        }
        catch (Exception e)
        {

        }
        return format.format(calendar.getTime());
    }

    public static Date getNextDate(Date curDate) {
        final SimpleDateFormat format =  new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar calendar = Calendar.getInstance();
        try {

            calendar.setTime(curDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        }
        catch (Exception e)
        {

        }
        return calendar.getTime();
    }

    public static Date getPrevDate(Date curDate) {
        final SimpleDateFormat format =  new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar calendar = Calendar.getInstance();
        try {

            calendar.setTime(curDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);

        }
        catch (Exception e)
        {

        }
        return calendar.getTime();
    }

    public static String getPrevDateInString(Date curDate) {
        final SimpleDateFormat format =  new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar calendar = Calendar.getInstance();
        try {

            calendar.setTime(curDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);

        }
        catch (Exception e)
        {

        }
        return format.format(calendar.getTime());
    }
}
