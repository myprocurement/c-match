package com.avricot.cboost.utils;

import com.ibm.icu.text.Transliterator;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils extends org.springframework.util.StringUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);
    private final static Pattern emailPatter = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    private static final String TEXT_0 = "0";
    private static final String PUNCTUATION = "[,_;:/-]";
    private static final String MORE_THAN_ONE_UNDERSCORE = "_{2,}";
    private static final String UNDERSCORE = "_";
    private static final String NON_ALPHA_NUMERIC = "[^a-zA-Z_0-9]";
    private static final String ONE_SPACE = " ";
    private static final String AT_LEAST_ONE_SPACE = "[\\s]+";
    private static final String SLASH_AND_BACK_SLASH = "[\\/\\\\]";
    public static final Whitelist WHITE_LIST = Whitelist.relaxed().addTags("span").addAttributes(":all", "style");
    private static final Whitelist WHITE_LIST_NONE = Whitelist.none();
    private static final Whitelist WHITE_LIST_NONE_BUT_BR = Whitelist.none().addTags("br");
    /**
     * Read this <a href=
     * 'http://userguide.icu-project.org/transforms/general'>documentation</a>.
     */
    private static final Transliterator NORMALIZER = Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove; NFC;");
    private static final String NULL_STRING = "NULL";

    /**
     * No public constructor for utility class.
     */
    private StringUtils() {
    }

    /**
     * Return true if the email is valid.
     */
    public static boolean isEmailValid(final String email) {
        if (email == null) {
            return false;
        }
        final Matcher m = emailPatter.matcher(email);
        return m.matches();
    }

    /**
     * Removes all "/" and "\" and replaces them by a space, and removes all
     * useless spaces.
     */
    public static String removeAllSlashesAndUselessSpaces(final String path) {
        return StringUtils.removeUselessSpace(path.replaceAll(SLASH_AND_BACK_SLASH, ONE_SPACE));
    }

    /**
     * Clean Html to prevent XSS leaks. Return null when null.
     * unescape all htmlentities.
     */
    public static String cleanHtml(final String html) {
        if (html == null) {
            return null;
        }
        Document doc = Jsoup.parse(html);
        Document cleaner = new Cleaner(WHITE_LIST).clean(doc);
        cleaner.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        return StringEscapeUtils.unescapeXml(StringEscapeUtils.unescapeHtml4(cleaner.body().html()));
    }

    /**
     * Add spaces before/after < and > tags (otherwise jsoup will erase something like "<100€"
     */
    public static String addSpaceBeforeLowerUpper(final String html) {
        if(html == null){
            return null;
        }
        return html.replaceAll("<([^ ]{1})", "< $1").replaceAll("([^ ]{1})>", "$1 >");
    }

    /**
     * Replace \r\n by <br />
     * Clean html. Not supposed to be used for html !
     */
    public static String toBrCleaned(final String html) {
        if (html == null) {
            return null;
        }
        final String cleanHtml = addSpaceBeforeLowerUpper(html).replaceAll("(\r\n|\n)", "<br />");
        return StringUtils.removeHtmlKeepBr(cleanHtml);
    }

    /**
     * Remove Html but keep the brs tag.
     *
     * @param html
     * @return
     */
    public static String removeHtmlKeepBr(final String html) {
        if (html == null) {
            return null;
        }
        Document doc = Jsoup.parse(html);
        Document cleaner = new Cleaner(WHITE_LIST_NONE_BUT_BR).clean(doc);
        cleaner.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        // Jsoup add \n, find out why ?
        return cleaner.body().html().replaceAll("\n", "");
    }

    /**
     * Remove all html. TODO : not sure about perfs
     */
    public static String removeHtml(final String html) {
        if (html == null) {
            return null;
        }
        Document doc = Jsoup.parse(html);
        Document cleaner = new Cleaner(WHITE_LIST_NONE).clean(doc);
        cleaner.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        return StringEscapeUtils.unescapeXml(StringEscapeUtils.unescapeHtml4(cleaner.body().html()));
    }

    /**
     * Clean the string, usefull for tag. Removes trailing and leading
     * whitespace, lowercase and uppercase first letter.
     */
    public static String cleanString(final String str) {
        String cleanedStr = trimWhitespace(str);
        cleanedStr = cleanedStr.toLowerCase();
        cleanedStr = removeUselessSpace(cleanedStr);
        return capitalize(cleanedStr);
    }

    public static String removeWhiteSpace(final String str) {
        return org.springframework.util.StringUtils.trimAllWhitespace(str);
    }

    /**
     * Cleans the string.
     * <ul>
     * <li>remove the HTML markup</li>
     * <li>remove useless spaces ( see {@link #removeUselessSpace(String)} ).</li>
     * </ul>
     */
    public static String noHtmlAndUselessSpaces(final String text) {
        return StringUtils.removeUselessSpace(text.replaceAll("</?[^>]+(?://s*)?>", ONE_SPACE));
    }

    /**
     * Returns the acronym for the given text.
     * <p/>
     * Examples :
     * <ul>
     * <li>Marie-Charlotte Dupuis : mcd</li>
     * </ul>
     */
    public static String acronym(final String string) {
        final StringTokenizer tokenizer = new StringTokenizer(string, " \t\n\r\f-'");
        final StringBuilder acronym = new StringBuilder();

        while (tokenizer.hasMoreTokens()) {
            final String word = tokenizer.nextToken();

            if (word.length() > 1) {
                acronym.append(word.substring(0, 1));
            }
        }
        return acronym.toString();
    }

    /**
     * Removes the accents from the given text and returns it.
     */
    public static String removeAccents(final String text) {
        return NORMALIZER.transliterate(text);
    }

    /**
     * Transform an string with special char in a String without.
     */
    public static String noAccent(final String string) {
        if (string == null) {
            return null;
        }
        return StringUtils.removeAccents(string);
    }

    /**
     * Remove punctuation, accent and useless space.
     */
    public static String normalize(final String string) {
        if (string == null) {
            return null;
        }
        return StringUtils.noAccent(StringUtils.removeUselessSpace(StringUtils.removePunctuation(string)));
    }

    /**
     * Replaces simple punctuation with spaces.
     *
     * @warn . is a special case
     */
    public static String removePunctuation(final String string) {
        if (string == null) {
            return null;
        }
        return StringUtils.removeDot(string.replaceAll(PUNCTUATION, ONE_SPACE));
    }

    /**
     * Removes all the dot ('.') following theses rules.
     * <ul>
     * <li>A dot followed or preceded by a space are replaced by a simple space.
     * </li>
     * <li>A dot beginning or ending the string is replaced by empty.</li>
     * <li>A dot in two words (one of them having two or more letters) is
     * replace by empty.</li>
     * <li>All the others dots are replaced by empty.</li>
     * </ul>
     */
    public static String removeDot(final String string) {
        if (string == null) {
            return null;
        }
        return string.replaceAll("(?:\\.\\s)|(?:\\s\\.)", ONE_SPACE) //
                .replaceAll("(?:^\\.)|(?:\\.$)", "") //
                .replaceAll("(?:(\\w{2})\\.(\\w))|(?:(\\w)\\.(\\w{2}))", "$1$3 $2$4") //
                .replaceAll("\\.", "");
    }

    /**
     * Removes all useless space in the given string.
     */
    public static String removeUselessSpace(final String string) {
        if (string == null) {
            return null;
        }
        return org.springframework.util.StringUtils.trimWhitespace(string).replaceAll(AT_LEAST_ONE_SPACE, ONE_SPACE);
    }

    /**
     * Formats the text to a data base identifier (MY_ID).
     */
    public static String formatForDDB(final String original) {
        if (original == null) {
            return null;
        }
        return StringUtils.noAccent(original).toUpperCase().trim().replaceAll(NON_ALPHA_NUMERIC, UNDERSCORE).replaceAll(MORE_THAN_ONE_UNDERSCORE, UNDERSCORE);
    }

    /**
     * Return a string formatted for a css class ie that should match
     * -?[_a-zA-Z]+[_a-zA-Z0-9-]* .
     */
    public static String formatForCSSClass(final String original) {
        return StringUtils.noAccent(original).trim().replaceAll(NON_ALPHA_NUMERIC, UNDERSCORE).replaceAll(MORE_THAN_ONE_UNDERSCORE, UNDERSCORE);
    }

    /**
     * Returns a copy of the provided list with all the empty element not added.
     */
    public static List<String> removeWithoutText(final List<String> strings) {
        final List<String> copy = new ArrayList<String>(strings.size());
        for (final String string : strings) {
            if (org.springframework.util.StringUtils.hasText(string)) {
                copy.add(string);
            }
        }
        return copy;
    }

    /**
     * Returns true if the list contains (without carrying of case sensitivity)
     * the given string.
     */
    public static boolean containsIgnoreCase(final String string, final List<String> strings) {
        if (string == null) {
            return strings.contains(null);
        }
        for (final String element : strings) {
            if (string.equalsIgnoreCase(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Splits the given <tt>string</tt> using all spaces character (\\s+)
     * without accepting empty string and return the result in a never-null
     * list.<br/>
     * This methods is null-safe: returning empty list if given <tt>string</tt>
     * is null.
     */
    public static List<String> splitWithSpaces(final String string) {
        return splitWithSeparator(string, AT_LEAST_ONE_SPACE);
    }

    /**
     * Splits the given <tt>string</tt> using a comma as separator. without
     * accepting empty string and return the result in a never-null list.<br/>
     * This methods is null-safe: returning empty list if given <tt>string</tt>
     * is null.
     */
    public static List<String> splitWithComma(final String string) {
        return splitWithSeparator(string, ",");
    }

    /**
     * Splits the given <tt>string</tt> using a comma as separator. without
     * accepting empty string and return the result in a never-null list.<br/>
     * This methods is null-safe: returning empty list if given <tt>string</tt>
     * is null.
     */
    public static List<Long> splitWithCommaAsLong(final String string) {
        if(string == null){
            return Collections.<Long>emptyList();
        }
        final List<Long> longs = new ArrayList<Long>();
        for (final String str : splitWithSeparator(string, ",")) {
            final Long longValue = Long.valueOf(str);
            if (longValue != null) {
                longs.add(longValue);
            }
        }
        return longs;
    }

    /**
     * Splits the given <tt>string</tt> using the given separator. without
     * accepting empty string and return the result in a never-null list.<br/>
     * This methods is null-safe: returning empty list if given <tt>string</tt>
     * is null.
     */
    private static List<String> splitWithSeparator(final String string, final String separator) {
        if (string == null) {
            return new ArrayList<String>();
        }
        final List<String> result = new ArrayList<String>();

        for (final String s : string.split(separator)) {
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * @return true if the whole provided string is in upper case
     */
    public static boolean isInUpperCase(final String string) {
        return string.toUpperCase().equals(string);
    }

    /**
     * Return <tt>true</tt> if the given string has text and is not "NULL"
     * (ignore case).
     */
    public static boolean hasTextNotNullString(final String string) {
        return StringUtils.hasText(string) && !NULL_STRING.equalsIgnoreCase(string);
    }

    /**
     * Remove all the given characters from the begining of a string. Eg: "0003"
     * => 3
     */
    public static String removeFromBegining(final String characterToRemove, final String text) {
        String textModified = text;
        while (textModified.length() > 0 && TEXT_0.equals(textModified.substring(0, 1))) {
            textModified = textModified.substring(1, textModified.length());
        }
        return textModified;
    }

    /**
     * Return a copy of the text limited at the given size, or the text if <
     * size.
     */
    public static String limitText(final String text, final int size) {
        if (text == null) {
            return null;
        }
        if (text.length() > size) {
            return text.substring(0, size) + "...";
        }
        return text;
    }

    /**
     * Return a copy of the html limited at the given size, or the text if <
     * size (do not count html <entities></entities>).
     */
    public static String limitHtml(final String text, final int size) {
        if (text == null) {
            return null;
        }
        if (text.length() < size) {
            return text;
        } else {
            Document doc = Jsoup.parse(text);
            Integer count = 0;
            for (Node node : doc.childNodes()) {
                count += exploreNode(node, count, size);
            }
            if (count > size) {
                return doc.html() + "...";
            } else {
                return text;
            }
        }
    }

    private static Integer exploreNode(Node root, Integer count, int size) {
        List<Node> toRemove = new ArrayList<Node>();
        for (Node node : root.childNodes()) {
            if (count > size) {
                toRemove.add(node);
            } else {
                if (node instanceof TextNode) {
                    String content = ((TextNode) node).text();
                    content = content.replaceAll("\\\\(n|t)", "");
                    content = content.replaceAll("&nbps;", "");
                    count += content.length();
                }
                count = exploreNode(node, count, size);
            }
        }
        for (Node node : toRemove) {
            node.remove();
        }
        return count;
    }

    /**
     * Return true if the string start with a number.
     */
    public static boolean startWithNumber(final String str) {
        if (str.length() < 1) {
            return false;
        }
        return isNumber(str.substring(0, 1));
    }

    /**
     * Clean the string before number transformation.
     */
    public static String cleanStringNumber(final String str) {
        return str.replaceAll(",", ".").replaceAll( "[^\\d.]", "" );
    }

    /**
     * Return true if the string is a number.
     */
    public static boolean isNumber(final String str) {
        if(str == null || str.length()<1){
            return false;
        }
        final DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
        final char localeMinusSign = currentLocaleSymbols.getMinusSign();
        if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign || str.charAt(0) == localeMinusSign && str.length() == 1) {
            return false;
        }

        boolean isDecimalSeparatorFound = false;
        final char localeDecimalSeparatorComa = ',';
        final char localeDecimalSeparatorPoint = '.';
        for (final char c : str.substring(1).toCharArray()) {
            if (!Character.isDigit(c)) {
                if (c == localeDecimalSeparatorComa || c == localeDecimalSeparatorPoint && !isDecimalSeparatorFound) {
                    isDecimalSeparatorFound = true;
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Return the toString() method of the object, or "" if null.
     */
    public static String toStringNotNull(final Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    /**
     * Return the double as a string, with a precision of 1 digit if != 0.
     */
    public static String getAsString(final Double d) {
        if (d == null) {
            return "";
        }
        if (d - d.intValue() > 0) {
            return String.valueOf(Math.round(d * 10) / 10.0);
        }
        return String.valueOf(d.intValue());
    }


    /**
     * Return a list of enum from a list of string. If the string doesn't match
     * any enum, nothing is stored in the array for this value.
     */
    public static <E extends Enum<E>> List<E> getEnumFromStrings(final String[] strings, final Class<E> enumType) {
        return getEnumFromStrings(Arrays.asList(strings), enumType);
    }

    /**
     * Return a list of enum from a list of string. If the string doesn't match
     * any enum, nothing is stored in the array for this value.
     */
    public static <E extends Enum<E>> List<E> getEnumFromStrings(final List<String> strings, final Class<E> enumType) {
        final List<E> result = new ArrayList<E>();
        for (final String str : strings) {
            try {
                result.add(Enum.valueOf(enumType, str));
            } catch (final IllegalArgumentException e) {
                LOGGER.debug("can't build enum from string" + str + " and for enum" + enumType.getName(), e);
                // Do nothing
            }
        }
        return result;
    }

    /**
     * Remove the duplicated string from the list. Keep the order. Ignore the
     * case. Limit the result to the given value.
     */
    public static List<String> removeDuplicatedIgnoreCase(final List<String> list, final int maxResult) {
        final List<String> result = new ArrayList<String>(maxResult);
        final Set<String> distinct = new HashSet<String>();
        for (final String str : list) {
            if (distinct.add(str.toLowerCase())) {
                result.add(str);
                if (result.size() >= maxResult) {
                    break;
                }
            }
        }
        return result;

    }

    /**
     * Returns a string representation for the given list of phrases (All the
     * words are normalized and sorted).
     */
    public static String normalizeAndSortPhrases(final String... phrases) {
        if (ObjectUtils.isEmpty(phrases)) {
            return null;
        }
        final List<String> words = new ArrayList<String>();
        for (final String phrase : phrases) {
            if (StringUtils.hasText(phrase)) {
                words.addAll(Arrays.asList(StringUtils.normalize(phrase).toLowerCase().split(" ")));
            }
        }
        Collections.sort(words);
        return StringUtils.collectionToCommaDelimitedString(words);
    }

    /**
     * Display a int with k separator.
     */
    public static String getFormattedNumber(final Number num) {
        final DecimalFormat formatter = new DecimalFormat("#,###,###", new DecimalFormatSymbols(Locale.FRENCH));
        return formatter.format(num);
    }

    /**
     * Return a String representing the given collection, separated with ",".
     */
    public static String getStringFromCollection(final Collection<?> c) {
        return collectionToDelimitedString(c, ",");
    }

    /**
     * Return a Set representing the given String separated with ",".
     */
    public static Set<String> getCollectionFromString(final String s) {
        return commaDelimitedListToSet(s);
    }

    /**
     * Reduce the string name to max 255 char.
     */
    public static String reduceFileName(final String fileName) {
        if (fileName == null) {
            return fileName;
        }
        if (fileName.length() < 255) {
            return fileName;
        }
        final int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1 || (fileName.length() - lastDot) >= 254) {
            return fileName.substring(0, 254);
        }
        return trimWhitespace(fileName.substring(0, 254 - (fileName.length() - lastDot)) + fileName.substring(lastDot));
    }

    /**
     * Returns the well formated size of a file (Mb,Kb or b depending on the
     * value of filesize).
     */
    public static String getFileSize(final Number fileSize) {
        if (fileSize == null) {
            return "undefined";
        }
        float val = fileSize.floatValue();
        if(val == 0){
            return "0 Mb";
        }
        if (val > 1000000000) {
            return Math.round(val * 100 / 1073741824.0) / 100.0 + " Gb";
        } else if (val > 1000000) {
            return Math.round(val * 100 / 1048576.0) / 100.0 + " Mb";
        } else if (val > 1000) {
            return Math.round(val * 100 / 1024.0) / 100.0 + " Kb";
        } else {
            return val + " b";
        }
    }

    /**
     * Removes all the non digit character from a string.
     */
    public static String removeAlphaCharacter(final String text) {
        return text.replaceAll("[^\\d.]", "");
    }

    public static String getMoneyNumber(final String num) throws ParseException {
        final DecimalFormat formatter = new DecimalFormat("###,###.###", new DecimalFormatSymbols(Locale.FRENCH));
        return formatter.format(Double.valueOf(num));
    }

    /**
     * Clean the file name.
     */
    public static String cleanFileNameKeepAccent(final String fileName) {
        return StringUtils.reduceFileName(removeHtml(fileName).replaceAll("/", "-").replaceAll("[^a-zA-Z0-9. -_éàêïîà]", " "));
    }

    /**
     * Clean the file name and remove the accent (useful to avoid zip errors)
     */
    public static String cleanFileName(final String fileName) {
        return removeAccents(cleanFileNameKeepAccent(fileName));
    }

    /**
     * Set the first letter of a word to upper case.
     */
    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static String reduceTagSize(final String tags) {
        if (tags == null || tags.length() <= 254) {
            return tags;
        }
        String result = tags.substring(0, 254);
        int index = result.lastIndexOf(',');
        if (index != -1) {
            result = result.substring(0, index);
        }
        return result;
    }

    /**
     * Remove 4-byte unicode from the string, for mysql5 support.
     * TODO : upgrade to mysql 5.5 for non Basic Multilingual Plane support.
     * TODO :not sure about performances for big string.
     */
    public static String removeNonBmpStripped(final String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[\\ud800-\\udfff]", "");
    }

}
