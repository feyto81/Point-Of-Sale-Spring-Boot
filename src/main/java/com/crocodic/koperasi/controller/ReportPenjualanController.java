package com.crocodic.koperasi.controller;


import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.Sales;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.repository.SalesRepository;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(AdminUrlString.REPORTPENJUALAN)
public class ReportPenjualanController {

    Helpers help = new Helpers();

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
        ct.setName("Foto");
        ct.setColumnsName("foto");
        columns.add(ct);


        ct = new ColumnsTable();
        ct.setName("Name");
        ct.setColumnsName("name");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Email");
        ct.setColumnsName("email");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Privileges");
        ct.setColumnsName("cmsPrivileges.name");
        columns.add(ct);





        return  columns;
    }

    public List<ButtonAction> buttonActions(){
        List<ButtonAction> button = new ArrayList<>();
        ButtonAction bt;

        bt = new ButtonAction();
        bt.setText("Tambah Data");
        bt.setIcon("fa fa-plus-circle");
        bt.setKelas("btn btn-white");
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
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        Pageable pageable = this.par(sortby.orElse("id-desc"),page.orElse(1),limit.orElse(10));
        Page<Sales> data = saleService.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String startdate = sdf1.format(currentSqlDate);
        String enddate = sdf1.format(currentSqlDate);
        List<Sales> listSales = salesRepository.findAll();
        String t = salesRepository.selectTotal3();
        double d=Double.parseDouble(t);
        String total = help.rupiah(d);


        model.addAttribute("page_title","Report Penjualan");
        model.addAttribute("url", AdminUrlString.REPORTPENJUALAN);
        model.addAttribute("columnsTable",ct);
        model.addAttribute("buttonAction",bt);
        model.addAttribute("help",help);
        model.addAttribute("listSales",listSales);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        model.addAttribute("total",total);
        return help.views("report/penjualan/tables");
    }

    @RequestMapping(value = "daterange",method = RequestMethod.GET)
    private String lists(
            @RequestParam(name = "startdate",required = true) String startdate,
            @RequestParam(name = "enddate",required = true) String enddate,
            Model model
    ) throws Exception{
        List<Sales> l = salesRepository.findAllByTanggal(startdate,enddate);
        String t = salesRepository.selectTotalByDateBeetween(startdate,enddate);
        if (t != null) {
            double d=Double.parseDouble(t);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Report Penjualan");
            model.addAttribute("url", AdminUrlString.REPORTPENJUALAN);
            model.addAttribute("help",help);
            model.addAttribute("listSales",l);
            model.addAttribute("startdate",startdate);
            model.addAttribute("enddate",enddate);
            model.addAttribute("total",total);
            return help.views("report/penjualan/tables");
        } else {
            String e = "0";
            double d = Double.parseDouble(e);
            String total = help.rupiah(d);
            model.addAttribute("page_title","Report Penjualan");
            model.addAttribute("url",AdminUrlString.REPORTPENJUALAN);
            model.addAttribute("help",help);
            model.addAttribute("listSales",l);
            model.addAttribute("startdate",startdate);
            model.addAttribute("enddate",enddate);
            model.addAttribute("total",total);
            return help.views("report/penjualan/tables");
        }

    }

    @RequestMapping(value = "print/startdate={startdate}&enddate={enddate}",method = RequestMethod.GET)
    private String listss(
            @PathVariable(name = "startdate") String startdate,
            @PathVariable(name = "enddate") String enddate,
            Model model,
            HttpServletRequest request
    ) throws Exception{
        List<Sales> l = salesRepository.findAllByTanggal(startdate,enddate);
        String pendapata = salesRepository.selectTotalByDateBeetween(startdate,enddate);

        double k = Double.parseDouble(pendapata);
        String total = help.rupiah(k);

        model.addAttribute("page_title","Penjualan");
        model.addAttribute("url", AdminUrlString.REPORTPENJUALAN);
        model.addAttribute("help",help);
        model.addAttribute("listSales",l);
        model.addAttribute("total",total);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        return help.views("report/penjualan/print");
    }
}
