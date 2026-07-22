package backend.model;

import java.util.Date;

/*
 * Object Class for Family Lawyer
 */
public class FamilyLawyer extends Lawyer{
    public int mediation_certified;

    public FamilyLawyer(int lawyer_id, int bar_id, String name,Date start_date, int new_cases,long success_rate, long rate, int mediation_certified) {
        super(lawyer_id, bar_id, name, start_date, new_cases, success_rate, rate);
        this.mediation_certified = mediation_certified;
    }
}
