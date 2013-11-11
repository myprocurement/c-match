package com.avricot.cboost.service.project.reader;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 */
public class ExcelReaderIntegrationTest {
    private final static HashMap<Integer, String> reading = new HashMap<Integer, String>();
    private static ILineReader lineProcessor = new ILineReader() {
        @Override
        public void processLine(Integer i, String[] split) {
            StringBuilder sb = new StringBuilder();
            for (String row : split) {
                sb.append(row).append("|");
            }
            reading.put(i, sb.toString());
        }
    };
    private String charsetName = "UTF-8";
    private FileReaderService fileReaderService = new FileReaderService();
    
    @Before
    public void clearReading(){
        reading.clear();
    }

    /**
     * CSV Part of tests
     */
    @Test
    public void testReaderCSVSemicolon() {
        try {
            fileReaderService.process(new ClassPathResource("reader/TestCsvSemicolon.csv").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je|je|je|tests||", reading.get(0));
            Assert.assertEquals("Ah|je?|je|je|tests||", reading.get(1));
            Assert.assertEquals("Oui|je|APACHE|POI|tests||", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderCSVSemicolon");
        }
    }


    @Test
    public void testReaderCSVComma() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestCsvComma.csv").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je|je|je|tests||", reading.get(0));
            Assert.assertEquals("Ah|je?|je|je|tests||", reading.get(1));
            Assert.assertEquals("Oui|je|APACHE|POI|tests||", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderCSVComma");
        }
    }

    @Test
    public void testReaderCSVSemicolonAndQuote() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestCsvSemicolonAndQuote.csv").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je;je|je|tests||", reading.get(0));
            Assert.assertEquals("Ah|je?|je|je;tests||", reading.get(1));
            Assert.assertEquals("Oui;je|APACHE|POI|tests||", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderCSVSemicolonAndQuote");
        }
    }

    @Test
    public void testReaderCSVCommaAndQuote() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestCsvCommaAndQuote.csv").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je,je|je|tests||", reading.get(0));
            Assert.assertEquals("Ah|je?|je|je,tests||", reading.get(1));
            Assert.assertEquals("Oui,je|APACHE|POI|tests||", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderCSVCommaAndQuote");
        }
    }

    @Test
    public void testReaderCSVMixedButSemicolonPrevailling() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestCsvMixedButSemicolonPrevailling.csv").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je|je|je,tests||", reading.get(0));
            Assert.assertEquals("Ah|je?|je,je|tests||", reading.get(1));
            Assert.assertEquals("Oui|je,APACHE|POI|tests||", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderCSVMixedButSemicolonPrevailling");
        }
    }

    @Test
    public void testReaderCSVMixedButCommaPrevailling() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestCsvMixedButCommaPrevailling.csv").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je|je|je;tests||", reading.get(0));
            Assert.assertEquals("Ah|je?;je|je|tests||", reading.get(1));
            Assert.assertEquals("Oui|je;APACHE|POI|tests||", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderCSVMixedButCommaPrevailling");
        }
    }

    @Test
    public void testReaderCSVOneColumn() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestCsvOneColumn.csv").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|", reading.get(0));
            Assert.assertEquals("Ah|", reading.get(1));
            Assert.assertEquals("Oui|", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderCSVOneColumn");
        }
    }

    @Test(expected = FileReaderException.class)
    public void testReaderCSVExceptionMultipleDelimiters() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestCsvMultipleDelimiters.csv").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je|je|je|tests|", reading.get(0));
            Assert.assertEquals("Ah|je?|je|je|tests|", reading.get(1));
            Assert.assertEquals("Oui,je,APACHE,POI,tests,", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderCSVExceptionMultipleDelimiters");
        }
    }

    @Test(expected = FileReaderException.class)
    public void testReaderCSVExceptionDelimiterUnknown() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestCsvUnbalanced.csv").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je|je|je|tests|", reading.get(0));
            Assert.assertEquals("Ah|je?|je|je|tests|", reading.get(1));
            Assert.assertEquals("Oui,je,APACHE,POI,tests,", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderCSVExceptionDelimiterUnknown");
        }
    }

    /**
     * Excel Part of tests
     */
    @Test
    public void testReaderExcel2003() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestExcel2003.xls").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je|je|je|tests|", reading.get(0));
            Assert.assertEquals("Ah|je?|je|je|tests|", reading.get(1));
            Assert.assertEquals("Oui|je|APACHE|POI|tests|", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderExcel2003");
        }
    }

    @Test
    public void testReaderExcel2007() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestExcel2007.xlsx").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je|je|je|tests|", reading.get(0));
            Assert.assertEquals("Ah|je?|je|je|tests|", reading.get(1));
            Assert.assertEquals("Oui|je|APACHE|POI|tests|", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderExcel2007");
        }
    }

    @Test
    public void testReaderExcelMacro2007() {
        
        try {
            fileReaderService.process(new ClassPathResource("reader/TestExcelMacro2007.xlsm").getFile(), lineProcessor, charsetName);
            Assert.assertEquals("Salut|je|je|je|tests|", reading.get(0));
            Assert.assertEquals("Ah|je?|je|je|tests|", reading.get(1));
            Assert.assertEquals("Oui|je|APACHE|POI|tests|", reading.get(2));
        } catch (IOException e) {
            throw new FileReaderException("IO exception during testReaderExcelMacro2007");
        }
    }
}
