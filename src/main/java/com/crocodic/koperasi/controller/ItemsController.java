package com.crocodic.koperasi.controller;

import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.helpers.ZXingHelper;
import com.crocodic.koperasi.models.Categories;
import com.crocodic.koperasi.models.Items;
import com.crocodic.koperasi.models.Units;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.models.management.Activity;
import com.crocodic.koperasi.models.management.CmsUsers;
import com.crocodic.koperasi.repository.ItemsRepository;
import com.crocodic.koperasi.services.CategoriesService;
import com.crocodic.koperasi.services.ItemsService;
import com.crocodic.koperasi.services.LogsService;
import com.crocodic.koperasi.services.UnitsService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(AdminUrlString.MANAGEITEM)
public class ItemsController {

    private Helpers help = new Helpers();

    @Autowired
    private ItemsService service;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private UnitsService unitsService;

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
        ct.setColumnsName("barcode");
        columns.add(ct);


        ct = new ColumnsTable();
        ct.setName("Name");
        ct.setColumnsName("name");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Category");
        ct.setColumnsName("categories.name");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Unit");
        ct.setColumnsName("units.name");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Price");
        ct.setColumnsName("price");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Stock");
        ct.setColumnsName("stock");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Image");
        ct.setColumnsName("image");
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
        Page<Items> data = service.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());
        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();
        model.addAttribute("page_title","Items");
        model.addAttribute("data",data);
        model.addAttribute("paginate",paginate);
        model.addAttribute("columnsTable",ct);
        model.addAttribute("buttonAction",bt);
        model.addAttribute("url", AdminUrlString.MANAGEITEM);
        model.addAttribute("help",help);
        return help.views("items/tables");
    }

