package honours.ing.banq.time.bean;

import java.util.Date;

/**
 * This bean is the return type for some of the {@link honours.ing.banq.time.TimeService}'s methods.
 * @author Jeffrey Bakker
 * @since 4-8-17
 */
public class DateBean {

    private Date date;

    /**
     * Creates a new {@link DateBean} with the given parameters.
     * @param date the date
     */
    public DateBean(Date date) {
        this.date = date;
    }

    /**
     * Returns the date.
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date.
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
