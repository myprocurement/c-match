package com.avricot.cboost.domain.project;

import com.avricot.cboost.utils.EnumJson;
import com.avricot.cboost.utils.MsgHelper;

import java.util.regex.Pattern;

/**
 *
 */
public enum FieldType {
    ID("(siret|siren|id|tva|vat)"),
    NAME("(raison|social|name|nom|fournisseur|entreprise|company)"),
    ZIP_CODE("(code|postal|zip|CP)", "naf"),
    ADDRESS("(adresse|address)"),
    COUNTRY("(pays|country)");

    FieldType(final String pattern) {
        this(pattern, null);
    }

    FieldType(final String pattern, final String antiPattern) {
        this.headPattern = Pattern.compile(".*" + pattern + ".*", Pattern.CASE_INSENSITIVE);
        this.headAntiPattern = antiPattern == null ? null : Pattern.compile(".*" + antiPattern + ".*", Pattern.CASE_INSENSITIVE);
    }

    private final Pattern headAntiPattern;
    private final Pattern headPattern;

    public String getLabel() {
        return MsgHelper.get("enum.field.status." + name());
    }

    public EnumJson getJson() {
        return new EnumJson(name(), getLabel());
    }

    public Pattern getHeadPattern() {
        return headPattern;
    }

    public Pattern getHeadAntiPattern() {
        return headAntiPattern;
    }
}
