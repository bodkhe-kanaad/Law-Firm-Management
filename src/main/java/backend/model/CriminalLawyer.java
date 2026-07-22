package backend.model;

import java.util.Date;

/*
 * Object Class for Criminal Lawyer
 */
public class CriminalLawyer extends Lawyer{
    public String security_clearance;

    public CriminalLawyer(int lawyer_id,int bar_id,String name,Date start_date,int new_cases,int success_rate,long rate, String security_clearance) {
        super(lawyer_id, bar_id, name, start_date, new_cases, success_rate, rate);
        this.security_clearance = security_clearance;
    }
}
