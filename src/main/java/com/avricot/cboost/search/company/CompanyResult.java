package com.avricot.cboost.search.company;

import com.avricot.cboost.service.Company;

/**
 *
 */
public class CompanyResult extends Company {
    private Float score;

    public Float getScore() {
        return score;
    }

    public void setScore(final Float score) {
        this.score = score;
    }
}
