package com.crocodic.koperasi.helpers;

import com.crocodic.koperasi.aes.AES;
import com.crocodic.koperasi.models.helper.MonthModel;
import com.crocodic.koperasi.models.helper.YearModel;
import com.crocodic.koperasi.models.management.RoleMenus;
import com.crocodic.koperasi.services.management.CmsMenusService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    @Autowired
    ServletContext context;

    @Autowired
    public CmsMenusService cmsMenusService;


    private static String uploadDir = "/storage-crocodic/";
    private static String dirView = "/storage/";
    private static String baseView = "pages";

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");


    public String uploadDir(
            HttpServletRequest request
    ){
//        ServletContext context = request.getSession().getServletContext();
//        String uploadPath = context.getRealPath("") ;
        String uploadPath = System.getProperty("catalina.base");
        return uploadPath+uploadDir;
    }
    public String uploadDir(){
//        ServletContext context = request.getSession().getServletContext();
//        String uploadPath = context.getRealPath("") ;
        String uploadPath = System.getProperty("catalina.base");
        return uploadPath+uploadDir;
    }

    public String uploadBase64(
            String imageValue,
            String filename,
            String extension
    )  throws IOException
    {
        try {
            String uploadDir = uploadDir();
            try{
                imageValue = imageValue.split(",")[1];
            }catch (Exception e){

            }
            byte[] imageByte= Base64.decodeBase64(imageValue);
            String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageByte));

            if (extension.equals("")){
                extension = ".jpg";
            }
            if (filename.equals("")){
                filename = this.random(20);
            }
            filename = filename+extension;

            File folderTujuan = new File(uploadDir);
            if(!folderTujuan.exists()){
                folderTujuan.mkdirs();
            }
            String directory= uploadDir+filename;
            new FileOutputStream(directory).write(imageByte);
            String location = dirView+filename;
            System.out.println(directory);
            System.out.println(location);
            return  location;
        }catch (IOException e){
            System.out.println("UPLOAD BASE64 ERROR - >");
            e.printStackTrace();
            return null;
        }
    }

    public String uploadFile(
            HttpServletRequest request,
            MultipartFile file,
            String filename
    )  throws IOException{

        try {
            String uploadDir = uploadDir(request);
            String filenames  = file.getOriginalFilename();
            String ext  = FilenameUtils.getExtension(filenames);
            if (filename.equals("")){
                filename = this.random(15);
            }
            File folderTujuan = new File(uploadDir);
            if(!folderTujuan.exists()){
                folderTujuan.mkdirs();
            }

            filename = filename+"."+ext;

            File files = new File(uploadDir + filename);
            file.transferTo(files);

//            System.out.println("File sudah dicopy ke :"+ files.getAbsolutePath());
//            System.out.println(url(request,"/storage/"+filename));

            return dirView+filename;
        }catch (IOException e){
            System.out.println("UPLOAD FILE ERROR - >");
            e.printStackTrace();
            return null;
        }
    }

    public String random(int n){
        String AlphaNumericString = "AaBbCcDdEeFfGgHhIiJjKkLlM2m3N4n656OoPpQqRrSsTtUuVvWwXxYyZz0123456789aAbBcCdDeEfF1g65G7h8H9i98I9jJ5k3K6l87L6m3Mn6N6o7Op3Pq7Q8r88R8sS9t2T4uU5v76V89w0W0xXyYzZ";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length()* Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public  String views(String page){
        return baseView+"/"+page;
    }


    public String baseUrl(HttpServletRequest request) throws MalformedURLException {
        URL requestURL = new URL(request.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        String host =  requestURL.getProtocol() + "://" + requestURL.getHost() + port;
        String context = request.getContextPath();
        return host+context;
    }
    public String url(
            HttpServletRequest request,
            String pathurl
    ) throws MalformedURLException {
        URL requestURL = new URL(request.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        String host =  requestURL.getProtocol() + "://" + requestURL.getHost() + port;
        String context = request.getContextPath();
        return host+context+pathurl;
    }

    public List<String> renderPaginate(int c, int m){
        int current = c;
        int last = m;
        int delta = 2;
        int left = current - delta;
        int right = current + delta + 1;
        List<Integer> range = new ArrayList();
        List<String> rangeWithDots = new ArrayList();
        int l = 0;

        for (int i = 1; i <= last; i++) {
            if (i == 1 || i == last || i >= left && i < right) {
                range.add(i);
            }
        }
        for (int i : range) {
            if (l!=0) {
                if (i - l == 2) {
                    Integer val = l + 1;
                    rangeWithDots.add(String.valueOf(val));
                } else if (i - l != 1) {
                    rangeWithDots.add("...");
                }
            }
            rangeWithDots.add(String.valueOf(i));
            l = i;
        }
        return rangeWithDots;
    }

    public String message(
            RedirectAttributes attr,
            String pesan,
            String pesanType
    ){
        attr.addFlashAttribute("message", pesan);
        attr.addFlashAttribute("message_type", "alert-"+pesanType);
        return null;
    }

    public String encodeString(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public String decodedString(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public int coutRowExcel(Iterator<Row> iterator) {
        int size = 0;
        while(iterator.hasNext()) {
            Row row = iterator.next();
            size++;
        }
        return size;
    }

    //this function must static
    public static List<RoleMenus> roleMenus(HttpSession session){
        List<RoleMenus> list = (List<RoleMenus>) session.getAttribute("listMenus");
        return list;
    }




    public static String rupiah(Double harga){

        if (harga!=null){
            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');

            kursIndonesia.setDecimalFormatSymbols(formatRp);
            String x = kursIndonesia.format(harga);
            return x;
        }else{
            return "-";
        }
    }

    public static Long rupiahDouble(Double harga) throws ParseException {
        if (harga!=null){
            long nilai = (new Double(harga)).longValue();
            return nilai;
        }else{
            return Long.parseLong("0");
        }

    }

    public boolean isValidUrl(String url)
    {
        try {
            new URL(url).toURI();
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean isPrima(int n) {
        if (n == 2) {
            return true;
        }
        int squareRoot = (int)Math.sqrt(n);
        for (int i = 1; i <= squareRoot; i++) {
            if (n % i == 0 && i != 1) {
                return false;
            }
            return true;
        }
        return false;
    }

    public String apiSuccess(JSONArray data) throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",1);
        res.put("message","success");
        res.put("data",data);
//        return this.encryptParameter(res.toString());
        return res.toString();
    }

    public String apiSuccess(JSONObject data) throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",1);
        res.put("message","success");
        res.put("data",data);
//        return this.encryptParameter(res.toString());
        return res.toString();
    }

    public String apiSuccess(String pesan) throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",1);
        res.put("message",pesan);
//        return this.encryptParameter(res.toString());
        return res.toString();
    }

    public String apiSuccess(String data,int i) throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",1);
        res.put("message","success");
        res.put("data",data);
//        return this.encryptParameter(res.toString());
        return res.toString();
    }
    public String apiSuccess(JSONObject data,String pesan) throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",1);
        res.put("message",pesan);
        res.put("data",data);
//        return this.encryptParameter(res.toString());
        return res.toString();
    }

    public String apiError(String pesan) throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",0);
        res.put("message",pesan);
//        return this.encryptParameter(res.toString());
        return res.toString();
    }

    public String apiRespon(int status,String pesan) throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",status);
        res.put("message",pesan);
//        return this.encryptParameter(res.toString());
        return res.toString();
    }

    public String apiError(String pesan,Exception e) throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",0);
        res.put("message",pesan);
        res.put("error_track",e.getMessage());
