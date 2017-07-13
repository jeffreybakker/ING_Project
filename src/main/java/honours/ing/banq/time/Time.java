package honours.ing.banq.time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Kevin Witlox
 * @since 13-7-2017.
 */
@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private int shift;

    public Time() {}

    public Time(int shift) {
        this.shift = shift;
    }

    public Integer getId() {
        return id;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    /**
     * Returns true if the given string conforms the database date format, false otherwise.
     *
     * @param date A string representing a date
     * @return True is success, false otherwise
     */
    public static boolean checkDate(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (date.equals(dateFormat.format(dateFormat.parse(date)))) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

}
