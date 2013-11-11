package com.avricot.cboost.service.project.reader;

/**
 *
 */

import com.avricot.cboost.service.project.reader.strategy.IReaderStrategy;
import com.avricot.cboost.service.project.reader.strategy.ReaderCSVStrategy;
import com.avricot.cboost.service.project.reader.strategy.ReaderExcelStrategy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;


@Service
public class FileReaderService {
    private static final HashMap<String, IReaderStrategy> strategies = new HashMap<String, IReaderStrategy>();

    static {
        strategies.put(".csv", new ReaderCSVStrategy());
        strategies.put(".xls", new ReaderExcelStrategy());
        strategies.put(".xlsx", new ReaderExcelStrategy());
        strategies.put(".xlsm", new ReaderExcelStrategy());
    }

    public static HashMap<String, IReaderStrategy> getStrategies() {
        return strategies;
    }

    /**
     * Applies processLine method (from ILineReader) to each line of the file.
     */
    public void process(File file, ILineReader lineProcessor, final String charsetName) {
        process(file, lineProcessor, charsetName, null);
    }

    /**
     * Applies processLine method (from ILineReader) to each line of the file.
     */
    public void process(File file, ILineReader lineProcessor, final String charsetName, final Integer lineMaxToRead) {
        // For CSV files, the sheet index is always equal to 0
        int position = file.getName().lastIndexOf(".");
        if (position == -1) {
            throw new FileReaderException("Cannot find any extension");
        }
        String extension = file.getName().substring(position);
        IReaderStrategy strategy = strategies.get(extension);
        strategy.process(file, lineProcessor, charsetName, lineMaxToRead);
    }

}