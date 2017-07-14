package honours.ing.banq.time.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Kevin Witlox
 * @since 13-7-2017.
 */
public class DateBean {

    private String date;

    public DateBean(Date date) {
        this.date = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
