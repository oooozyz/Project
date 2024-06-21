package hrms.ZMGFH.Objection;

public class Worktime {
    private String start;// 上班时间
    private String end;// 下班时间

    public Worktime() {
        super();
    }

    public Worktime(String start, String end) {
        super();
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