//        return this.encryptParameter(res.toString());
        return res.toString();
    }

    public String inArray(String[] listArray,String def,String value){
        for(String val : listArray){
            if (value.equals(val)) {
                value = val;
                return value;
            }
        }
        return def;
    }

    public JSONObject decryptParameter(
            String parameter
    ) throws Exception{
        String parameterData = AES.Decrypt(parameter);
        JSONObject object = new JSONObject(parameterData);
        return object;
    }

    public JSONArray decryptParameterToJsonArray(
            String parameter
    ) throws Exception{
        String parameterData = AES.Decrypt(parameter);
        JSONArray arr = new JSONArray(parameterData);
        return arr;
    }

    public String decryptParameterToString(
            String parameter
    ) {
        String parameterData = AES.Decrypt(parameter);
        return parameterData;
    }

    public String encryptParameter(
            String parameter
    ) {
        String parameterData = AES.Encrypt(parameter);
        return parameterData;
    }

    public String makeSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static String findBulanActiveAtScheduleImgs(int currentBulan,int year)
    {
        try{
            int lastYear = year-1;
            String[] listBulan = new String[13];
            listBulan[0] = "";
            listBulan[1] = "Desember "+lastYear;
            listBulan[2] = "Januari "+year;
            listBulan[3] = "Februari "+year;
            listBulan[4] = "Maret "+year;
            listBulan[5] = "April "+year;
            listBulan[6] = "Mei "+year;
            listBulan[7] = "Juni "+year;
            listBulan[8] = "Juli "+year;
            listBulan[9] = "Agustus "+year;
            listBulan[10] = "September "+year;
            listBulan[11] = "Oktober "+year;
            listBulan[12] = "November "+year;
            return listBulan[currentBulan];
        }catch (Exception e){
            return null;
        }
    }

    public static List<YearModel> listTahun()
    {
        List<YearModel> listTahun = new ArrayList<>();
        Date date = new Date();
        int currentyear =  DateFormat.getTahun(date);
        int nextYear = currentyear+5;
        int lastYear = currentyear-5;
        for (int x = lastYear; x < currentyear; x++) {
            YearModel nl = new YearModel();
            nl.setTahun(x);
            listTahun.add(nl);
        }
        for (int i = currentyear; i <=nextYear; i++) {
            YearModel nn = new YearModel();
            nn.setTahun(i);
            listTahun.add(nn);
        }
        return listTahun;
    }
    public static List<MonthModel> listBulan()
    {
        List<MonthModel> listBulan = new ArrayList<>();
        for (int i = 1; i <=12; i++) {
            MonthModel nn = new MonthModel();
            String namaBulan = Helpers.namaBulan(i);
            nn.setMonth(i);
            nn.setName(namaBulan);
            listBulan.add(nn);
        }
        return listBulan;
    }

    public static String namaBulan(int bulan){
        try{
            String[] listBulan = new String[13];
            listBulan[1] = "Januari";
            listBulan[2] = "Februari";
            listBulan[3] = "Maret";
            listBulan[4] = "April";
            listBulan[5] = "Mei";
            listBulan[6] = "Juni";
            listBulan[7] = "Juli";
            listBulan[8] = "Agustus";
            listBulan[9] = "September";
            listBulan[10] = "Oktober";
            listBulan[11] = "November";
            listBulan[12] = "Desember";
            return listBulan[bulan];
        }catch (Exception e){
            return null;
        }
    }

    public static String randomString(int n){
        String AlphaNumericString = "AbcaXesfDghFiSjGFkUlImnUJYopVCqrsUtuIvwxYyzABCDEaFGaHaIJaKLcMtNyOuPQhRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length()* Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public static String randomInteger(int n){
        String AlphaNumeric = "0918273645546372819";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumeric.length()* Math.random());
            sb.append(AlphaNumeric.charAt(index));
        }
        return sb.toString();
    }

    public static boolean isValidEmail(String email)
    {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getRoleName(int role)
    {
        try{
            String[] listBulan = new String[6];
            listBulan[1] = "FA";
            listBulan[2] = "ASM";
            listBulan[3] = "RSM";
            listBulan[4] = "HOS";
            listBulan[5] = "Direktur";
            return listBulan[role];
        }catch (Exception e){
            return null;
        }
    }

    private Integer getTotalDaysInMonth(int year, int month)
    {
        Calendar calendar = Calendar.getInstance();
        int date = 1;
        calendar.set(year, month, date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }
}
