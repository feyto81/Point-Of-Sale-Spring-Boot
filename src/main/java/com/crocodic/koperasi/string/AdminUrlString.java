package com.crocodic.koperasi.string;

public class AdminUrlString {

    public static final String adminUrl = "/admin";
    public static final String manage = "/management";

    public static final String addUrl = "add";
    public static final String saveUrl = "save";
    public static final String editUrl = "edit/{id}";
    public static final String detailUrl = "detail/{id}";
    public static final String importUrl = "import";
    public static final String exportUrl = "export";
    public static final String deleteUrl = "delete/{id}";
    public static final String actionSelectedUrl = "action-selected";



    public static final String DENIED = adminUrl+"/denied";

    //SUPER ADMIN
    public static final String MANAGEROLES = manage+"/privileges";
    public static final String MANAGEUSERS = manage+"/cms_users";
    public static final String MANAGEMENU = manage+"/cms_menus";
    public static final String LOGS = manage+"/cms_logs";
    public static final String saveMenuManagement = "save-menu";
    //end

    //ADMIN
    public static final String MANAGESUPPLIER = adminUrl+"/supplier";
    public static final String MANAGECUSTOMER = adminUrl+"/customer";
    public static final String MANAGECATEGORY = adminUrl+"/categories";
    public static final String MANAGEUNIT = adminUrl+"/unit";
    public static final String MANAGEITEM = adminUrl+"/item";
    public static final String MANAGESTOCKIN = adminUrl+"/stock-in";
    public static final String MANAGESTOCKOUT = adminUrl+"/stock-out";
    public static final String MANAGESALES = adminUrl+"/sale";
    public static final String TRANSAKSIPENJUALAN = adminUrl+"/penjualan";
    public static final String PEMBELIAN = adminUrl+"/pembelian";
    public static final String addPembelian = PEMBELIAN +"/"+ addUrl;
    public static final String KAS = adminUrl+"/kas";
    public static final String REPORTKAS = adminUrl + "/report-kas";
    public static final String REPORTPEMBELIAN = adminUrl + "/report-pembelian";
    public static final String REPORTPENJUALAN = adminUrl + "/report-penjualan";
    //end


    public static final String LOGIN = adminUrl+"/login";
    public static final String LOGOUT = adminUrl+"/logout";
    public static final  String HOME = adminUrl+"/home";

}
