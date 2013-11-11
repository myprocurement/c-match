package com.avricot.cboost.service.project;

/**
 * Something went wrong during project initialization
 */
public class ProjectInitializationError extends RuntimeException {

    public ProjectInitializationError(String message, Throwable e){
        super(message, e);
    }
}
