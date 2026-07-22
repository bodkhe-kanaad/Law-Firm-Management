package backend.model;
/*
 * Object Class for Client
 */
public class Client {
    public int client_id;
    public String name;
    public String contact;
    public long amount_payable;

    public Client(int client_id, String name, String contact, long amount_payable){
        this.client_id = client_id;
        this.name = name;
        this.contact = contact;
        this.amount_payable = amount_payable;
    }

}
