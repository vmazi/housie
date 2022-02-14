package com.housie.model.ticket;

import java.util.Objects;

public class TicketSpot {
    private Integer spotValue;
    private boolean marked;

    public TicketSpot(Integer spotValue) {
        this.spotValue = spotValue;
        this.marked = false;
    }

    public Integer getSpotValue() {
        return spotValue;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketSpot that = (TicketSpot) o;
        return marked == that.marked &&
                Objects.equals(spotValue, that.spotValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spotValue, marked);
    }
}
