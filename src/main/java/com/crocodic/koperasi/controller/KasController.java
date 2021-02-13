package com.crocodic.koperasi.controller;


import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.Kas;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.models.management.Activity;
import com.crocodic.koperasi.models.management.CmsUsers;
import com.crocodic.koperasi.repository.KasRepository;
import com.crocodic.koperasi.services.KasService;
import com.crocodic.koperasi.services.LogsService;
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
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(AdminUrlString.KAS)
public class KasController {

    Helpers help = new Helpers();

    @Autowired
    private KasService kasService;

    @Autowired
    private KasRepository kasRepository;

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
        ct.setColumnsName("tanggal");
        columns.add(ct);


        ct = new ColumnsTable();
        ct.setName("Faktur");
        ct.setColumnsName("faktur");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Jenis");
        ct.setColumnsName("type");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Pendapatan");
        ct.setColumnsName("pemasukan");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Pengeluaran");
        ct.setColumnsName("pengeluaran");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Keterangan");
        ct.setColumnsName("keterangan");
        columns.add(ct);

        return  columns;
    }

    public List<ButtonAction> buttonActions(){
        List<ButtonAction> button = new ArrayList<>();
        ButtonAction bt;

        bt = new ButtonAction();
        bt.setText("Tambah Data");
        bt.setIcon("fas fa-plus-circle");
        bt.setKelas("btn btn-info");
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
        Page<Kas> data = kasService.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        model.addAttribute("page_title","Kas");
        model.addAttribute("data",data);
        model.addAttribute("paginate",paginate);
        model.addAttribute("columnsTable",ct);
        model.addAttribute("buttonAction",bt);
        model.addAttribute("url", AdminUrlString.KAS);
        model.addAttribute("help",help);
        return help.views("kas/tables");
    }

    @RequestMapping(value = "delete/{id}",method = RequestMethod.GET)
    private String getDeleteKas(
            @PathVariable(name = "id") Long id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request
    )throws Exception{
        kasService.delete(id);
        redirAttrs.addFlashAttribute("success", "Kas Successfully Deleted");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("action-selected")
    private String postActionSelected(
            @RequestParam(name = "idSelected",required = false) Long [] idSelected,
            RedirectAttributes attr,
            HttpServletRequest request
    )throws Exception{
        if (idSelected != null) {
            for (Long id : idSelected) {
                kasService.delete(id);
            }
            attr.addFlashAttribute("success", "Berhasil menghapus data terpilih");
            help.message(attr,"Berhasil menghapus data terpilih","success");
        } else {
            attr.addFlashAttribute("error", "Mohon pilih salah satu data terlebih dahulu");
        }
        return "redirect:"+AdminUrlString.KAS;
    }
    @RequestMapping(value = "add",method = RequestMethod.GET)
    private String getAdd(Model model) throws Exception{
        Kas cms = new Kas();
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf1.format(currentSqlDate);
        Date datee = new Date(System.currentTimeMillis());
        long l = datee.getTime();
        String faktur = "KAS" + l;
        model.addAttribute("page_title","Add New Kas");
        model.addAttribute("form_title","Add New Kas");
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.KAS);
        model.addAttribute("edit",false);
        model.addAttribute("cms",cms);
        model.addAttribute("date",date);
        model.addAttribute("faktur",faktur);
        return help.views("kas/form");
    }

    @RequestMapping(value = "save",method = RequestMethod.POST)
    private String postSaveKas(
            @RequestParam(name = "tanggal",required = false) String tanggal,
            @RequestParam(name = "faktur", required = false) String faktur,
            @RequestParam(name = "type",required = false) String type,
            @RequestParam(name = "pemasukan", required = false) Integer pemasukan,
            @RequestParam(name = "jenis_pemasukan", required = false) String jenis_pemasukan,
            @RequestParam(name = "pengeluaran", required = false) Integer pengeluaran,
            @RequestParam(name = "jenis_pengeluaran", required = false) String jenis_pengeluaran,
//            @RequestParam(name = "keterangan", required = false) Boolean keterangan,
            @RequestParam(name = "keterangan", required = false) String keterangan1,
            @RequestParam Optional<String> edit,
            @RequestParam("submit") String submit,
            @RequestParam("return_url") String returnUrl,
            @RequestParam("current_url") String currentUrl,
            @RequestParam(value = "user_id", required = false) CmsUsers users,
            @RequestParam(value = "type", required = false) String typee,
            String ip,
            RedirectAttributes redirAttrs,
            HttpServletRequest request,
            Model model,
            HttpSession session
    )throws Exception, IOException {
        returnUrl = help.decodedString(returnUrl);
        currentUrl = help.decodedString(currentUrl);
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String created_at = sdf2.format(currentSqlDatee);
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
        String da = sdf4.format(currentSqlDatee);
        Kas kas = new Kas();
        kas.setTanggal(da);
        kas.setFaktur(faktur);
        kas.setType(type);
        kas.setCreatedAt(created_at);
        if (type.equals("pendapatan")) {
            kas.setPemasukan(pemasukan);
            kas.setPengeluaran(0);
            kas.setJenis(jenis_pemasukan);
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
            activity.setCmsUsers(users);
            activity.setDescription("Add "+typee+" at Kas");
            logsService.save(activity);
        } else {
            kas.setPengeluaran(pengeluaran);
            kas.setPemasukan(0);
            kas.setJenis(jenis_pengeluaran);
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
            activity.setCmsUsers(users);
            activity.setDescription("Add "+"pengeluaran"+" at Kas");
            logsService.save(activity);
        }
        kas.setKeterangan(keterangan1);
        kasService.save(kas);
        String isedit = edit.orElse("false");
        if (isedit.equals("true")){
            redirAttrs.addFlashAttribute("success", "Kas Successfully Updated");
        }else{
            redirAttrs.addFlashAttribute("success", "Kas Successfully Added");
        }
        if(submit.equals("more")){
            redirAttrs.addFlashAttribute("success", "Kas Successfully Added");
            return "redirect:"+currentUrl;
        } else{
            return "redirect:"+returnUrl;
        }

    }
}
