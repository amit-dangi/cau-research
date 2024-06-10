// 
// Decompiled by Procyon v0.5.36
// 

package com.sits.general;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.util.Date;
import java.text.ParseException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import com.sits.conn.DBConnection;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;

public class General
{
    private static SimpleDateFormat inSDF;
    private static SimpleDateFormat outSDF;
    static Logger log;
    
    static {
        General.inSDF = new SimpleDateFormat("dd/mm/yyyy");
        General.outSDF = new SimpleDateFormat("yyyy-mm-dd");
        General.log = Logger.getLogger("exceptionlog");
    }
    
    public static String encPassword(String pass) {
        if (!pass.equals("")) {
            String newpass = "";
            int nVal1 = 0;
            final int nVal2 = 0;
            int j;
            for (int l1 = j = pass.length(); j < 10; ++j) {
                pass = String.valueOf(pass) + " ";
            }
            for (int i = pass.length(), k = 1; k <= i; ++k) {
                final char c = (char)(nVal1 = pass.charAt(k - 1));
                nVal1 += k;
                if (nVal1 == -39) {
                    newpass = String.valueOf(newpass) + "'||''''||'";
                }
                else {
                    newpass = String.valueOf(newpass) + (char)nVal1;
                }
            }
            newpass = newpass.trim();
            return newpass;
        }
        return "";
    }
    
    public static String decPassword(final String pass) {
        if (!pass.equals("")) {
            String ver1 = "";
            final int nLen = pass.length();
            int nVal1 = 0;
            int j1 = 1;
            for (int i = 1; i <= nLen; ++i) {
                char c;
                for (c = (char)(nVal1 = pass.charAt(i - 1)), nVal1 -= 0 + i; nVal1 < 0; nVal1 += 256 * j1, ++j1) {}
                ver1 = String.valueOf(ver1) + (char)nVal1;
            }
            ver1 = ver1.trim();
            return ver1;
        }
        return "";
    }
    
