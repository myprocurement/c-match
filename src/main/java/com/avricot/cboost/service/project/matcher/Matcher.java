package com.avricot.cboost.service.project.matcher;

import com.avricot.cboost.domain.project.FieldType;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Matcher {
    private FieldType fieldType;
    private Condition condition = Condition.MUST;
    private Operator operator;
    private List<Matcher> matcher = new ArrayList<Matcher>();
    private List<Analyzer> analyzers = Lists.newArrayList(Analyzer.EXACT);
    public Matcher(FieldType fieldType){
    }

    public Matcher(String fieldType){
        this(FieldType.valueOf(fieldType.toUpperCase()));
    }

    public Matcher and(Matcher... matchers){
        return addMatcher(Operator.AND, matchers);
    }

    public Matcher or(Matcher... matcher){
        return addMatcher(Operator.OR, matcher);
    }

    private Matcher addMatcher(Operator operator, Matcher[] matchers){
        this.operator = operator;
        for(Matcher matcher : matchers){
            this.matcher.add(matcher);
        }
        return this;
    }

    public Matcher addAnalyzer(Analyzer... analyzers){
        for(Analyzer analyzer : analyzers){
            this.analyzers.add(analyzer);
        }
        return this;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(final FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(final Condition condition) {
        this.condition = condition;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(final Operator operator) {
        this.operator = operator;
    }

    public List<Matcher> getMatcher() {
        return matcher;
    }

    public void setMatcher(final List<Matcher> matcher) {
        this.matcher = matcher;
    }

    public static enum Operator{
        AND,OR;
    }
    public static enum Condition{
        MUST,SHOULD;
    }
    public static enum Analyzer{
        EXACT,STEMMING,NGRAM;
    }
}
