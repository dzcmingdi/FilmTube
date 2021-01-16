package test.zmh.filmtube.utils;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FilmTubeDateParser {
    public static final SimpleDateFormat filmTubeVideoDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parse(String dateStr) throws ParseException {
        return filmTubeVideoDateFormat.parse(dateStr);
    }
}
