package com.avricot.cboost.service.project.reader.strategy;

import au.com.bytecode.opencsv.CSVReader;
import com.avricot.cboost.service.project.reader.FileReaderException;
import com.avricot.cboost.service.project.reader.ILineReader;
import com.google.common.collect.Lists;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 */
public class ReaderCSVStrategy implements IReaderStrategy{
    private static final List<Character> separators = Lists.newArrayList(',', ';');

    @Override
    public void process(File file, ILineReader lineProcessor, final String charsetName, final Integer lineMaxToRead) {
        try {
            HashMap<Character,Integer> separatorCount = new HashMap<Character,Integer>();
            // Loop done to get the delimiter back.
            //The right delimiter will be the one which value (in the Map) will be superior to 0.
            for(Character separator : separators){
                int lastColumnCount=-1;
                InputStreamReader ioReader = new InputStreamReader(new FileInputStream(file), charsetName);
                CSVReader reader = new CSVReader(ioReader, separator);
                String [] nextLine;
                int i = 0;
                boolean invalidSeparator = false ;
                while(i<3 && (nextLine = reader.readNext()) != null){
                    int inColumnCount = nextLine.length-1;
                    if(inColumnCount!=lastColumnCount && lastColumnCount !=-1){
                        separatorCount.put(separator, null);
                        invalidSeparator = true ;
                        break;
                    }
                    lastColumnCount = inColumnCount;
                }
                if(!invalidSeparator){
                    separatorCount.put(separator, lastColumnCount);
                }
                reader.close();
            }

            // Read the entire text
            CSVReader finalReader = new CSVReader(new FileReader(file), getBestSeparator(separatorCount));
            String[] nextLine;
            int i =0;
            Object result = null;
            while ((nextLine = finalReader.readNext()) != null) {
                result = lineProcessor.processLine(i, nextLine, result);
                i++;
                if(lineMaxToRead != null && i>lineMaxToRead){
                    break;
                }
            }
            finalReader.close();
        } catch (IOException e) {
            throw new FileReaderException("IO Exception during the 'process' method", e);
        }
    }

    /**
     * gets the best separator for the CSV file.
     */
    protected Character getBestSeparator(HashMap<Character, Integer> separatorCount) {
        Character delimiter = null;
        int maxValue = 0;
        for(Entry<Character,Integer> entry : separatorCount.entrySet()){
            Integer value = entry.getValue();
            if(value!=null && value >= maxValue){
                maxValue = value;
                delimiter = entry.getKey();
            }
        }
        if(delimiter == null){
            throw new FileReaderException("Cannot find any delimiter for this csv file.");
        }
        Integer rowCount = separatorCount.get(delimiter);
        if(rowCount.equals(Integer.valueOf(0))){
            return ';';
        }
        for(Entry<Character,Integer> entry : separatorCount.entrySet()){
            if(!entry.getKey().equals(delimiter) && rowCount.equals(entry.getValue())) {
                throw new FileReaderException("multiple delimiters match. Can't select one");
            }
        }
        return delimiter;
    }

}