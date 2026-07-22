package backend.model;

import java.util.Date;

public class WorkLog {
    public int lawyer_id;
    public int case_id;
    public Date log_date;
    public long number_of_hours;

    public WorkLog(int lawyer_id, int case_id, Date log_Date, long number_of_hours) {
        this.lawyer_id = lawyer_id;
        this.case_id = case_id;
        this.log_date = log_Date;
        this.number_of_hours = number_of_hours;
    }
}
