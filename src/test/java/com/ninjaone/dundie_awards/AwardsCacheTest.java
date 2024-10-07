package com.ninjaone.dundie_awards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AwardsCacheTest {

    @Test
    public void testSetTotalAwards() {
        AwardsCache awardsCache = new AwardsCache();
        awardsCache.setTotalAwards(10);
        assert awardsCache.getTotalAwards() == 10;
    }

    @Test
    public void testAddOneAward() {
        AwardsCache awardsCache = new AwardsCache();
        awardsCache.addOneAward();
        assert awardsCache.getTotalAwards() == 1;
    }

    @Test
    public void testRemoveAwards() {
        AwardsCache awardsCache = new AwardsCache();
        awardsCache.setTotalAwards(10);
        awardsCache.removeAwards(5);
        assert awardsCache.getTotalAwards() == 5;
    }

    @Test
    public void testRemoveAwardsThrowsException() {
        AwardsCache awardsCache = new AwardsCache();
        awardsCache.setTotalAwards(1);
        assertThrows(IllegalArgumentException.class, () -> awardsCache.removeAwards(2));

    }
}
