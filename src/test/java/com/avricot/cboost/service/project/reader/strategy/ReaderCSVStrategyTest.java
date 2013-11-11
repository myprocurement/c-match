package com.avricot.cboost.service.project.reader.strategy;

import com.avricot.cboost.service.project.reader.FileReaderException;
import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 */
public class ReaderCSVStrategyTest {
    ReaderCSVStrategy csvStrategy = new ReaderCSVStrategy();

    @Test
    public void testDelimiter() {
        HashMap<Character, Integer> separatorCount = new HashMap<Character, Integer>();
        separatorCount.put(';', 2);
        separatorCount.put(',', null);
        separatorCount.put('a', 1);

        Character delimiter = csvStrategy.getBestSeparator(separatorCount);
        Assert.assertEquals(new Character(';'), delimiter);
    }

    @Test(expected = FileReaderException.class)
    public void testDelimiterExceptionNull() {
        HashMap<Character, Integer> separatorCount = new HashMap<Character, Integer>();
        separatorCount.put(';', null);
        separatorCount.put(',', null);
        separatorCount.put('a', null);

        Character delimiter = csvStrategy.getBestSeparator(separatorCount);
    }

    @Test
    public void testDelimiterAllEqualToZero() {
        LinkedHashMap<Character, Integer> separatorCount = new LinkedHashMap<Character, Integer>();
        separatorCount.put('i', 0);
        separatorCount.put(',', 0);
        separatorCount.put('a', 0);

        Character delimiter = csvStrategy.getBestSeparator(separatorCount);
        Assert.assertEquals(new Character(';'), delimiter);
    }

    @Test(expected = FileReaderException.class)
    public void testDelimiterExceptionSameCount() {
        HashMap<Character, Integer> separatorCount = new HashMap<Character, Integer>();
        separatorCount.put(';', 20);
        separatorCount.put(',', 20);
        separatorCount.put('a', 20);

        Character delimiter = csvStrategy.getBestSeparator(separatorCount);
    }

    @Test(expected = FileReaderException.class)
    public void testDelimiterExceptionSameCountNull() {
        HashMap<Character, Integer> separatorCount = new HashMap<Character, Integer>();
        separatorCount.put(';', null);
        separatorCount.put(',', 20);
        separatorCount.put('a', 20);

        Character delimiter = csvStrategy.getBestSeparator(separatorCount);
    }
}
