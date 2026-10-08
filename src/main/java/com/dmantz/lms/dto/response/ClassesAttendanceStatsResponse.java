package com.dmantz.lms.dto.response;

public class ClassesAttendanceStatsResponse {

    private int attended;
    private int skipped;
    private int monthAttended;
    private int monthSkipped;

    public int getAttended() {
        return attended;
    }

    public void setAttended(int attended) {
        this.attended = attended;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getMonthAttended() {
        return monthAttended;
    }

    public void setMonthAttended(int monthAttended) {
        this.monthAttended = monthAttended;
    }

    public int getMonthSkipped() {
        return monthSkipped;
    }

    public void setMonthSkipped(int monthSkipped) {
        this.monthSkipped = monthSkipped;
    }

    @Override
    public String toString() {
        return "ClassesAttendanceStatsResponse{" +
                "attended=" + attended +
                ", skipped=" + skipped +
                ", monthAttended=" + monthAttended +
                ", monthSkipped=" + monthSkipped +
                '}';
    }
}
