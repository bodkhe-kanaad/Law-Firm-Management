package backend.model;

import java.util.Date;

/*
 * Object Class for Presented
 */
public class Presented {
    public int evidence_id;
    public int case_id;
    public Date hearing_date;

    public Presented(int evidence_id, int case_id, Date hearing_date) {
        this.evidence_id = evidence_id;
        this.case_id = case_id;
        this.hearing_date = hearing_date;
    }
}
