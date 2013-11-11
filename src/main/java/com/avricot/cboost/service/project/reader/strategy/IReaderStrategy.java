package com.avricot.cboost.service.project.reader.strategy;

import com.avricot.cboost.service.project.reader.ILineReader;

import java.io.File;

/**
 *
 */
public interface IReaderStrategy {

    void process(File file, ILineReader lineProcessor, String charsetName, final Integer lineMaxToRead);

}