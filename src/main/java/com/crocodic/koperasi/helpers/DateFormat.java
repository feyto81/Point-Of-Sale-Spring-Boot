package com.crocodic.koperasi.helpers;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Service
public class DateFormat {

    String formatDateIndo = "dd MMMM yyyy HH:mm:ss";
//            =================================================
//    dd/MM/yy         ---> 06/04/14
//    dd MMMM yyyy         ---> 06 April 2014
//    EEEE, dd MMMM, ''yy  ---> Minggu, 06 April, '14
//    h:mm a           ---> 6:19 PM
//    H:mm             ---> 18:19
//    H:mm:ss:SSS      ---> 18:19:48:838
//    yyyy.MM.dd G 'at' hh:mm:ss z         ---> 2014.04.06 CE at 06:19:48 ICT
//    yyyy.MMMMM.dd GGG hh:mm aaa      ---> 2014.April.06 CE 06:19 PM
//    dd MMMM yyyy', Pukul' HH:mm:ss:SSSSS     ---> 06 April 2014, Pukul 18:19:48:00838
//    EEEE, dd MMMM, yyyy          ---> dimanche, 06 avril, 2014 (Lokal yng dipakai = French (France))
//    Dalam format timestamp = 1396783188838

    private static Locale lokal = null;
    private static String pola = "yyyy/MM/dd HH:mm:ss";
    private static Date sekarang = new Date();

    public static Date nowFromTimeZone(HttpServletRequest request) throws Exception {
        String zone = request.getHeader("Time-Zone");
        String zona = "wib";
        Calendar calExpiredDate = DateFormat.tambahWaktu(sekarang,0, "jam");
        if (zone!=null){
            zona = zone.toLowerCase();
        }
        if (zona.equals("wita")){
            calExpiredDate = DateFormat.tambahWaktu(sekarang,1, "jam");
        }else if (zona.equals("wit")){
            calExpiredDate = DateFormat.tambahWaktu(sekarang,2, "jam");
        }
        String newDate = DateFormat.tampilkanTanggalDanWaktu(calExpiredDate.getTime(), pola, lokal);
        SimpleDateFormat sdfDate = new SimpleDateFormat(pola);
        return sdfDate.parse(newDate);
    }


