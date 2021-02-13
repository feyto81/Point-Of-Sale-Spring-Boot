package com.crocodic.koperasi.controller;


import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.SaleDetails;
import com.crocodic.koperasi.models.Sales;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.repository.SalesRepository;
import com.crocodic.koperasi.services.SaleDetailsService;
import com.crocodic.koperasi.services.SaleService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(AdminUrlString.TRANSAKSIPENJUALAN)
public class TransaksiPenjualanController {

    Helpers help = new Helpers();

    @Autowired
    private SaleDetailsService service;

    @Autowired
    private SaleService saleService;

    @Autowired
    private SalesRepository salesRepository;


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
        ct.setColumnsName("date");
        columns.add(ct);


        ct = new ColumnsTable();
        ct.setName("Faktur");
        ct.setColumnsName("invoice");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Total");
        ct.setColumnsName("final_price");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Customer");
        ct.setColumnsName("customer");
        columns.add(ct);





        return  columns;
    }

    public List<ButtonAction> buttonActions(){
        List<ButtonAction> button = new ArrayList<>();
        ButtonAction bt;

        bt = new ButtonAction();
        bt.setText("Tambah Data");
        bt.setIcon("fas fa-plus-circle");
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
        Page<Sales> data = saleService.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
//        Date currentSqlDate = new Date(System.currentTimeMillis());
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
//        String date = sdf1.format(currentSqlDate);
//        Calendar cal = Calendar.getInstance();
//        System.out.println(new SimpleDateFormat("MMM").format(cal.getTime()));
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-yyyy");
        String month = sdf2.format(currentSqlDatee);
        Year ee = Year.now(ZoneId.of("Asia/Jakarta"));
        String y = String.valueOf(ee);
        String total = salesRepository.selectTotal2();
        double d=Double.parseDouble(total);
        String totall = help.rupiah(d);
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf1.format(currentSqlDate);
        model.addAttribute("page_title","History Sale");
        model.addAttribute("data",data);
        model.addAttribute("paginate",paginate);
        model.addAttribute("columnsTable",ct);
        model.addAttribute("buttonAction",bt);
        model.addAttribute("url", AdminUrlString.TRANSAKSIPENJUALAN);
        model.addAttribute("help",help);
        model.addAttribute("total",totall);
        model.addAttribute("date",date);
        model.addAttribute("year",y);
        model.addAttribute("month",month);
        return help.views("transaksipenjualan/tables");



    }

    @PostMapping("action-selected")
    private String postActionSelected(
            @RequestParam(name = "idSelected",required = false) Long [] idSelected,
            RedirectAttributes attr,
            HttpServletRequest request
    )throws Exception{
        if (idSelected != null) {
            for (Long id : idSelected) {
                saleService.delete(id);
            }
            attr.addFlashAttribute("success", "Berhasil menghapus data terpilih");
            help.message(attr,"Berhasil menghapus data terpilih","success");
        } else {
            attr.addFlashAttribute("error", "Mohon pilih salah satu data terlebih dahulu");
        }
        return "redirect:"+AdminUrlString.TRANSAKSIPENJUALAN;
    }

    @RequestMapping(value = "nota/{sale_id}",method = RequestMethod.GET)
    private String getPrintOrder(
            @PathVariable(name = "sale_id") Long sale_id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request,
            Model model,
            HttpSession session
    )throws Exception, IOException {
        Optional<Sales> sales = salesRepository.findById(sale_id);
        String s=String.valueOf(sale_id);
        List<SaleDetails> listDetails = service.listAll(s);
        String date = sales.get().getDate();
        if (sales != null) {
            String created_at = sales.get().getCreated_at();
            Long sale_idd = sales.get().getSale_id();
            String invoice = sales.get().getInvoice();
            String kasir = sales.get().getCmsUsers().getName();
            String customer = sales.get().getCustomer();
            Long order_id = sales.get().getSale_id();
            String total_price = sales.get().getTotal_price();
            String discount = sales.get().getDiscount();
            Integer final_price = sales.get().getFinal_price();
            String cash = sales.get().getCash();
            String remaining = sales.get().getRemaining();
            String note = sales.get().getNote();
            model.addAttribute("date",date);
            model.addAttribute("page_title","Nota");
            model.addAttribute("invoice",invoice);
            model.addAttribute("kasir",kasir);
            model.addAttribute("customer",customer);
            model.addAttribute("order_id",order_id);
            model.addAttribute("total_price",total_price);
            model.addAttribute("discount",discount);
            model.addAttribute("final_price",final_price);
            model.addAttribute("cash",cash);
            model.addAttribute("remaining",remaining);
            model.addAttribute("note",note);
            model.addAttribute("listDetails",listDetails);
            model.addAttribute("sale_id",sale_idd);
            model.addAttribute("created_at",created_at);
            model.addAttribute("url", AdminUrlString.MANAGESALES);
            return help.views("transaksipenjualan/nota");
        }
        model.addAttribute("date",date);
        model.addAttribute("page_title","Nota");
        System.out.println("sale_Id = "+sale_id);
        return help.views("transaksipenjualan/nota");

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
        Page<Sales> data = saleService.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        Year ee = Year.now(ZoneId.of("Asia/Jakarta"));
        String y = String.valueOf(ee);
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-yyyy");
        String month = sdf2.format(currentSqlDatee);
        String tota = salesRepository.selectTotal(date);
        if (tota != null) {
            double d=Double.parseDouble(tota);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Penjualan");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.TRANSAKSIPENJUALAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",y);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("transaksipenjualan/tables");
        } else {
            String tote = "0";
            double d=Double.parseDouble(tote);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Transaksi Penjualan");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.TRANSAKSIPENJUALAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",y);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("transaksipenjualan/tables");
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
        Page<Sales> data = saleService.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf1.format(currentSqlDate);
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-yyyy");
        String month = sdf2.format(currentSqlDatee);
        String tota = salesRepository.selectTotalByYear(year);
        if (tota != null) {
            double d=Double.parseDouble(tota);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Penjualan");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.TRANSAKSIPENJUALAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",year);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("transaksipenjualan/tables");
        } else {
            String tote = "0";
            double d=Double.parseDouble(tote);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Transaksi Penjualan");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.TRANSAKSIPENJUALAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",year);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("transaksipenjualan/tables");
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
        Page<Sales> data = saleService.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf1.format(currentSqlDate);
        Year ee = Year.now(ZoneId.of("Asia/Jakarta"));
        String y = String.valueOf(ee);
        String tota = salesRepository.selectTotalByMonth(month);
        if (tota != null) {
            double d=Double.parseDouble(tota);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Transaksi Penjualan");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.TRANSAKSIPENJUALAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",y);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("transaksipenjualan/tables");
        } else {
            String tote = "0";
            double d=Double.parseDouble(tote);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Transaksi Penjualan");
            model.addAttribute("data",data);
            model.addAttribute("paginate",paginate);
            model.addAttribute("columnsTable",ct);
            model.addAttribute("buttonAction",bt);
            model.addAttribute("url", AdminUrlString.TRANSAKSIPENJUALAN);
            model.addAttribute("help",help);
            model.addAttribute("total",total);
            model.addAttribute("year",y);
            model.addAttribute("date",date);
            model.addAttribute("month",month);
            return help.views("transaksipenjualan/tables");
        }
    }
}
