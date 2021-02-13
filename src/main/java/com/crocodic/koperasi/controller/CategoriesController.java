package com.crocodic.koperasi.controller;


import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.Categories;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.models.management.Activity;
import com.crocodic.koperasi.models.management.CmsUsers;
import com.crocodic.koperasi.repository.CategoriesRepository;
import com.crocodic.koperasi.repository.management.CmsUsersRepository;
import com.crocodic.koperasi.services.CategoriesService;
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
@RequestMapping(AdminUrlString.MANAGECATEGORY)
public class CategoriesController {

    private Helpers help = new Helpers();

    @Autowired
    private CategoriesService service;

    @Autowired
    private LogsService logsService;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private CmsUsersRepository cmsUsersRepository;


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
        ct.setName("Category Name");
        ct.setColumnsName("name");
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
        Page<Categories> data = service.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        model.addAttribute("page_title","Category");
        model.addAttribute("data",data);
        model.addAttribute("paginate",paginate);
        model.addAttribute("columnsTable",ct);
        model.addAttribute("buttonAction",bt);
        model.addAttribute("url", AdminUrlString.MANAGECATEGORY);
        model.addAttribute("help",help);
        return help.views("categories/tables");
    }

    @RequestMapping(value = "add",method = RequestMethod.GET)
    private String getAdd(Model model) throws Exception{
        Categories cms = new Categories();
        model.addAttribute("page_title","Add New Category");
        model.addAttribute("form_title","Add New Category");
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGECATEGORY);
        model.addAttribute("edit",false);
        model.addAttribute("cms",cms);
        return help.views("categories/form");
    }

    @RequestMapping(value = "save",method = RequestMethod.POST)
    private String postSaveSuppliers(
            @ModelAttribute("cms") Categories cms,
            @RequestParam("submit") String submit,
            @RequestParam("return_url") String returnUrl,
            @RequestParam("current_url") String currentUrl,
            @RequestParam(value = "user_id", required = false) CmsUsers users,
            @RequestParam(value = "name", required = false) String name,
            HttpSession session,
            String ip,
            @RequestParam Optional<String> edit,
            RedirectAttributes redirAttrs,
            HttpServletRequest request
    )throws Exception, IOException {
        returnUrl = help.decodedString(returnUrl);
        currentUrl = help.decodedString(currentUrl);
//        System.out.println("currentUrl =" + currentUrl);
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
        activity.setDescription("Add New Data "+name+" at Category");
        logsService.save(activity);
        service.save(cms);
        String isedit = edit.orElse("false");
        if (isedit.equals("true")){
            activity.setIpAddress(ips);
            activity.setUserAgent(userAgent);
            activity.setUrl(host + url);
            activity.setCreated(das);
            activity.setCmsUsers(users);
            activity.setDescription("Update Data "+name+" at Category");
            logsService.save(activity);
            service.save(cms);
            redirAttrs.addFlashAttribute("success", "Category Successfully Updated");
        }else{
            redirAttrs.addFlashAttribute("success", "Category Successfully Added");
        }
        if(submit.equals("more")){
            redirAttrs.addFlashAttribute("success", "Category Successfully Added");
            return "redirect:"+currentUrl;
        } else{
            return "redirect:"+returnUrl;
        }
    }

    @RequestMapping(value = "delete/{category_id}",method = RequestMethod.GET)
    private String getDeleteCategory(
            @PathVariable(name = "category_id") Long id,
            RedirectAttributes redirAttrs,
            HttpSession session,
            String ip,
            HttpServletRequest request
    )throws Exception{
        // log
        Optional<Categories> cek = categoriesRepository.findById(id);
        String name = cek.get().getName();
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
        Object id_user = session.getAttribute("id_users");


//        System.out.println(host + url);
        InetAddress ipss = InetAddress.getByName(ip);
        String ips = ipss.getHostAddress();
        Activity activity = new Activity();
        activity.setIpAddress(ips);
        activity.setUserAgent(userAgent);
        activity.setUrl(host + url);
        activity.setCreated(das);
//        activity.setCmsUsers();
        activity.setDescription("Add New Data "+name+" at Category");
//        logsService.save(activity);
        //
        service.delete(id);
        redirAttrs.addFlashAttribute("success", "Category Successfully Deleted");
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
        return "redirect:"+AdminUrlString.MANAGECATEGORY;
    }

    @RequestMapping(value = "detail/{category_id}",method = RequestMethod.GET)
    private String getDetail(
            @PathVariable(name = "category_id") Long category_id,
            Model model
    )throws Exception{
        Categories cms = service.find(category_id);
        model.addAttribute("page_title","Detail Data Category");
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGECATEGORY);
        model.addAttribute("category_id",category_id);
        model.addAttribute("cms",cms);
        return help.views("categories/detail");
    }

    @RequestMapping(value = "edit/{category_id}",method = RequestMethod.GET)
    private String getEdit(
            @PathVariable(name = "category_id") Long category_id,
            Model model
    )throws Exception{
        Categories cms = service.find(category_id);
        model.addAttribute("page_title","Edit Data Category");
        model.addAttribute("form_title","Edit Data Category");
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGECATEGORY);
        model.addAttribute("edit",true);
        model.addAttribute("category_id",category_id);
        model.addAttribute("cms",cms);
        return help.views("categories/form");
    }

    public static Long castObjectToLong(Object object) {
        return new Long(object.toString());
    }

}
