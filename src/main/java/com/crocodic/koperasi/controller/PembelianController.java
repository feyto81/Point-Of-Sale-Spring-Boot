package com.crocodic.koperasi.controller;


import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.*;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.models.management.Activity;
import com.crocodic.koperasi.models.management.CmsUsers;
import com.crocodic.koperasi.repository.CartPembelianRepository;
import com.crocodic.koperasi.repository.KasRepository;
import com.crocodic.koperasi.repository.PembelianDetailsRepository;
import com.crocodic.koperasi.repository.PembelianRepository;
import com.crocodic.koperasi.services.*;
import com.crocodic.koperasi.string.AdminUrlString;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(AdminUrlString.PEMBELIAN)
public class PembelianController {

    Helpers help = new Helpers();

    @Autowired
    private SuppliersService suppliersService;

    @PersistenceContext
    EntityManager entityManager;



    @Autowired
    private ItemsService itemsService;

    @Autowired
    private CartPembelianService cartPembelianService;

    @Autowired
    private PembelianRepository pembelianRepository;

    @Autowired
    private CartPembelianRepository cartPembelianRepository;

    @Autowired
    private PembelianDetailsRepository pembelianDetailsRepository;

    @Autowired
    private KasRepository kasRepository;

    @Autowired
    private PembelianService pembelianService;

    @Autowired
    private PembelianDetailService pembelianDetailService;

    @Autowired
    private LogsService logsService;

    public Pageable par(String sortby, Integer offset, Integer limit){
        String[] array = sortby.split("\\-", -1);
        String value = (array[0]!=null)?array[0]:"sorting";
        String order = (array[1]!=null)?array[1]:"asc";
        Pageable pageable = null;
        offset = offset-1;
        try{
            if (order.equals("asc")){
                pageable = PageRequest.of(offset, limit, Sort.Direction.ASC,value);
            }else{
                pageable = PageRequest.of(offset, limit, Sort.Direction.DESC,value);
            }
        }catch (Exception e){
            pageable = PageRequest.of(offset, limit, Sort.Direction.DESC,"id");
            e.printStackTrace();
        }
        return pageable;
    }

    public List<ColumnsTable> columnsTable() throws JSONException {
        List<ColumnsTable> columns = new ArrayList<>();
        ColumnsTable ct;

        ct = new ColumnsTable();


        ct = new ColumnsTable();
        ct.setName("Tanggal");
        ct.setColumnsName("tanggalPembelian");
        columns.add(ct);


        ct = new ColumnsTable();
        ct.setName("Faktur");
        ct.setColumnsName("faktur");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Pembelian");
        ct.setColumnsName("total");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Supplier");
        ct.setColumnsName("suppliers.name");
        columns.add(ct);

        return  columns;
    }

