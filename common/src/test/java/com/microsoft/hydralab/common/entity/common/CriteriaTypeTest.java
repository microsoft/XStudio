package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

public class CriteriaTypeTest {

    @Test
    public void testOpType() {
        Assert.assertEquals("equal", CriteriaType.OpType.Equal);
        Assert.assertEquals("ne", CriteriaType.OpType.NotEqual);
        Assert.assertEquals("gt", CriteriaType.OpType.GreaterThan);
        Assert.assertEquals("lt", CriteriaType.OpType.LessThan);
        Assert.assertEquals("like", CriteriaType.OpType.Like);
        Assert.assertEquals("in", CriteriaType.OpType.In);
    }

    @Test
    public void testLikeRuleType() {
        Assert.assertEquals("front", CriteriaType.LikeRuleType.Front);
        Assert.assertEquals("end", CriteriaType.LikeRuleType.End);
        Assert.assertEquals("all", CriteriaType.LikeRuleType.All);
    }
}