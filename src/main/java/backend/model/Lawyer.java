package backend.model;

import java.util.Date;

/*
 * Super Type for Lawyers
 */
public class Lawyer {
    public int lawyer_id;
    public int bar_id;
    public String name;
    public Date start_date;
    public int new_cases;
    public long success_rate;
    public long rate;

    public Lawyer(int lawyer_id, int bar_id, String name,Date start_date, int new_cases,long success_rate, long rate) {
        this.lawyer_id = lawyer_id;
        this.bar_id = bar_id;
        this.name = name;
        this.start_date = start_date;
        this.new_cases = new_cases;
        this.success_rate = success_rate;
        this.rate = rate;
    }
}