//    public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
//        Barcode barcode = BarcodeFactory.createEAN13(barcodeText);
//        barcode.setFont(BARCODE_TEXT_FONT);
//
//        return BarcodeImageHandler.getImage(barcode);
//    }

    @RequestMapping(value = "add",method = RequestMethod.GET)
    private String getAdd(Model model) throws Exception{
        Items cms = new Items();
        List<Categories> categories = categoriesService.listAll();
        List<Units> units = unitsService.listAll();
        model.addAttribute("page_title","Tambah Item Baru");
        model.addAttribute("form_title","Add New Item");
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGEITEM);
        model.addAttribute("edit",false);
        model.addAttribute("categories",categories);
        model.addAttribute("units",units);
        model.addAttribute("cms",cms);
        return help.views("items/form");
    }

    @RequestMapping(value = "save",method = RequestMethod.POST)
    private String postSaveItems(
            @ModelAttribute("cms") Items cms,
            @RequestParam("submit") String submit,
            @RequestParam("return_url") String returnUrl,
            @RequestParam("current_url") String currentUrl,
            @RequestParam(name = "gambar",required = false) MultipartFile foto,
            @RequestParam(name = "barcode",required = false) String barcode,
            @RequestParam Optional<String> edit,
            @RequestParam(value = "user_id", required = false) CmsUsers users,
            @RequestParam(value = "name", required = false) String name,
            HttpSession session,
            String ip,
            RedirectAttributes redirAttrs,
            HttpServletRequest request
    )throws Exception, IOException {

        returnUrl = help.decodedString(returnUrl);
        currentUrl = help.decodedString(currentUrl);
//        System.out.println("barcode " + barcode);
//        Items items = itemsRepository.findByBarcode(barcode);
        Long id = cms.getItem_id();
        if(id==null){
            Items already = service.findFirstByBarcode(barcode);
            if(already!=null){
                redirAttrs.addFlashAttribute("error", "Terdapat Barcode yang sama");
//                String referer = request.getHeader("Referer");
//                return "redirect:" + referer;
                return "redirect:"+currentUrl;
            }
        }

        if (!foto.isEmpty()){
            String fv = help.uploadFile(request,foto,"");
//            System.out.println(fv);
            cms.setImage(fv);
//            System.out.println("foto" + foto);
        }else{
            if (id!=null){

                Items already = service.findFirstByBarcode(barcode);
//                if(already!=null){
//                    redirAttrs.addFlashAttribute("error", "Terdapat Barcode yang sama");
//                    String referer = request.getHeader("Referer");
//                    return "redirect:" + referer;
//                }
                Items a = service.find(id);
                String pt = a.getImage();
                cms.setImage(pt);
            }else{
                redirAttrs.addFlashAttribute("error", "Foto harap di isi");
                return "redirect:"+currentUrl;
            }
        }

        //
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
        activity.setDescription("Add New Data "+name+" at Items");
        logsService.save(activity);


        //
        service.save(cms);
        String isedit = edit.orElse("false");
        if (isedit.equals("true")){
            activity.setIpAddress(ips);
            activity.setUserAgent(userAgent);
            activity.setUrl(host + url);
            activity.setCreated(das);
            activity.setCmsUsers(users);
            activity.setDescription("Update Data "+name+" at Items");
            logsService.save(activity);
            redirAttrs.addFlashAttribute("success", "Item Successfully Updated");
        }else{
            redirAttrs.addFlashAttribute("success", "Item Successfully Added");
        }
        if(submit.equals("more")){
            return "redirect:"+currentUrl;
        }else{
            return "redirect:"+returnUrl;
        }
    }

    @RequestMapping(value = "delete/{item_id}",method = RequestMethod.GET)
    private String getDeleteItems(
            @PathVariable(name = "item_id") Long id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request
    )throws Exception{
        service.delete(id);
        redirAttrs.addFlashAttribute("success", "Item Successfully Deleted");
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
        return "redirect:"+AdminUrlString.MANAGEITEM;
    }

    @RequestMapping(value = "edit/{item_id}",method = RequestMethod.GET)
    private String getEdit(
            @PathVariable(name = "item_id") Long item_id,
            Model model
    )throws Exception{
        Items cms = service.find(item_id);
        List<Categories> categories = categoriesService.listAll();
        List<Units> units = unitsService.listAll();
        model.addAttribute("page_title","Edit Data Item");
        model.addAttribute("form_title","Edit Data Item");
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGEITEM);
        model.addAttribute("edit",true);
        model.addAttribute("item_id",item_id);
        model.addAttribute("categories",categories);
        model.addAttribute("units",units);
        model.addAttribute("cms",cms);
        return help.views("items/form");
    }

    @RequestMapping(value = "detail/{item_id}",method = RequestMethod.GET)
    private String getDetail(
            @PathVariable(name = "item_id") Long item_id,
            Model model
    )throws Exception{
        Items cms = service.find(item_id);
        model.addAttribute("page_title","Detail Data Items");
        model.addAttribute("form_title","Detail Data Items");
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGEITEM);
        model.addAttribute("item_id",item_id);
        model.addAttribute("cms",cms);
        return help.views("items/detail");
    }
    @RequestMapping(value = "qrcode/{barcode}", method = RequestMethod.GET)
    public void qrcode(
            @PathVariable("barcode") String barcode,
            HttpServletResponse response
    ) throws Exception {
        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(ZXingHelper.getQRCodeImage(barcode, 200, 200));
        outputStream.flush();
        outputStream.close();
    }
    @RequestMapping(value = "barcode/{barcode}", method = RequestMethod.GET)
    public void barcode(
            @PathVariable("barcode") String barcode,
            HttpServletResponse response
    ) throws Exception {
        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(ZXingHelper.getBarcodeImage(barcode, 150, 100));
        outputStream.flush();
        outputStream.close();
    }

    @RequestMapping(value = "barcode-qrcode/{barcode}")
    public String getBarcodeAndQRCode(@PathVariable("barcode") String barcode,
                                      HttpServletResponse response,
                                      Model model
    ) throws Exception {
        model.addAttribute("page_title","Detail Data Items");
        model.addAttribute("barcode",barcode);
        model.addAttribute("url",AdminUrlString.MANAGEITEM);
        return help.views("items/all");
    }
}
