package backend.model;
/*
 * Object Class for Evidence
 */
public class Evidence {
    public int evidence_id;
    public storedLocations stored_in;
    public evidenceType evidence_type;
    public int case_id;

    public Evidence(int evidence_id, storedLocations stored_in, evidenceType evidence_type, int case_id) {
        this.evidence_id = evidence_id;
        this.stored_in = stored_in;
        this.evidence_type = evidence_type;
        this.case_id = case_id;
    }


    public enum storedLocations {
        Cabinet,
        Digital,
        Police
    }

    public enum evidenceType {
        Files,
        PenDrive,
        Forensic
    }
}
