package converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nicob on 09.11.2016.
 * converts a string to date
 */

public class DateConverter {

    //instance for the singleton pattern
    private static DateConverter instance = new DateConverter();

    /**
     *
     * @return the instance of the date converter
     */
    public static DateConverter getInstance() {
        return instance;
    }

    private DateConverter() {
    }

    /**
     *
     * @param dateString datestring that needs to be converted
     * @return the converted date
     */
    public Date getDateFromString(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX"); //format of the date to parse
        Date date = null;

        try {
            date = dateFormat.parse(dateString.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}