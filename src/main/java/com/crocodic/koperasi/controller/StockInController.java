package com.crocodic.koperasi.controller;

import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.Items;
import com.crocodic.koperasi.models.StockIn;
import com.crocodic.koperasi.models.Suppliers;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.models.management.Activity;
import com.crocodic.koperasi.models.management.CmsUsers;
import com.crocodic.koperasi.repository.ItemsRepository;
import com.crocodic.koperasi.repository.StockInRepository;
import com.crocodic.koperasi.services.ItemsService;
import com.crocodic.koperasi.services.LogsService;
import com.crocodic.koperasi.services.StockInService;
import com.crocodic.koperasi.services.SuppliersService;
import com.crocodic.koperasi.services.management.CmsUsersService;
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
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(AdminUrlString.MANAGESTOCKIN)
public class StockInController {

    private Helpers help = new Helpers();

    @Autowired
    private StockInService service;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private SuppliersService suppliersService;

    @Autowired
    private CmsUsersService cmsUsersService;

    @Autowired
    private StockInRepository stockInRepository;

    @Autowired
    private ItemsRepository itemsRepository;

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
        ct.setName("Barcode");
        ct.setColumnsName("items.barcode");
        columns.add(ct);


        ct = new ColumnsTable();
        ct.setName("Product Item");
        ct.setColumnsName("items.name");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Qty");
        ct.setColumnsName("qty");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Info");
        ct.setColumnsName("detail");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Date");
        ct.setColumnsName("created_at");
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
        Page<StockIn> data = service.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        model.addAttribute("page_title","Stock In");
        model.addAttribute("data",data);
        model.addAttribute("paginate",paginate);
        model.addAttribute("columnsTable",ct);
        model.addAttribute("buttonAction",bt);
        model.addAttribute("url", AdminUrlString.MANAGESTOCKIN);
        model.addAttribute("help",help);
        return help.views("stockin/tables");
    }

    @RequestMapping(value = "add",method = RequestMethod.GET)
    private String getAdd(Model model,HttpSession session) throws Exception{
        StockIn cms = new StockIn();
        List<Items> items = itemsService.listAll();
        List<Suppliers> suppliers = suppliersService.listAll();
        List<CmsUsers> cmsUsers = cmsUsersService.listAl();
        List<Items> listItems = itemsService.listAll();
        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdd = new SimpleDateFormat();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf1.format(currentSqlDate);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String created_at = sdf2.format(currentSqlDate);
        Object id_user = session.getAttribute("id_users");
        model.addAttribute("page_title","Tambah Stock In");
        model.addAttribute("form_title","Add New Stock In");
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGESTOCKIN);
        model.addAttribute("edit",false);
        model.addAttribute("items",items);
        model.addAttribute("suppliers",suppliers);
        model.addAttribute("cmsUsers",cmsUsers);
        model.addAttribute("cms",cms);
        model.addAttribute("date",date);
        model.addAttribute("id_user",id_user);
        model.addAttribute("created_at",created_at);
        model.addAttribute("listItems",listItems);
        model.addAttribute("barcodee","A001");
        return help.views("stockin/form");
    }

    @RequestMapping(value = "save",method = RequestMethod.POST)
    private String postSaveStockIn(
            @ModelAttribute("cms") StockIn cms,
            @RequestParam("submit") String submit,
            @RequestParam("return_url") String returnUrl,
            @RequestParam("current_url") String currentUrl,
            @RequestParam Optional<String> edit,
            RedirectAttributes redirAttrs,
            @RequestParam("items") Long item_id,
            @RequestParam("qty") String qty,
            @RequestParam(value = "user_id", required = false) CmsUsers users,
            @RequestParam(value = "barcode", required = false) String barcode,
            String ip,
            HttpSession session,
            HttpServletRequest request
    )throws Exception, IOException {
        returnUrl = help.decodedString(returnUrl);
        currentUrl = help.decodedString(currentUrl);
        Items items1 = itemsService.getIds(item_id);
        Optional<Items> items2 = itemsRepository.findById(item_id);
        if (items1 != null) {
            if (items2 != null) {
                Long iddd = items1.getItem_id();
                Items item3 = itemsService.get(iddd);
                String stock_sementara = items1.getStock();
                BigInteger a;
                BigInteger b;
                a = new BigInteger(stock_sementara);
                b = new BigInteger(qty);
                String e = String.valueOf(b.add(a));
                item3.setStock(e);
                itemsRepository.save(item3);
                service.save(cms);
            } else {
                redirAttrs.addFlashAttribute("error", "Stock In Failed Added");
                return "redirect:"+currentUrl;
            }
        } else {
            redirAttrs.addFlashAttribute("error", "Stock In Failed Added");
            return "redirect:"+currentUrl;
        }
        String userAgent = request.getHeader("User-Agent");
//        String method = request.getMethod();
        String ipAddress = help.getClientIpAddress(request);
        String url = request.getRequestURI();
        URL requestURL = new URL(request.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        String host = requestURL.getProtocol() + "://" + requestURL.getHost() + port;
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String das = sdf5.format(currentSqlDatee);
        Object namee = session.getAttribute("name");
//        System.out.println(host + url);
        InetAddress ipss = InetAddress.getByName(ip);
        String ips = ipss.getHostAddress();
        Activity activity = new Activity();
        activity.setIpAddress(ips);
        activity.setUserAgent(userAgent);
        activity.setUrl(host + url);
        activity.setCreated(das);
        activity.setCmsUsers(users);
        activity.setDescription("Tambah Stock Barcode "+barcode+" at Stock In");
        logsService.save(activity);
        String isedit = edit.orElse("false");
        if (isedit.equals("true")){

            redirAttrs.addFlashAttribute("success", "Stock In Successfully Updated");
        }else{
            redirAttrs.addFlashAttribute("success", "Stock In Successfully Added");
        }
        if(submit.equals("more")){
            redirAttrs.addFlashAttribute("success", "Stock In Successfully Added");
            return "redirect:"+currentUrl;
        } else{
            return "redirect:"+returnUrl;
        }
    }

    @RequestMapping(value = "delete/{stock_id}",method = RequestMethod.GET)
    private String getDeleteStock(
            @PathVariable(name = "stock_id") Long id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request
    )throws Exception{
        service.delete(id);
        redirAttrs.addFlashAttribute("success", "Stock In Successfully Deleted");
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
                service.delete(id);
            }
            attr.addFlashAttribute("success", "Berhasil menghapus data terpilih");
            help.message(attr,"Berhasil menghapus data terpilih","success");
        } else {
            attr.addFlashAttribute("error", "Mohon pilih salah satu data terlebih dahulu");
        }
        return "redirect:"+AdminUrlString.MANAGESTOCKIN;
    }
}
