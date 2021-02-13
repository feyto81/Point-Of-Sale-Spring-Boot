package com.crocodic.koperasi.controller.management;

import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.models.management.CmsMenus;
import com.crocodic.koperasi.models.management.CmsPrivileges;
import com.crocodic.koperasi.models.management.CmsPrivilegesRoles;
import com.crocodic.koperasi.models.management.MenusRoles;
import com.crocodic.koperasi.repository.management.CmsPrivilegesRolesRepository;
import com.crocodic.koperasi.services.management.CmsMenusService;
import com.crocodic.koperasi.services.management.CmsPrivilegesRolesService;
import com.crocodic.koperasi.services.management.CmsPrivilegesServices;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(AdminUrlString.MANAGEROLES)
public class CmsPrivilegesController {

    Helpers help = new Helpers();

    @Autowired
    private CmsPrivilegesServices service;

    @Autowired
    private CmsMenusService cmsMenusService;

    @Autowired
    private CmsUsersService cmsUsersService;

    @Autowired
    private CmsPrivilegesRolesRepository cmsPrivilegesRolesRepository;

    @Autowired
    private CmsPrivilegesRolesService cmsPrivilegesRolesService;


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
        ct.setName("ID");
        ct.setColumnsName("id");
        columns.add(ct);


        ct = new ColumnsTable();
        ct.setName("Nama Menu");
        ct.setColumnsName("name");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Is Superadmin");
        ct.setColumnsName("is_superadmin");
        columns.add(ct);



