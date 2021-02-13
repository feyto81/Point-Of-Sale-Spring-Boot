package com.crocodic.koperasi.controller.management;

import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.models.management.CmsPrivileges;
import com.crocodic.koperasi.models.management.CmsUsers;
import com.crocodic.koperasi.services.management.CmsPrivilegesServices;
import com.crocodic.koperasi.services.management.CmsUsersService;
import com.crocodic.koperasi.string.AdminUrlString;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(AdminUrlString.MANAGEUSERS)
public class CmsUsersController {

    Helpers help = new Helpers();

    @Autowired
    private CmsUsersService service;

    @Autowired
    private CmsPrivilegesServices roleService;


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
            HttpSession session,
            RedirectAttributes attr,
            Model model
    ) throws Exception {

        Pageable pageable = this.par(sortby.orElse("id-desc"),page.orElse(1),limit.orElse(10));
        Page<CmsUsers> data = service.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());

        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();

        model.addAttribute("page_title","Users");
        model.addAttribute("data",data);
        model.addAttribute("paginate",paginate);
        model.addAttribute("columnsTable",ct);
        model.addAttribute("buttonAction",bt);
        model.addAttribute("url", AdminUrlString.MANAGEUSERS);
        model.addAttribute("help",help);

        return help.views("management/users/tables");
    }

    @RequestMapping(value = "add",method = RequestMethod.GET)
    private String getAdd(Model model) throws Exception{
        CmsUsers cms = new CmsUsers();
        List<CmsPrivileges> role = roleService.listAll();
        model.addAttribute("page_title","Tambah Users Baru");
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGEUSERS);
        model.addAttribute("edit",false);
        model.addAttribute("role",role);
        model.addAttribute("cms",cms);
        return help.views("management/users/form");
    }

    @RequestMapping(value = "edit/{id}",method = RequestMethod.GET)
    private String getEdit(
            @PathVariable(name = "id") Long id,
            Model model
    )throws Exception{
        CmsUsers cms = service.find(id);
        List<CmsPrivileges> role = roleService.listAll();
        model.addAttribute("page_title","Edit Data Users");
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGEUSERS);
        model.addAttribute("edit",true);
        model.addAttribute("id",id);
        model.addAttribute("role",role);
        model.addAttribute("cms",cms);
        return help.views("management/users/form");
    }


    @RequestMapping(value = "save",method = RequestMethod.POST)
    private String postSaveCmsUsers(
            @ModelAttribute("cms") CmsUsers cms,
            @RequestParam("submit") String submit,
            @RequestParam("email") String email,
            @RequestParam("return_url") String returnUrl,
            @RequestParam("current_url") String currentUrl,
            @RequestParam(name = "gambar",required = false) MultipartFile foto,
            @RequestParam(name = "pw",required = false) String password,
            @RequestParam Optional<String> edit,
            RedirectAttributes attr,
            HttpServletRequest request
    )throws Exception, IOException {

        returnUrl = help.decodedString(returnUrl);
        currentUrl = help.decodedString(currentUrl);
        Long id = cms.getId();
        if(id==null){
            CmsUsers already = service.findFirstByEmail(email);
            if(already!=null){
//                help.message(attr,"Terdapat email yang sama","warning");
                attr.addFlashAttribute("error", "Terdapat email yang sama");
                return "redirect:"+currentUrl;
            }
        }
        if (!password.equals("")){
            String pw = new BCryptPasswordEncoder().encode(password);
            cms.setPassword(pw);
        }else{
            CmsUsers a = service.find(id);
            String pwo = a.getPassword();
            cms.setPassword(pwo);
        }
        if (!foto.isEmpty()){
            String fv = help.uploadFile(request,foto,"");
            System.out.println(fv);
            cms.setFoto(fv);
        }else{
            if (id!=null){
                CmsUsers a = service.find(id);
                String pt = a.getFoto();
                cms.setFoto(pt);
            }else{
//                help.message(attr,"Foto harap disi","warning");
                attr.addFlashAttribute("error", "Foto harap di isi");
                return "redirect:"+currentUrl;
            }
        }
        service.save(cms);
        String isedit = edit.orElse("false");
        if (isedit.equals("true")){
            attr.addFlashAttribute("success", "Users Successfully Updated");
        }else{

            attr.addFlashAttribute("success", "Users Successfully Added");
        }
        if(submit.equals("more")){
            return "redirect:"+currentUrl;
        }else{
            return "redirect:"+returnUrl;
        }
    }

    @RequestMapping(value = "delete/{id}",method = RequestMethod.GET)
    private String getDeleteCmsUsers(
            @PathVariable(name = "id") Long id,
            RedirectAttributes attr
    )throws Exception{
        service.delete(id);
        attr.addFlashAttribute("success", "Users Successfully Deleted");
        return "redirect:"+AdminUrlString.MANAGEUSERS;
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
        } else {
            attr.addFlashAttribute("error", "Mohon pilih salah satu data terlebih dahulu");
        }
        return "redirect:"+AdminUrlString.MANAGEUSERS;
    }

}
