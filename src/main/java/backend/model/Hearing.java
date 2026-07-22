package backend.model;

import java.util.Date;

/*
 * Object Class for Hearing
 */
public class Hearing {
    public int case_id;
    public Date hearing_date;
    public int courtId;

    public Hearing(int case_id, Date hearing_date, int courtId) {
        this.case_id = case_id;
        this.hearing_date = hearing_date;
        this.courtId = courtId;
    }
}