    public List<ButtonAction> buttonActions(){
        List<ButtonAction> button = new ArrayList<>();
        ButtonAction bt;

        bt = new ButtonAction();
        bt.setText("Pembelian Barang");
        bt.setIcon("fa fa-plus-circle");
        bt.setKelas("btn btn-success btn-sm");
        bt.setLink("add");
        button.add(bt);


        return  button;
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    private String listTable(
            @RequestParam Optional<String> sortby,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<String> q,
            @RequestParam(value = "message", required = false) String message,
            HttpSession session,
            RedirectAttributes attr,
            Model model
    ) throws Exception {
        Pageable pageable = this.par(sortby.orElse("id-desc"),page.orElse(1),limit.orElse(10));
        Page<Pembelian> data = pembelianService.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        //

        //
        String date = sdf1.format(currentSqlDate);
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-yyyy");
        String month = sdf2.format(currentSqlDatee);
        Year ee = Year.now(ZoneId.of("Asia/Jakarta"));
        String y = String.valueOf(ee);
        String total = pembelianRepository.selectTotal2();
        Long iddd = Long.valueOf("3");

        if (total != null) {
            double d=Double.parseDouble(total);
            String totall = help.rupiah(d);
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("page_title","Pembelian");
            model.addAttribute("url", AdminUrlString.PEMBELIAN);
            model.addAttribute("date",date);
            model.addAttribute("total",totall);
            model.addAttribute("year",y);
            model.addAttribute("month",month);
            return help.views("pembelian/tables");
        } else {
            double d=Double.parseDouble("0");
            String totall = help.rupiah(d);
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("page_title","Pembelian");
            model.addAttribute("url", AdminUrlString.PEMBELIAN);
            model.addAttribute("date",date);
            model.addAttribute("total",totall);
            model.addAttribute("year",y);
            model.addAttribute("month",month);
            return help.views("pembelian/tables");
        }

    }

    @RequestMapping(value = "add",method = RequestMethod.GET)
    private String getAdd(Model model,HttpSession session) throws Exception{
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf1.format(currentSqlDate);
        Date datee = new Date(System.currentTimeMillis());
        long l = datee.getTime();
        //
        Date datet = new Date();
        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(datet);
        System.out.println(modifiedDate);
        //
        //
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String date3 = simpleDateFormat.format(currentSqlDate);
        //
        List<Suppliers> listSuppliers = suppliersService.listAll();
        List<Items> listItems = itemsService.listAll();
        String transaction_code = "PMB" + l;
//        Pembelian pembelian = new Pembelian();
//        PembelianDetails pembelianDetails = new PembelianDetails();
//        Long det = pembelianDetails.getId();
//        Long id = pembelian.getId();
//        System.out.println("id = " + det);
        model.addAttribute("url", AdminUrlString.PEMBELIAN);
        model.addAttribute("date",date);
        model.addAttribute("page_title","Tambah Pembelian");
        model.addAttribute("faktur",transaction_code);
        model.addAttribute("listSuppliers",listSuppliers);
        model.addAttribute("listItems",listItems);
        model.addAttribute("date3",date3);
//        model.addAttribute("pembelian_id",id);
        return help.views("pembelian/form");
    }
    @RequestMapping(value = "get",method = RequestMethod.GET)
    private String getData(Model model,HttpSession session) throws Exception{
        List<CartPembelian> carts = cartPembelianService.listAll();
        Pembelian cms = new Pembelian();
        Long id = cms.getId();
        model.addAttribute("carts",carts);
        model.addAttribute("cms",cms);
        model.addAttribute("pembelian_id",id);
        return help.views("pembelian/getData");
    }

    @RequestMapping(value = "save",method = RequestMethod.POST)
    private String postSavePembelian(
            @RequestParam(name = "faktur2",required = false) String faktur,
            @RequestParam(name = "sub_total", required = false) Integer sub_total,
            @RequestParam(name = "supplier2", required = false) Suppliers suppliers,
            @RequestParam(name = "tanggal_pembelian", required = false) String tanggal_pembelian,
            @RequestParam(name = "id_users", required = false) CmsUsers cmsUsers,
            @RequestParam(name = "pembelian_id", required = false) Pembelian p_id,
            @RequestParam(name = "date3", required = false) Date date3,
            @RequestParam Optional<String> edit,
            RedirectAttributes redirAttrs,
            HttpServletRequest request,
            String ip,
            Model model,
            HttpSession session
    )throws Exception, IOException {
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String created_at = sdf1.format(currentSqlDate);
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-yyyy");
        String month = sdf2.format(currentSqlDatee);

        Year t = Year.now(ZoneId.of("Asia/Jakarta"));
        String y = String.valueOf(t);
        Pembelian pembelian = new Pembelian();
        pembelian.setFaktur(faktur);
        pembelian.setTanggalPembelian(tanggal_pembelian);
        pembelian.setSuppliers(suppliers);
        pembelian.setTotal(sub_total);
        pembelian.setCmsUsers(cmsUsers);
        pembelian.setCreatedAt(created_at);
        pembelian.setYear(y);
        pembelian.setMonth(month);
//        pembelian.setDate(date3);
        pembelianRepository.save(pembelian);
        Long pembelian3 = pembelian.getId();
        String pembelian2 = String.valueOf(pembelian3);

        // Log Activity
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = help.getClientIpAddress(request);
        String url = request.getRequestURI();
        URL requestURL = new URL(request.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        String host = requestURL.getProtocol() + "://" + requestURL.getHost() + port;
        Date currentSqlDateee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String das = sdf5.format(currentSqlDateee);
        Object namee = session.getAttribute("name");
        InetAddress ipss = InetAddress.getByName(ip);
        String ips = ipss.getHostAddress();
        Activity activity = new Activity();
        activity.setIpAddress(ips);
        activity.setUserAgent(userAgent);
        activity.setUrl(host + url);
        activity.setCreated(das);
        activity.setCmsUsers(cmsUsers);
        activity.setDescription("Add Pembelian Item "+faktur+" at Supplier");
        logsService.save(activity);

        //
        Pembelian pembelian1 = new Pembelian();
        List<CartPembelian> carts = cartPembelianRepository.findAll();
        for (CartPembelian carts1 : carts) {
            Long cart_id = carts1.getId();
            Items items = carts1.getItems();
            Integer price = carts1.getPrice();
            Integer qty = carts1.getQty();
            Integer total = carts1.getSubtotal();
            String created_att = carts1.getCreatedAt();
            PembelianDetails pembelianDetails = new PembelianDetails();
            pembelianDetails.setCreatedAt(created_att);
            pembelianDetails.setItems(items);
            pembelianDetails.setQty(qty);
            pembelianDetails.setSubtotal(total);
            pembelianDetails.setPembelian(pembelian2);
            pembelianDetails.setPembelian1(p_id);
            pembelianDetailsRepository.save(pembelianDetails);
            cartPembelianService.delete(cart_id);
        }
        Kas kas = new Kas();
        kas.setTanggal(tanggal_pembelian);
        kas.setFaktur(faktur);
        kas.setType("pengeluaran");
        kas.setJenis("pembelian");
        kas.setPemasukan(0);
        kas.setPengeluaran(sub_total);
        kas.setKeterangan("Pembelian "+faktur);
        kas.setCreatedAt(created_at);
        kasRepository.save(kas);
        return "redirect:"+AdminUrlString.PEMBELIAN;
    }

    @RequestMapping(value = "today/{date}",method = RequestMethod.GET)
    private String listByDate(
            @PathVariable(name = "date") String date,
            @RequestParam Optional<String> sortby,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<String> q,
            @RequestParam(value = "message", required = false) String message,
            HttpSession session,
            RedirectAttributes attr,
            Model model
    ) throws Exception {
        Pageable pageable = this.par(sortby.orElse("id-desc"),page.orElse(1),limit.orElse(10));
        Page<Pembelian> data = pembelianService.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        Year ee = Year.now(ZoneId.of("Asia/Jakarta"));
        String y = String.valueOf(ee);
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-yyyy");
        String month = sdf2.format(currentSqlDatee);
        String tota = pembelianRepository.selectTotal(date);
        if (tota != null) {
            double d=Double.parseDouble(tota);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Pembelian");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.PEMBELIAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",y);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("pembelian/tables");
        } else {
            String tote = "0";
            double d=Double.parseDouble(tote);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Pembelian");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.PEMBELIAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",y);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("pembelian/tables");
        }
    }