    public static Date getDateWithoutTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    public static Date getTomorrowDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }


    public static String currentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String currentDate() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static Date currentDates() throws ParseException {
        String pola = "yyyy-MM-dd";
        SimpleDateFormat sdfDate = new SimpleDateFormat(pola);//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return sdfDate.parse(strDate);
    }

    public static Integer getBulan(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM");//dd/MM/yyyy
        String strDate = sdfDate.format(date);
        return Integer.valueOf(strDate);
    }

    public static String dateFormat(Date date,String format) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(format);//dd/MM/yyyy
        String strDate = sdfDate.format(date);
        return strDate;
    }

    public static Date stringToDate(String date,String format) throws ParseException {
        return new SimpleDateFormat(format).parse(date);
    }


    public String getBulanNama(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MMMM");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(date);
        return strDate;
    }

    public static Integer getTahun(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(date);
        return Integer.valueOf(strDate);
    }

    public static Integer getTanggal(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(date);
        return Integer.valueOf(strDate);
    }

    public String getTahunBulan(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM");//dd/MM/yyyy
        String strDate = sdfDate.format(date);
        return strDate;
    }



    public static String rawPretty(String dateRaw){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(dateRaw);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date!=null) {
            //DateFormat formatter_show = new SimpleDateFormat("d MMMM yyyy, HH:mm");
            //DateFormat formatter_show = new SimpleDateFormat("EEEE, d MMM yyyy");
            SimpleDateFormat formatter_show = new SimpleDateFormat("d MMMM yyyy");
            return formatter_show.format(date);
        }

        return "";
    }

    public static String convert(String dateRaw, String source, String to){
        if (dateRaw == null) return "";
        if (dateRaw.isEmpty()) return "";
        SimpleDateFormat formatter = new SimpleDateFormat(source);
        Date date = null;
        try {
            date = formatter.parse(dateRaw);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date!=null) {
            SimpleDateFormat formatter_show = new SimpleDateFormat(to);
            return formatter_show.format(date);
        }

        return "";
    }

    public static String convertFromUTC(String dateRaw, String source, String to){
        if (dateRaw == null) return "";
        if (dateRaw.isEmpty()) return "";
        SimpleDateFormat formatter = new SimpleDateFormat(source);
        Date date = null;
        try {
            date = formatter.parse(dateRaw);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String tz = timeZone().trim().split(":")[0];

        int diff = 0;

        try {
            diff = Integer.parseInt(tz.replace("+","").replace("-",""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tz.contains("+")) {
            calendar.add(Calendar.HOUR, diff);
        } else {
            calendar.add(Calendar.HOUR, 0-diff);
        }

        if(date!=null) {
            SimpleDateFormat formatter_show = new SimpleDateFormat(to);
            return formatter_show.format(calendar.getTime());
        }

        return "";
    }

    public static String convertToUTC(String dateRaw, String source, String to){
        if (dateRaw == null) return "";
        if (dateRaw.isEmpty()) return "";
        SimpleDateFormat formatter = new SimpleDateFormat(source);
        Date date = null;
        try {
            date = formatter.parse(dateRaw);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date!=null) {
            SimpleDateFormat formatter_show = new SimpleDateFormat(to);
            formatter_show.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter_show.format(date);
        }

        return "";
    }

    /*public static String lastWeek(){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", EN);

        Calendar calendar = Calendar.getInstance();

        int dayFromMonday = (calendar.get(Calendar.DAY_OF_WEEK) + 7 - Calendar.MONDAY) % 7;
        calendar.add(Calendar.DATE, -dayFromMonday-1);

        return formatter.format(calendar.getTime());
    }*/

    public static String lastWeek(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);

        return formatter.format(calendar.getTime());
    }

    public static String yesterday(int day){
        if (day==0) day = 1;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -day);

        return formatter.format(calendar.getTime());
    }

    public static String timeZone(){
        SimpleDateFormat formatter = new SimpleDateFormat("ZZZZZ");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static String today(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static String todayComplete(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static String timeToday(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static String hourToday(){
        SimpleDateFormat formatter = new SimpleDateFormat("H");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static String timeTodayAmPm(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:a");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static String timeToday(int hour){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hour);
        return formatter.format(calendar.getTime());
    }

    public static String createAt(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = Calendar.getInstance().getTime();
        return formatter.format(date);
    }

    public static String todayUTC(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static String timeTodayUTC(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static int bulanNow(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        Date date = Calendar.getInstance().getTime();
        return Integer.parseInt(formatter.format(date));
    }

    public static Date strToDate(String dateRaw){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(dateRaw);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date!=null) {
            return date;
        }

        return null;
    }

    public static Date strToTime(String dateRaw){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = formatter.parse(dateRaw);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date!=null) {
            return date;
        }

        return null;
    }


    public static Calendar tambahWaktu(Date waktuPermulaan,int jmlTambahanWaktu, String satuan) {
        /*
         * Untuk mengurangi hari gunakan nilai minus (-) pada jmlTambahanWaktu
         */
        Calendar cal = Calendar.getInstance();
        cal.setTime(waktuPermulaan);
        switch (satuan) {
            case "hari":
                cal.add(Calendar.DATE, jmlTambahanWaktu);
                break;
            case "bulan":
                cal.add(Calendar.MONTH, jmlTambahanWaktu);
                break;
            case "tahun":
                cal.add(Calendar.YEAR, jmlTambahanWaktu);
                break;
            case "jam":
                cal.add(Calendar.HOUR, jmlTambahanWaktu);
                break;
            case "menit":
                cal.add(Calendar.MINUTE, jmlTambahanWaktu);
                break;
            case "detik":
                cal.add(Calendar.SECOND, jmlTambahanWaktu);
                break;
            case "milidetik":
                cal.add(Calendar.MILLISECOND, jmlTambahanWaktu);
                break;
        }
        return cal;
    }

    public static String tampilkanTanggalDanWaktu(Date tanggalDanWaktu, String pola, Locale lokal) {
        String tanggalStr;
        SimpleDateFormat formatter;
        if (lokal == null) {
            formatter = new SimpleDateFormat(pola);
        } else {
            formatter = new SimpleDateFormat(pola, lokal);
        }
        tanggalStr = formatter.format(tanggalDanWaktu);
        return tanggalStr;
    }

    public static long dateToLong(Date date) throws ParseException {
        long millis = date.getTime();
        return millis;
    }

}
