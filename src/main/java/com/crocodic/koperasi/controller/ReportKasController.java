package com.crocodic.koperasi.controller;


import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.Kas;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.repository.KasRepository;
import com.crocodic.koperasi.string.AdminUrlString;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(AdminUrlString.REPORTKAS)
public class ReportKasController {

    Helpers help = new Helpers();

    @Autowired
    private KasRepository kasRepository;

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
        List<Kas> l = kasRepository.findAll();
        String pendapata = kasRepository.selectTotalPendapatan();
        String pengeluara  = kasRepository.selectTotalPengeluaran();
        BigInteger a;
        BigInteger b;
        BigInteger e;
        BigInteger b1;
        a = new BigInteger(pendapata);
        b = new BigInteger(pengeluara);
        e = a.subtract(b);
        String sald = String.valueOf(e);
        double k = Double.parseDouble(sald);
        double j = Double.parseDouble(pengeluara);
        double d=Double.parseDouble(pendapata);
        String pendapatan = help.rupiah(d);
        String pengeluaran = help.rupiah(j);
        String saldo = help.rupiah(k);
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String startdate = sdf1.format(currentSqlDate);
        String enddate = sdf1.format(currentSqlDate);
        model.addAttribute("page_title","Kas");
        model.addAttribute("url", AdminUrlString.REPORTKAS);
        model.addAttribute("columnsTable",ct);
        model.addAttribute("buttonAction",bt);
        model.addAttribute("help",help);
        model.addAttribute("listKas",l);
        model.addAttribute("pendapatan",pendapatan);
        model.addAttribute("pengeluaran", pengeluaran);
        model.addAttribute("saldo",saldo);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        return help.views("report/kas/tables");
    }

    @RequestMapping(value = "daterange",method = RequestMethod.GET)
    private String lists(
            @RequestParam(name = "startdate",required = true) String startdate,
            @RequestParam(name = "enddate",required = true) String enddate,
            Model model
    ) throws Exception{
        List<Kas> l = kasRepository.findAllByTanggal(startdate,enddate);
        String pendapata = kasRepository.selectTotalPendapatanByDateBeetween(startdate,enddate);
        String pengeluara  = kasRepository.selectTotalPengeluaranByDateBeetween(startdate, enddate);
        BigInteger a;
        BigInteger b;
        BigInteger e;
        BigInteger b1;
        a = new BigInteger(pendapata);
        b = new BigInteger(pengeluara);
        e = a.subtract(b);
        String sald = String.valueOf(e);
        double k = Double.parseDouble(sald);
        double j = Double.parseDouble(pengeluara);
        double d=Double.parseDouble(pendapata);
        String pendapatan = help.rupiah(d);
        String pengeluaran = help.rupiah(j);
        String saldo = help.rupiah(k);
        model.addAttribute("page_title","Kas");
        model.addAttribute("url", AdminUrlString.REPORTKAS);
        model.addAttribute("help",help);
        model.addAttribute("listKas",l);
        model.addAttribute("pendapatan",pendapatan);
        model.addAttribute("pengeluaran", pengeluaran);
        model.addAttribute("saldo",saldo);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        return help.views("report/kas/tables");
    }

    @RequestMapping(value = "print/startdate={startdate}&enddate={enddate}",method = RequestMethod.GET)
    private String listss(
            @PathVariable(name = "startdate") String startdate,
            @PathVariable(name = "enddate") String enddate,
//            @RequestParam(name = "startdate",required = true) String startdate,
//            @RequestParam(name = "enddate",required = true) String enddate,
            Model model,
            HttpServletRequest request
    ) throws Exception{
        List<Kas> l = kasRepository.findAllByTanggal(startdate,enddate);
        String pendapata = kasRepository.selectTotalPendapatanByDateBeetween(startdate,enddate);
        String pengeluara  = kasRepository.selectTotalPengeluaranByDateBeetween(startdate, enddate);
        BigInteger a;
        BigInteger b;
        BigInteger e;
        BigInteger b1;
        a = new BigInteger(pendapata);
        b = new BigInteger(pengeluara);
        e = a.subtract(b);
        String sald = String.valueOf(e);
        double k = Double.parseDouble(sald);
        double j = Double.parseDouble(pengeluara);
        double d=Double.parseDouble(pendapata);
        String pendapatan = help.rupiah(d);
        String pengeluaran = help.rupiah(j);
        String saldo = help.rupiah(k);
        model.addAttribute("page_title","Kas");
        model.addAttribute("url", AdminUrlString.REPORTKAS);
        model.addAttribute("help",help);
        model.addAttribute("listKas",l);
        model.addAttribute("pendapatan",pendapatan);
        model.addAttribute("pengeluaran", pengeluaran);
        model.addAttribute("saldo",saldo);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        return help.views("report/kas/print");
    }
}
