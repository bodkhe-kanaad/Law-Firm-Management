package backend.model;
/*
 * Object Class for InvoiceStatus
 */
public class InvoiceStatus {
    public int status_code;
    public statusDescription status_description;

    public enum statusDescription {
        Paid,
        Unpaid
    }

    public InvoiceStatus(int status_code, statusDescription status_description) {
        this.status_code = status_code;
        this.status_description = status_description;
    }
}
