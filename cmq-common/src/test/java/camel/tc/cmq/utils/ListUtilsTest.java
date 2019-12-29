package camel.tc.cmq.utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

// done
public class ListUtilsTest {

    @Test
    public void testContains() {
        Assert.assertTrue(ListUtils.contains(new ArrayList<>(Arrays.asList(
                new byte[0])), new byte[0]));
        Assert.assertTrue(ListUtils.contains(new ArrayList<>(Arrays.asList(
                new byte[]{1, 2, 3})), new byte[]{1, 2, 3}));

        Assert.assertFalse(ListUtils.contains(new ArrayList<>(Arrays.asList(
                new byte[]{1, 2, 3})), new byte[0]));
        Assert.assertFalse(ListUtils.contains(new ArrayList<>(Arrays.asList(
                new byte[]{1, 2, 3})), new byte[]{4, 5, 6}));
        Assert.assertFalse(ListUtils.contains(new ArrayList<>(Arrays.asList(
                new byte[]{1}, new byte[]{2}, new byte[]{3})),
                new byte[]{1, 2, 3}));
    }

    @Test
    public void testContainsAll() {
        Assert.assertTrue(ListUtils.containsAll(
                new ArrayList<>(Arrays.asList(new byte[0])),
                new ArrayList<>(Arrays.asList(new byte[0]))));
        Assert.assertTrue(ListUtils.containsAll(
                new ArrayList<>(Arrays.asList(new byte[]{1, 2})),
                new ArrayList<>(Arrays.asList(new byte[]{1, 2}))));

        Assert.assertFalse(ListUtils.containsAll(
                new ArrayList<>(Arrays.asList(new byte[0])),
                new ArrayList<>(Arrays.asList(new byte[]{1, 2}))));
        Assert.assertFalse(ListUtils.containsAll(
                new ArrayList<>(Arrays.asList(new byte[]{1, 2})),
                new ArrayList<>(Arrays.asList(new byte[]{2, 3, 4}))));
        Assert.assertFalse(ListUtils.containsAll(
                new ArrayList<>(Arrays.asList(new byte[]{1, 2})),
                new ArrayList<>(Arrays.asList(new byte[]{4, 5, 6}))));
    }

    @Test
    public void testIntersection() {
        Assert.assertTrue(ListUtils.intersection(
                new ArrayList<>(Arrays.asList(new byte[0])),
                new ArrayList<>(Arrays.asList(new byte[0]))));
        Assert.assertTrue(ListUtils.intersection(
                new ArrayList<>(Arrays.asList(new byte[]{1, 2})),
                new ArrayList<>(Arrays.asList(new byte[]{1, 2}))));

        Assert.assertFalse(ListUtils.intersection(
                new ArrayList<>(Arrays.asList(new byte[0])),
                new ArrayList<>(Arrays.asList(new byte[]{1, 2}))));
        Assert.assertFalse(ListUtils.intersection(
                new ArrayList<>(Arrays.asList(new byte[]{1, 2})),
                new ArrayList<>(Arrays.asList(new byte[]{2, 3, 4}))));
        Assert.assertFalse(ListUtils.intersection(
                new ArrayList<>(Arrays.asList(new byte[]{1, 2})),
                new ArrayList<>(Arrays.asList(new byte[]{4, 5, 6}))));
    }
}
