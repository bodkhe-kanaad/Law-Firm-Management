package backend.model;

import java.util.Date;

/*
 * Object Class for Case
 */
public class CaseFile {

    public int case_id;
    public Date opening_date;
    public Date closing_date;
    public caseTypes case_type;
    public int is_successful;
    public int client_id;
    public int lawyer_id;

    public CaseFile(int case_id, Date opening_date, Date closing_date, caseTypes case_type, int is_successful, int client_id, int lawyer_id) {
        this.case_id = case_id;
        this.opening_date = opening_date;
        this.closing_date = closing_date;
        this.case_type = case_type;
        this.is_successful = is_successful;
        this.client_id = client_id;
        this.lawyer_id = lawyer_id;
    }


    public enum caseTypes {
        Criminal,
        Family,
        Corporate
    }
}
