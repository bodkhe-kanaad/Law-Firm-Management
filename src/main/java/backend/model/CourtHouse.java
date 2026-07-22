package backend.model;
/*
 * Object Class for CourtHouse
 */
public class CourtHouse {
    public int courtId;
    public String judge_name;
    public String city;
    public String province;

    public CourtHouse(int courtId, String judge_name, String city, String province){
        this.courtId = courtId;
        this.judge_name = judge_name;
        this.city = city;
        this.province = province;
    }

}
