package com.avricot.cboost.utils;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.context.Phased;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Resource bundle helper.
 */
@Component
public class MsgHelper implements Phased {

    private static MessageSource source;

    public final static String get(final String code, final Object... str) {
        return source.getMessage(code, str, LocaleContextHolder.getLocale());
    }

    public final static String get(final String code, final String[] str) {
        return source.getMessage(code, str, LocaleContextHolder.getLocale());
    }

    public final static String get(final String code, final List<Object> args) {
        return source.getMessage(code, args == null ? null : args.toArray(), LocaleContextHolder.getLocale());
    }

    public final static String get(final String code) {
        return source.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    private MessageSource msgSource;

    public String getMessage(final String code, final List<String> args) {
        return msgSource.getMessage(code, args == null ? null : args.toArray(), LocaleContextHolder.getLocale());
    }

    public String getMessage(final String code, Object... args) {
        return getMessage(code, Collections.<String> emptyList());
    }

    @Inject
    public void setMsgSource(final MessageSource msgSource) {
        this.msgSource = msgSource;
        source = msgSource;
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
