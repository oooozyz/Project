package hrms.ZMGFH.Objection;

import java.sql.Date;

public class Detail {
    int id;
    Date ddate;
    int change_num;
    String reason;
    public Detail(int id, Date ddate, int change_num, String reason) {
        this.id = id;
        this.ddate = ddate;
        this.change_num = change_num;
        this.reason = reason;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Date getDdate() {
        return ddate;
    }
    public void setDdate(Date ddate) {
        this.ddate = ddate;
    }
    public int getChange_num() {
        return change_num;
    }
    public void setChange_num(int change_num) {
        this.change_num = change_num;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
}