    public static String getClientIpAddr(final HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public static String PSTL(String PTSL) {
        if (PTSL != null) {
            PTSL = PTSL.replace('0', 'M');
            PTSL = PTSL.replace('1', 'C');
            PTSL = PTSL.replace('2', 'Z');
            PTSL = PTSL.replace('3', 'K');
            PTSL = PTSL.replace('4', 'L');
            PTSL = PTSL.replace('5', 'H');
            PTSL = PTSL.replace('6', 'Q');
            PTSL = PTSL.replace('7', 'J');
            PTSL = PTSL.replace('8', 'U');
            PTSL = PTSL.replace('9', 'W');
        }
        return PTSL;
    }
    
    public static String checknull(final String n) {
        if (n != null) {
            return n;
        }
        return "";
    }
    
    public static String checkXSS(String n) {
        if (n != null) {
            n = n.replace('<', ' ');
            n = n.replace('>', ' ');
            n = n.replace('\"', ' ');
            n = n.replace(';', ' ');
            n = n.replace('(', ' ');
            n = n.replace(')', ' ');
            n = n.replace('%', ' ');
            n = n.replace('&', ' ');
            n = n.replace('+', ' ');
        }
        return n;
    }
    
    public static String check_null(final String n) {
        if (n != null) {
            return checkXSS(n);
        }
        return "";
    }
    
    public static String getRandomPass() {
        final int charsToPrint = 4;
        final int charsToInt = 4;
        final int charsToSChar = 2;
        final String elegibleChars = "ABCDEFGHJKLMPQRSTUVWXYZabcdefhjkmnpqrstuvwxyz";
        final String elegibleInt = "123456789";
        final String elegibleSChar = "#$*";
        final char[] chars = elegibleChars.toCharArray();
        final StringBuffer finalString = new StringBuffer();
        final char[] ints = elegibleInt.toCharArray();
        final StringBuffer finalInt = new StringBuffer();
        final char[] SChar = elegibleSChar.toCharArray();
        final StringBuffer finalSChar = new StringBuffer();
        for (int i1 = 0; i1 < charsToPrint; ++i1) {
            final double randomValue = Math.random();
            final int randomIndex = (int)Math.round(randomValue * (chars.length - 1));
            final char characterToShow = chars[randomIndex];
            finalString.append(characterToShow);
        }
        for (int i2 = 0; i2 < charsToInt; ++i2) {
            final double randomValue = Math.random();
            final int randomIndex = (int)Math.round(randomValue * (ints.length - 1));
            final char intToShow = ints[randomIndex];
            finalInt.append(intToShow);
        }
        final String x = finalString.toString();
        final String x2 = finalInt.toString();
        final String PASSWORD = String.valueOf(x) + x2;
        return shuffle(PASSWORD);
    }
    
    public static String shuffle(final String input) {
        final List<Character> characters = new ArrayList<Character>();
        char[] charArray;
        for (int length = (charArray = input.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            characters.add(c);
        }
        final StringBuilder output = new StringBuilder(input.length());
        while (characters.size() != 0) {
            final int randPicker = (int)(Math.random() * characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }
    
    public static String getToken() {
        final int charsToPrint = 4;
        final int charsToInt = 4;
        final String elegibleChars = "ABCDEFGHJKLMPQRSTUVWXYZabcdefhjkmnpqrstuvwxyz";
        final String elegibleInt = "123456789";
        final char[] chars = elegibleChars.toCharArray();
        final StringBuffer finalString = new StringBuffer();
        final char[] ints = elegibleInt.toCharArray();
        final StringBuffer finalInt = new StringBuffer();
        for (int i1 = 0; i1 < charsToPrint; ++i1) {
            final double randomValue = Math.random();
            final int randomIndex = (int)Math.round(randomValue * (chars.length - 1));
            final char characterToShow = chars[randomIndex];
            finalString.append(characterToShow);
        }
        for (int i2 = 0; i2 < charsToInt; ++i2) {
            final double randomValue = Math.random();
            final int randomIndex = (int)Math.round(randomValue * (ints.length - 1));
            final char intToShow = ints[randomIndex];
            finalInt.append(intToShow);
        }
        final String x = finalString.toString();
        final String x2 = finalInt.toString();
        final String Token = String.valueOf(x) + x2;
        return getMD5(Token);
    }
    
    public static String getMD5(final String input) {
        byte[] source;
        try {
            source = input.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e2) {
            source = input.getBytes();
        }
        String result = null;
        final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            final byte[] temp = md.digest();
            final char[] str = new char[32];
            int k = 0;
            for (int i = 0; i < 16; ++i) {
                final byte byte0 = temp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xF];
                str[k++] = hexDigits[byte0 & 0xF];
            }
            result = new String(str);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static String isNumeric(final String no, final String errorMsg) {
        String msg = null;
        if (!checknull(no).equals("")) {
            final Pattern num = Pattern.compile("[(A-Za-z)]");
            final Matcher matcher = num.matcher(no);
            if (matcher.find()) {
                msg = errorMsg;
            }
            else {
                msg = "";
            }
        }
        return msg;
    }
    
    public static String isLength(final String str, final int len, final String errorMsg) {
        String msg = null;
        if (str.length() != len) {
            msg = errorMsg;
        }
        else {
            msg = "";
        }
        return msg;
    }
    
    public static String isDecimal(final String value, final String errorMsg) {
        String msg = null;
        if (value.length() > 0) {
            final Pattern num = Pattern.compile("[(A-Za-z)]");
            final Matcher matcher = num.matcher(value);
            if (!matcher.find()) {
                final int n = value.indexOf(".");
                final int m = value.indexOf(".");
                if (value.indexOf(".") != -1) {
                    if (n != m) {
                        msg = errorMsg;
                    }
                    else {
                        msg = "";
                    }
                }
                else {
                    msg = errorMsg;
                }
            }
            else {
                msg = errorMsg;
            }
        }
        else {
            msg = "";
        }
        return msg;
    }
    
    public static String isEmail(final String email, final String errorMsg) {
        String msg = null;
        if (email.length() > 0) {
            final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
            final Pattern p = Pattern.compile(regEx);
            final Matcher m = p.matcher(email);
            if (m.find()) {
                msg = "";
            }
            else {
                msg = errorMsg;
            }
        }
        else {
            msg = "";
        }
        return msg;
    }
    
    public static boolean isEmailId(final String email) {
        if (email.length() > 0) {
            final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
            final Pattern p = Pattern.compile(regEx);
            final Matcher m = p.matcher(email);
            return m.find();
        }
        return false;
    }
    
    public static boolean isNumber(final String no) {
        if (!checknull(no).equals("")) {
            final Pattern num = Pattern.compile("\\d+");
            final Matcher matcher = num.matcher(no);
            return matcher.find();
        }
        return true;
    }
    
    public static boolean IsDecimal(final String value) {
        if (value.length() <= 0) {
            return true;
        }
        final Pattern num = Pattern.compile("[(A-Za-z)]");
        final Matcher matcher = num.matcher(value);
        if (!matcher.find()) {
            final int n = value.indexOf(".");
            final int m = value.indexOf(".");
            return value.indexOf(".") != -1 && n == m;
        }
        return false;
    }
    
    public static boolean isAlphaNumeric(final String s) {
        final String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }
    
    public static boolean isPAN(final String pan) {
        if (pan.length() > 0) {
            final Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
            final Matcher matcher = pattern.matcher(pan);
            return matcher.find();
        }
        return false;
    }
    
    public static boolean isValidDate(final String date) {
        if (date.length() <= 0) {
            return true;
        }
        final Pattern pattern = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)");
        final Matcher matcher = pattern.matcher(date);
        if (!matcher.matches()) {
            return false;
        }
        matcher.reset();
        if (!matcher.find()) {
            return false;
        }
        final String day = matcher.group(1);
        final String month = matcher.group(2);
        final int year = Integer.parseInt(matcher.group(3));
        if (day.equals("31") && (month.equals("4") || month.equals("6") || month.equals("9") || month.equals("11") || month.equals("04") || month.equals("06") || month.equals("09"))) {
            return false;
        }
        if (!month.equals("2") && !month.equals("02")) {
            return true;
        }
        if (year % 4 == 0) {
            return !day.equals("30") && !day.equals("31");
        }
        return !day.equals("29") && !day.equals("30") && !day.equals("31");
    }
    
    public static String salttostring(final String SALT) {
        String PTSL = "";
        PTSL = SALT;
        PTSL = PTSL.replace('0', 'M');
        PTSL = PTSL.replace('1', 'C');
        PTSL = PTSL.replace('2', 'Z');
        PTSL = PTSL.replace('3', 'K');
        PTSL = PTSL.replace('4', 'L');
        PTSL = PTSL.replace('5', 'H');
        PTSL = PTSL.replace('6', 'Q');
        PTSL = PTSL.replace('7', 'J');
        PTSL = PTSL.replace('8', 'U');
        PTSL = PTSL.replace('9', 'W');
        return PTSL;
    }
    
    public static String stringtosalt(final String str) {
        String salt = "";
        salt = str;
        salt = salt.replace('M', '0');
        salt = salt.replace('C', '1');
        salt = salt.replace('Z', '2');
        salt = salt.replace('K', '3');
        salt = salt.replace('L', '4');
        salt = salt.replace('H', '5');
        salt = salt.replace('Q', '6');
        salt = salt.replace('J', '7');
        salt = salt.replace('U', '8');
        salt = salt.replace('W', '9');
        return salt;
    }
    
    public static String strtoMD5(String str) {
        try {
            MessageDigest mdAlgorithm = null;
            String plainText = "";
            plainText = str;
            mdAlgorithm = MessageDigest.getInstance("MD5");
            mdAlgorithm.update(plainText.getBytes());
            final byte[] digest = mdAlgorithm.digest();
            final StringBuffer hexString = new StringBuffer();
            for (int k = 0; k < digest.length; ++k) {
                plainText = Integer.toHexString(0xFF & digest[k]);
                if (plainText.length() < 2) {
                    plainText = "0" + plainText;
                }
                hexString.append(plainText);
            }
            str = hexString.toString();
        }
        catch (Exception ex) {}
        return str;
    }
    
    public static String capitalize(final String s) {
        if (s.length() == 0) {
            return s;
        }
        return String.valueOf(s.substring(0, 1).toUpperCase()) + s.substring(1).toLowerCase();
    }
    
    public static double roundTwoDecimal(final Double d) {
        final DecimalFormat twoDForm = new DecimalFormat("#############.00");
        return Double.valueOf(twoDForm.format(d));
    }
    
    private static String df4format(final String pattern, final Object value) {
        return new DecimalFormat(String.valueOf(pattern) + ".0000").format(value);
    }
    
    private static String df2format(final String pattern, final Object value) {
        return new DecimalFormat(String.valueOf(pattern) + ".##").format(value);
    }
    
    public static String df2format(final Double value) {
        if (value < 1000.0) {
            return df2format("###", value);
        }
        final Double hundreds = value % 1000.0;
        final int other = (int)(value / 1000.0);
        return String.valueOf(df2format(",##", other)) + ',' + df2format("000", hundreds);
    }
    
    public static String df4format(final Double value) {
        if (value < 1000.0) {
            return df4format("###", value);
        }
        final Double hundreds = value % 1000.0;
        final int other = (int)(value / 10000.0);
        return String.valueOf(df4format(",##", other)) + ',' + df4format("000", hundreds);
    }
    
    public static String getSeqNo(final String seq_name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String cSql = "";
        String seqNo = "";
        try {
            conn = DBConnection.getConnection();
            cSql = "select " + seq_name + ".nextval from dual";
            pstmt = conn.prepareStatement(cSql);
            rst = pstmt.executeQuery();
            if (rst.next()) {
                seqNo = rst.getString(1);
            }
        }
        catch (SQLException e) {
            System.out.println("Error in General.java(getSeqNo)01 : " + e.getMessage());
            try {
                if (pstmt != null) {
                    pstmt = null;
                }
                if (rst != null) {
                    rst = null;
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            return seqNo;
        }
        finally {
            try {
                if (pstmt != null) {
                    pstmt = null;
                }
                if (rst != null) {
                    rst = null;
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            if (pstmt != null) {
                pstmt = null;
            }
            if (rst != null) {
                rst = null;
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return seqNo;
    }
    
    public static String curr_date() {
        String curr_date = "";
        String sql = "";
        Connection conn = null;
        PreparedStatement mpstmt = null;
        ResultSet mrst = null;
        try {
            conn = DBConnection.getConnection();
            sql = "select date_format(now(),'%d-%m-%Y')";
            mpstmt = conn.prepareStatement(sql);
            mrst = mpstmt.executeQuery();
            if (mrst.next()) {
                curr_date = checknull(mrst.getString(1));
            }
            else {
                curr_date = "";
            }
        }
        catch (SQLException e) {
            System.out.println("curr_date (General.java) : " + e.getMessage());
            try {
                if (mrst != null) {
                    mrst.close();
                }
                if (mpstmt != null) {
                    mpstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            return curr_date;
        }
        finally {
            try {
                if (mrst != null) {
                    mrst.close();
                }
                if (mpstmt != null) {
                    mpstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            if (mrst != null) {
                mrst.close();
            }
            if (mpstmt != null) {
                mpstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return curr_date;
    }
    
    public static String currdate_time() {
        String curr_date = "";
        String sql = "";
        Connection conn = null;
        PreparedStatement mpstmt = null;
        ResultSet mrst = null;
        try {
            conn = DBConnection.getConnection();
            sql = "select date_format(now(),'%d-%m-%Y %H:%i:%s')";
            mpstmt = conn.prepareStatement(sql);
            mrst = mpstmt.executeQuery();
            if (mrst.next()) {
                curr_date = checknull(mrst.getString(1));
            }
            else {
                curr_date = "";
            }
        }
        catch (SQLException e) {
            System.out.println("curr_date (General.java) : " + e.getMessage());
            try {
                if (mrst != null) {
                    mrst.close();
                }
                if (mpstmt != null) {
                    mpstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            return curr_date;
        }
        finally {
            try {
                if (mrst != null) {
                    mrst.close();
                }
                if (mpstmt != null) {
                    mpstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            if (mrst != null) {
                mrst.close();
            }
            if (mpstmt != null) {
                mpstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return curr_date;
    }
    
    public static String formatDate(final String inDate) {
        String outDate = "";
        if (inDate != null) {
            try {
                final Date date = General.inSDF.parse(inDate);
                outDate = General.outSDF.format(date);
            }
            catch (ParseException ex) {
                System.out.println("Unable to format date: " + inDate + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return outDate;
    }
    
    public static Logger getLogger(final HttpServletRequest req) {
        final String requestUri = req.getRequestURI();
        final String jspName = requestUri.substring(requestUri.lastIndexOf(47));
        return Logger.getLogger(jspName);
    }
    
    public static String countSpace(final String n) {
        String n2 = "";
        if (!checknull(n).equals("") && !n.equals("") && n.length() > 0) {
            n2 = n.replaceAll("\\s+", " ");
            return n2;
        }
        return n2;
    }
    
    public static boolean isNullOrBlank(final String str) {
        return str == null || "".equals(str.trim());
    }
    
    public static Double getAmountAfterRounding(final String head_id, String amount) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "";
        String namt = "";
        String damt = "";
        double xamount = 0.0;
        double amt = 0.0;
        int n = 0;
        int aamt = 0;
        n = amount.indexOf(".");
        namt = amount.substring(0, n);
        damt = amount.substring(n);
        sql = " select rounding from salary_head_mast where salary_head_id = ? ";
        try {
            if (!head_id.equals("") && !amount.equals("")) {
                conn = DBConnection.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, head_id);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    amt = rs.getDouble(1);
                }
                if (amt == 0.0) {
                    xamount = Double.parseDouble(amount);
                }
                else if (amt == 0.5) {
                    if (Double.parseDouble(damt) >= 0.5) {
                        aamt = Integer.parseInt(namt) + 1;
                        amount = String.valueOf(aamt) + ".00";
                        xamount = Double.parseDouble(amount);
                    }
                    else if (Double.parseDouble(damt) >= 0.01 && Double.parseDouble(damt) < 0.5) {
                        amount = String.valueOf(Integer.parseInt(namt)) + ".00";
                        xamount = Double.parseDouble(amount);
                    }
                    else {
                        xamount = Double.parseDouble(amount);
                    }
                }
                else if (amt == 1.0) {
                    if (Double.parseDouble(damt) > 0.0) {
                        aamt = Integer.parseInt(namt) + 1;
                        amount = String.valueOf(aamt) + ".00";
                        xamount = Double.parseDouble(amount);
                    }
                    else {
                        xamount = Double.parseDouble(amount);
                    }
                }
                else if (amt == 10.0) {
                    if (Double.parseDouble(amount.substring(n - 1)) != 0.0) {
                        amt = Double.parseDouble(amount) + 10.0;
                        amount = String.valueOf(amt).substring(0, String.valueOf(amt).indexOf(".") - 1);
                        amount = String.valueOf(amount) + "0.00";
                        xamount = Double.parseDouble(amount);
                    }
                    else {
                        xamount = Double.parseDouble(amount);
                    }
                }
            }
        }
        catch (Exception ex2) {
            try {
                conn.close();
                pstmt.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        finally {
            try {
                conn.close();
                pstmt.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            conn.close();
            pstmt.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return xamount;
    }
    
    public static boolean checkAdharNumber(final String str) {
        final char ch = str.charAt(0);
        return '0' != ch && '1' != ch && str.length() <= 12 && str.length() >= 12 && isNumber(str);
    }
    
    public static String toCamelCase(String s) {
        s = countSpace(s);
        final String[] parts = s.split(" ");
        String camelCaseString = "";
        String[] array;
        for (int length = (array = parts).length, i = 0; i < length; ++i) {
            final String part = array[i];
            if (camelCaseString.equals("")) {
                camelCaseString = toProperCase(part);
            }
            else {
                camelCaseString = String.valueOf(camelCaseString) + " " + toProperCase(part);
            }
        }
        return camelCaseString;
    }
    
    public static String toProperCase(final String s) {
        return String.valueOf(s.substring(0, 1).toUpperCase()) + s.substring(1).toLowerCase();
    }
    
    public static Integer cntMenuPages(final String module, final String user_status, final String user_id) {
        String sql = "";
        int cntRecord = 0;
        Connection conn = null;
        PreparedStatement mpstmt = null;
        ResultSet mrst = null;
        try {
            conn = DBConnection.getConnection();
            if (user_status.equals("A")) {
                sql = "select count(*) from tree_menu where module=? and jsp_file is not null";
            }
            else {
                sql = " select count(*) from tree_menu where MENU_ID in ( ";
                sql = String.valueOf(sql) + " select page_id from web_user_acess where user_id = '" + user_id + "') and module=? ";
            }
            mpstmt = conn.prepareStatement(sql);
            mpstmt.setString(1, module);
            mrst = mpstmt.executeQuery();
            if (mrst.next()) {
                cntRecord = mrst.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("cntMenuPages (General.java) : " + e.getMessage());
            try {
                if (mrst != null) {
                    mrst.close();
                }
                if (mpstmt != null) {
                    mpstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            return cntRecord;
        }
        finally {
            try {
                if (mrst != null) {
                    mrst.close();
                }
                if (mpstmt != null) {
                    mpstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            if (mrst != null) {
                mrst.close();
            }
            if (mpstmt != null) {
                mpstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return cntRecord;
    }
    
    public static boolean compareDate(final String frmDate, final String toDate, final int param) {
        boolean flg = false;
        try {
            if (isValidDate(frmDate) && isValidDate(toDate)) {
                final String str1 = String.valueOf(frmDate.substring(6, 10)) + frmDate.substring(3, 5) + frmDate.substring(0, 2);
                final String str2 = String.valueOf(toDate.substring(6, 10)) + toDate.substring(3, 5) + toDate.substring(0, 2);
                final int x = Integer.parseInt(str1);
                final int y = Integer.parseInt(str2);
                flg = ((x <= y || param != 1) && (x < y || param != 0));
            }
            else {
                flg = false;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in General[compareDate]" + e.getMessage());
            return flg;
        }
        return flg;
    }
    
    public static String getGradeSheetPath() {
        Connection conn = null;
        PreparedStatement psmt = null;
        ResultSet rst = null;
        String qry = "";
        String path = "";
        try {
            conn = DBConnection.getConnection();
            qry = "select descp1 from cparam where code='GRADESHEET' and serial='PATH' and param1='Y'";
            psmt = conn.prepareStatement(qry);
            rst = psmt.executeQuery();
            if (rst.next()) {
                path = checknull(rst.getString("descp1"));
            }
        }
        catch (Exception e) {
            System.out.println("Exception in General[getGradeSheetPath] " + e.getMessage());
            return path;
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psmt != null) {
                    psmt.close();
                }
                if (rst != null) {
                    rst.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            if (conn != null) {
                conn.close();
            }
            if (psmt != null) {
                psmt.close();
            }
            if (rst != null) {
                rst.close();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return path;
    }
    
    public static int manageLog(final String formName, final String remarks, final String userId, final String machine) {
        Connection conn = null;
        PreparedStatement psmt = null;
        int cnt = 0;
        String qry = "";
        try {
            conn = DBConnection.getConnection();
            qry = "insert into log_mast (form,remarks,create_by,created,machine) values(?,?,?,now(),?)";
            psmt = conn.prepareStatement(qry);
            psmt.setString(1, checknull(formName));
            psmt.setString(2, checknull(remarks));
            psmt.setString(3, checknull(userId));
            psmt.setString(4, checknull(machine));
            cnt = psmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("Exception in General(manageLog) " + e.getMessage());
            return cnt;
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psmt != null) {
                    psmt.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            if (conn != null) {
                conn.close();
            }
            if (psmt != null) {
                psmt.close();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return cnt;
    }
    
    public static String userType(final String puser_id, final String puser_type) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String userDetail = "";
        String cSql = "";
        try {
            conn = DBConnection.getConnection();
            if (puser_type.trim().equals("CL")) {
                cSql = "select id as college_id, college_code, college_name from academic_college_profile_master ";
                cSql = String.valueOf(cSql) + "where id = (select college from user_type_mast where user_type_id = (select employeeid from user_mast ";
                cSql = String.valueOf(cSql) + "where user_id='" + puser_id + "' and user_type='" + puser_type + "'))";
                pstmt = conn.prepareStatement(cSql);
                rst = pstmt.executeQuery();
                if (rst.next()) {
                    userDetail = String.valueOf(checknull(rst.getString("college_id"))) + "~" + checknull(rst.getString("college_code")) + "~" + checknull(rst.getString("college_name"));
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception in General(userType) " + e.getMessage());
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return userDetail;
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            if (conn != null) {
                conn.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return userDetail;
    }
    
    public static String getDefaultDDOLocation(final String puser_type) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String defaultDDOLocation = "";
        String cSql = "";
        try {
            conn = DBConnection.getConnection();
            if (puser_type.trim().equals("CL")) {
                cSql = "select ddo_id, location_code, id from ddolocationmapping  ";
                cSql = String.valueOf(cSql) + "where id = (select pdoc from cparam where code='USER' and serial='DEFAULT') ";
                pstmt = conn.prepareStatement(cSql);
                rst = pstmt.executeQuery();
                if (rst.next()) {
                    defaultDDOLocation = String.valueOf(checknull(rst.getString("ddo_id"))) + "~" + checknull(rst.getString("location_code")) + "~" + checknull(rst.getString("id"));
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception in General(getDefaultDDOLocation) " + e.getMessage());
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return defaultDDOLocation;
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            if (conn != null) {
                conn.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return defaultDDOLocation;
    }
    
    public static boolean isValidFormat(final String format, final String value) {
        Date date = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        }
        catch (ParseException ex) {}
        return date != null && value.split("-")[2].length() <= 4;
    }
    
    public static boolean isAlphaNumericWithSpecialCharacters(final String s) {
        final String pattern = "^[a-zA-Z0-9!@#$^*]*$";
        return s.matches(pattern);
    }
    
    public static boolean isValidAlpha(final String str) {
        if (!checknull(str).equals("")) {
            final Pattern num = Pattern.compile("(?![0-9]*$)[a-zA-Z0-9]+$");
            final Matcher matcher = num.matcher(str);
            return matcher.find();
        }
        return true;
    }
    
    public static boolean isIfsc(final String s) {
        if (s.length() > 11 || s.length() < 11) {
            final String pattern = "^[A-Za-z]{4}[0]{1}[a-zA-Z0-9]{6}$";
            return s.matches(pattern);
        }
        return false;
    }
    
    public static void deleteFile(final File directory, final String fname) {
        if (directory.isDirectory()) {
            final File[] children = directory.listFiles();
            File[] array;
            for (int length = (array = children).length, i = 0; i < length; ++i) {
                final File child = array[i];
                if (child.getAbsoluteFile().toString().indexOf(fname) != -1) {
                    child.getAbsoluteFile().delete();
                }
            }
        }
    }
    
    public static double convertFileBytToMB(final double file_len) {
        return file_len / 1048576.0;
    }
    
    public static double convertFileBytToKB(final double file_len) {
        return file_len / 1024.0;
    }
    
    public static String getFileSize(final String cd, final String srl, final String dc, final String p) {
        Connection conn = null;
        PreparedStatement psmt = null;
        ResultSet rst = null;
        String qry = "";
        String doc_size = "";
        try {
            conn = DBConnection.getConnection();
            qry = "select pdoc, descp1 from cparam where code=? and serial=? and doc=? and param1 = ? ";
            psmt = conn.prepareStatement(qry);
            psmt.setString(1, cd);
            psmt.setString(2, srl);
            psmt.setString(3, dc);
            psmt.setString(4, p);
            rst = psmt.executeQuery();
            while (rst.next()) {
                doc_size = String.valueOf(rst.getInt("DESCP1")) + "~" + checknull(rst.getString("pdoc"));
            }
        }
        catch (Exception e) {
            System.out.println("Error in General[getFileSize] : " + e.getMessage());
            return doc_size;
        }
        finally {
            try {
                conn.close();
                psmt.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            conn.close();
            psmt.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return doc_size;
    }
    
    public static Image generateEANBarcode(final PdfWriter writer, final String text) {
        Image codeEANImage = null;
        try {
            final PdfContentByte cb = writer.getDirectContent();
            final BarcodeEAN barcodeEAN = new BarcodeEAN();
            barcodeEAN.setCode(text);
            barcodeEAN.setCodeType(1);
            barcodeEAN.setFont((BaseFont)null);
            codeEANImage = barcodeEAN.createImageWithBarcode(cb, (BaseColor)null, (BaseColor)null);
        }
        catch (Exception e) {
            System.out.println("Error in General[generateEANBarcode] : " + e.getMessage());
            e.printStackTrace();
        }
        return codeEANImage;
    }
    
    public static Connection updtDeletedData(final String tname, final String cn1, final String cn2, final String cn3, final String cn4, final String cn5, final String cn6, final String cv1, final String cv2, final String cv3, final String cv4, final String cv5, final String cv6, final String ip, final String uid, String schema, Connection conn) {
        PreparedStatement pstmt = null;
        String cSql = "";
        int cnt = 0;
        try {
            if (!checknull(schema).equals("")) {
                schema = String.valueOf(schema) + ".";
            }
            cSql = " update " + schema + tname + " set DELETED_BY = '" + uid + "', DELETED_IP = '" + ip + "', DELETED_DT = now() ";
            cSql = String.valueOf(cSql) + " where " + cn1 + "='" + cv1 + "'";
            if (!checknull(cn2).equals("") && !checknull(cv2).equals("")) {
                cSql = String.valueOf(cSql) + " and " + cn2 + "='" + cv2 + "'";
            }
            if (!checknull(cn3).equals("") && !checknull(cv3).equals("")) {
                cSql = String.valueOf(cSql) + " and " + cn3 + "='" + cv3 + "'";
            }
            if (!checknull(cn4).equals("") && !checknull(cv4).equals("")) {
                cSql = String.valueOf(cSql) + " and " + cn4 + "='" + cv4 + "'";
            }
            if (!checknull(cn5).equals("") && !checknull(cv5).equals("")) {
                cSql = String.valueOf(cSql) + " and " + cn5 + "='" + cv5 + "'";
            }
            if (!checknull(cn6).equals("") && !checknull(cv6).equals("")) {
                cSql = String.valueOf(cSql) + " and " + cn6 + "='" + cv6 + "'";
            }
            pstmt = conn.prepareStatement(cSql);
            System.out.println("pstmt"+pstmt);
            cnt = pstmt.executeUpdate();
            if (cnt == 0) {
                conn = null;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in General(updtDeletedData) " + e.getMessage());
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return conn;
        }
        finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }
    
    public static String formattingNumbersToIndianRupeeWithCommas(long amountln) {
	      String Amount = Long.toString(amountln);
	      int lenght = Amount.length();
	      StringBuffer Amountbuf = new StringBuffer(Amount);
	      if (amountln > 999L) {
	         for(int i = lenght; i > 0; i -= 2) {
	            if (i == lenght) {
	               i -= 3;
	               Amountbuf = Amountbuf.insert(i, ",");
	            } else {
	               Amountbuf = Amountbuf.insert(i, ",");
	            }
	         }
	      }

	      Amount = Amountbuf.toString();
	      return Amount;
	   }
}
