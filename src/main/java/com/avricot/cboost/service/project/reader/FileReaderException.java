package com.avricot.cboost.service.project.reader;

/**
 *
 */
public class FileReaderException extends RuntimeException {

    public FileReaderException(final String message, final Throwable e){
        super(message, e);
    }
    public FileReaderException(final String message){
        super(message);
    }
}
