package com.avricot.cboost.search.company;

/**
 *
 */
public class CompanySearchException extends RuntimeException {

    public CompanySearchException(final String msg) {
        super(msg);
    }

    public CompanySearchException(final String msg, final Throwable e) {
        super(msg, e);
    }
}