        return  columns;
    }

    public List<ButtonAction> buttonActions(){
        List<ButtonAction> button = new ArrayList<>();
        ButtonAction bt;

        bt = new ButtonAction();
        bt.setText("Tambah Data");
        bt.setIcon("fa fa-plus-circle");
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
        Page<CmsPrivileges> data = service.findAllPaginate(q.orElse(""),pageable);
        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());

        List<ColumnsTable> ct = this.columnsTable();
        List<ButtonAction> bt = this.buttonActions();

        model.addAttribute("page_title","Privileges");
        model.addAttribute("data",data);
        model.addAttribute("paginate",paginate);
        model.addAttribute("columnsTable",ct);
        model.addAttribute("buttonAction",bt);
        model.addAttribute("url", AdminUrlString.MANAGEROLES);
        model.addAttribute("help",help);

        return help.views("management/privileges/tables");
    }

    @RequestMapping(value = "add",method = RequestMethod.GET)
    private String getAdd(Model model) throws Exception{
        CmsPrivileges cms = new CmsPrivileges();
        List<MenusRoles> menu = listMenusRoles(cms);
        model.addAttribute("page_title","Tambah Privileges Baru");
        model.addAttribute("cms", cms);
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGEROLES);
        model.addAttribute("edit",false);
        model.addAttribute("menu",menu);
        return help.views("management/privileges/form");
    }

    @RequestMapping(value = "edit/{id}",method = RequestMethod.GET)
    private String getEdit(
            @PathVariable(name = "id") Long id,
            Model model
    )throws Exception{
        CmsPrivileges cms = service.find(id);
//        String a = help.privilegesRoles(Long.parseLong("2"),cms,"view");
//        System.out.println("value = "+a);
        List<MenusRoles> menu = listMenusRoles(cms);
        model.addAttribute("page_title","Edit Data Menu");
        model.addAttribute("cms", cms);
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGEROLES);
        model.addAttribute("edit",true);
        model.addAttribute("menu",menu);
        return help.views("management/privileges/form");
    }

    public List<MenusRoles> listMenusRoles(CmsPrivileges cms){
        List<CmsMenus> menus = cmsMenusService.listAll();
        List<MenusRoles> list = new ArrayList<>();
        for (CmsMenus m : menus){
            int canView = 0;
            int canAdd = 0;
            int canEdit = 0;
            int canDelete = 0;

            if (cms.getId()!=null){
                CmsPrivilegesRoles find = cmsPrivilegesRolesRepository.findByCmsPrivilegesAndCmsMenus(cms,m);
                if (find!=null){
                    if (find.getCan_view()==1){
                        canView = 1;
                    }
                    if (find.getCan_add()==1){
                        canAdd = 1;
                    }
                    if (find.getCan_edit()==1){
                        canEdit = 1;
                    }
                    if (find.getCan_delete()==1){
                        canDelete = 1;
                    }
                }
            }
            MenusRoles mm = new MenusRoles();
            mm.setId(m.getId());
            mm.setName(m.getName());
            mm.setCan_add(canAdd);
            mm.setCan_edit(canEdit);
            mm.setCan_view(canView);
            mm.setCan_delete(canDelete);
            list.add(mm);
        }
        return list;
    }



    @RequestMapping(value = "save",method = RequestMethod.POST)
    private String postSaveCmsPrivileges(
            @ModelAttribute("cms") CmsPrivileges cms,
            @RequestParam("submit") String submit,
            @RequestParam("return_url") String returnUrl,
            @RequestParam("current_url") String currentUrl,
            @RequestParam Optional<String> edit,
            @RequestParam(name = "privileges",required = false) String[] privileges,
            RedirectAttributes attr,
            HttpServletRequest request
    )throws Exception{

        service.save(cms);
        cmsPrivilegesRolesService.deleteCmsPrivilegesRolesByCmsPrivileges(cms);
        for (String value : privileges) {
            String[] array = value.split("-", -1);
            String val = array[0];
            Long id = Long.parseLong(array[1]);
            CmsMenus menu = cmsMenusService.find(id);
            CmsPrivilegesRoles roles;
            CmsPrivilegesRoles find = cmsPrivilegesRolesRepository.findByCmsPrivilegesAndCmsMenus(cms,menu);
            if(find==null){
                roles = new CmsPrivilegesRoles();
            }else{
                roles = find;
            }
            if (val.equals("is_visible")){
                roles.setCan_view(1);
            }else{
                roles.setCan_view((roles!=null)?roles.getCan_view():0);
            }
            if (val.equals("is_create")){
                roles.setCan_add(1);
            }else{
                roles.setCan_add((roles!=null)?roles.getCan_add():0);
            }
            if (val.equals("is_edit")){
                roles.setCan_edit(1);
            }else{
                roles.setCan_edit((roles!=null)?roles.getCan_edit():0);
            }
            if (val.equals("is_delete")){
                roles.setCan_delete(1);
            }else{
                roles.setCan_delete((roles!=null)?roles.getCan_delete():0);
            }
            roles.setCmsPrivileges(cms);
            roles.setCmsMenus(menu);
            cmsPrivilegesRolesRepository.save(roles);

        }
        String isedit = edit.orElse("false");
        if (isedit.equals("true")){
            help.message(attr,"Data privileges berhasil diperbarui","success");
        }else{
            help.message(attr,"Data privileges berhasil ditambahkan","success");
        }
        returnUrl = help.decodedString(returnUrl);
        currentUrl = help.decodedString(currentUrl);
        if(submit.equals("more")){
            return "redirect:"+currentUrl;
        }else{
            return "redirect:"+returnUrl;
        }
    }

    @RequestMapping(value = "delete/{id}",method = RequestMethod.GET)
    private String getDeleteCmsPrivileges(
            @PathVariable(name = "id") Long id,
            RedirectAttributes attr
    )throws Exception{
        CmsPrivileges cms = service.find(id);
        cmsUsersService.deleteCmsUsersByCmsPrivileges(cms);
        service.delete(id);
        help.message(attr,"Data privileges berhasil dihapus","success");
        return "redirect:"+AdminUrlString.MANAGEROLES;
    }

    @PostMapping("action-selected")
    private String postActionSelected(
            @RequestParam(name = "idSelected",required = false) Long [] idSelected,
            RedirectAttributes attr,
            HttpServletRequest request
    )throws Exception{
        if (idSelected != null) {
            for (Long id : idSelected) {
                CmsPrivileges cms = service.find(id);
                cmsUsersService.deleteCmsUsersByCmsPrivileges(cms);
                service.delete(id);
            }
            help.message(attr,"Berhasil menghapus data terpilih","success");
        } else {
            help.message(attr,"Mohon pilih salah satu data terlebih dahulu","warning");
        }
        return "redirect:"+AdminUrlString.MANAGEROLES;
    }
}