    @RequestMapping(value = "year/{year}",method = RequestMethod.GET)
    private String listByYear(
            @PathVariable(name = "year") String year,
            @RequestParam Optional<String> sortby,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<String> q,
            @RequestParam(value = "message", required = false) String message,
            HttpSession session,
            RedirectAttributes attr,
            Model model
    ) throws Exception {
        Pageable pageable = this.par(sortby.orElse("id-desc"),page.orElse(1),limit.orElse(10));
        Page<Pembelian> data = pembelianService.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf1.format(currentSqlDate);
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-yyyy");
        String month = sdf2.format(currentSqlDatee);
        String tota = pembelianRepository.selectTotalByYear(year);
        if (tota != null) {
            double d=Double.parseDouble(tota);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Pembelian");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.PEMBELIAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",year);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("pembelian/tables");
        } else {
            String tote = "0";
            double d=Double.parseDouble(tote);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Pembelian");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.PEMBELIAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",year);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("pembelian/tables");
        }
    }

    @RequestMapping(value = "month/{month}",method = RequestMethod.GET)
    private String listByMonth(
            @PathVariable(name = "month") String month,
            @RequestParam Optional<String> sortby,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<String> q,
            @RequestParam(value = "message", required = false) String message,
            HttpSession session,
            RedirectAttributes attr,
            Model model
    ) throws Exception {
        Pageable pageable = this.par(sortby.orElse("id-desc"),page.orElse(1),limit.orElse(10));
        Page<Pembelian> data = pembelianService.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf1.format(currentSqlDate);
        Year ee = Year.now(ZoneId.of("Asia/Jakarta"));
        String y = String.valueOf(ee);
        String tota = pembelianRepository.selectTotalByMonth(month);
        if (tota != null) {
            double d=Double.parseDouble(tota);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Pembelian");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.PEMBELIAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",y);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("pembelian/tables");
        } else {
            String tote = "0";
            double d=Double.parseDouble(tote);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Pembelian");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.PEMBELIAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",y);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("pembelian/tables");
        }
    }

    @PostMapping("action-selected")
    private String postActionSelected(
            @RequestParam(name = "idSelected",required = false) Long [] idSelected,
            RedirectAttributes attr,
            HttpServletRequest request
    )throws Exception{
        if (idSelected != null) {
            for (Long id : idSelected) {
                pembelianService.delete(id);
            }
            attr.addFlashAttribute("success", "Berhasil menghapus data terpilih");
            help.message(attr,"Berhasil menghapus data terpilih","success");
        } else {
            attr.addFlashAttribute("error", "Mohon pilih salah satu data terlebih dahulu");
        }
        return "redirect:"+AdminUrlString.PEMBELIAN;
    }

    @RequestMapping(value = "nota/{id}",method = RequestMethod.GET)
    private String getPrintOrder(
            @PathVariable(name = "id") Long id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request,
            Model model,
            HttpSession session
    )throws Exception, IOException {
        Optional<Pembelian> pembelian = pembelianRepository.findById(id);
        String s=String.valueOf(id);
        List<PembelianDetails> listDetails = pembelianDetailService.listAll(s);
        String tanggal_pembelian = pembelian.get().getTanggalPembelian();

        if (pembelian != null) {
            String created_at = pembelian.get().getCreatedAt();
            Long pembelian_idd = pembelian.get().getId();
            String faktur = pembelian.get().getFaktur();
            String tanggal = pembelian.get().getTanggalPembelian();
            String supplier = pembelian.get().getSuppliers().getName();
            String alamat = pembelian.get().getSuppliers().getAddress();
            Integer total = pembelian.get().getTotal();
            model.addAttribute("tanggal",tanggal);
            model.addAttribute("page_title","Nota");
            model.addAttribute("faktur",faktur);
            model.addAttribute("supplier",supplier);
            model.addAttribute("alamat",alamat);
            model.addAttribute("total",total);
            model.addAttribute("listDetails",listDetails);
            model.addAttribute("created_at",created_at);
            model.addAttribute("url", AdminUrlString.MANAGESALES);
            return help.views("pembelian/nota");
        }
        String tanggal = pembelian.get().getTanggalPembelian();
        model.addAttribute("tanggal",tanggal);
        model.addAttribute("page_title","Nota");
        return help.views("pembelian/nota");

    }
}
