package backend.model;

import java.util.Date;

/*
 * Object Class for Corporate Lawyer
 */
public class CorporateLawyer extends Lawyer{
    public int retainer_required;

    public CorporateLawyer(int lawyer_id,int bar_id,String name,Date start_date,int new_cases,int success_rate,long rate,int retainer_required) {
        super(lawyer_id, bar_id, name, start_date, new_cases, success_rate, rate);
        this.retainer_required = retainer_required;
    }
}
