/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-12-01
 * Last Updated: 2015-12-01, Gunnar Eriksson
 * Description: Generates timestamp
 * Version: First version.
 */
package se.gunnareriksson.bank.timestamp;

import java.text.DateFormat;
import java.util.Date;

public class TimeStamp
{
    /**
     * Static method to return actual year, date and time separated 
     * with a space. Hours in 24 hour format
     * 
     * @return yy-mm-dd HH:mm:ss
     */
     public static String getCurrentYearDateAndTime()
     {
         DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
         Date now = new Date();
             
         return dateTimeFormat.format(now);
     }
}
