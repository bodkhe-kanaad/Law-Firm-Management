package backend.model;
/*
 * Object Class for Invoice
 */
public class Invoice {
    public int invoice_id;
    public long amount;
    public int case_id;
    public int status_code;

    public Invoice(int invoice_id, long amount, int case_id, int status_code) {
        this.invoice_id = invoice_id;
        this.amount = amount;
        this.case_id = case_id;
        this.status_code = status_code;
    }
}
