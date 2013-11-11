package com.avricot.cboost.utils;

/**
 *
 */
public class EnumJson {
    private String name;
    private String label;

    public EnumJson(final String name, final String label) {
        this.name = name;
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }
}
