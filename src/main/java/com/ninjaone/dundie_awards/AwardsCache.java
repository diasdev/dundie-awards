package com.ninjaone.dundie_awards;

import org.springframework.stereotype.Component;


@Component
public class AwardsCache {
    private int totalAwards;

    public synchronized void setTotalAwards(int totalAwards) {
        this.totalAwards = totalAwards;
    }

    public int getTotalAwards() {
        return totalAwards;
    }

    public synchronized void addOneAward() {
        this.totalAwards += 1;
    }

    public synchronized void removeAwards(int awardsRemoved) {
        if (this.totalAwards < awardsRemoved) {
            throw new IllegalArgumentException("Cannot remove more awards than are available");
        }
        this.totalAwards -= awardsRemoved;
    }
}
