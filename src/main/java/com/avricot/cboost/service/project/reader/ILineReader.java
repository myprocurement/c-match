package com.avricot.cboost.service.project.reader;

/**
 *
 */
public abstract class ILineReader<T> {
    public void processLine(Integer i, String[] split){
        processLine(i, split, null);
    }

    public T processLine(Integer i, String[] split, T lastLineResult){
        return null;
    }
}
