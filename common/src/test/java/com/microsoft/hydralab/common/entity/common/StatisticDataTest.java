package com.microsoft.hydralab.common.entity.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StatisticDataTest {

    @Test
    public void testConstructorWithNameAndValue() {
        String name = "Test";
        int value = 10;
        
        StatisticData statisticData = new StatisticData(name, value);
        
        assertNotNull(statisticData.getId());
        assertNotNull(statisticData.getStartTime());
        assertNotNull(statisticData.getEndTime());
        assertEquals(name, statisticData.getName());
        assertEquals(value, statisticData.getValue());
    }
    
    @Test
    public void testDefaultConstructor() {
        StatisticData statisticData = new StatisticData();
        
        assertNotNull(statisticData.getId());
        assertNull(statisticData.getStartTime());
        assertNull(statisticData.getEndTime());
        assertNull(statisticData.getName());
        assertEquals(0, statisticData.getValue());
    }
}