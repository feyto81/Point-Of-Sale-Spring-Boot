package com.crocodic.koperasi.controller.management;


import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.helpers.ListIcon;
import com.crocodic.koperasi.models.helper.ButtonAction;
import com.crocodic.koperasi.models.helper.ColumnsTable;
import com.crocodic.koperasi.models.management.CmsMenus;
import com.crocodic.koperasi.models.management.RoleMenus;
import com.crocodic.koperasi.services.management.CmsMenusService;
import com.crocodic.koperasi.services.management.CmsPrivilegesRolesService;
import com.crocodic.koperasi.string.AdminUrlString;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
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
@RequestMapping(AdminUrlString.MANAGEMENU)
public class CmsMenusController {

    Helpers help = new Helpers();

    @Autowired
    private CmsMenusService service;

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
        ct.setName("Icon");
        ct.setColumnsName("icon");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Path");
        ct.setColumnsName("path");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Parent");
        ct.setColumnsName("parent_id");
        columns.add(ct);

        ct = new ColumnsTable();
        ct.setName("Sorting");
        ct.setColumnsName("sorting");
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
            HttpSession session,
            RedirectAttributes attr,
            Model model
    ) throws Exception {

//        Pageable pageable = this.par(sortby.orElse("id-desc"),page.orElse(1),limit.orElse(10));
//        Page<CmsMenus> data = service.findAllPaginate(q.orElse(""),pageable);
//        List<String> paginate = help.renderPaginate(page.orElse(1),data.getTotalPages());

//        List<ColumnsTable> ct = this.columnsTable();
//        List<ButtonAction> bt = this.buttonActions();
        List<RoleMenus> menuList = service.allMenusList();
        CmsMenus menus = new CmsMenus();

        model.addAttribute("page_title","Menu");
        model.addAttribute("menus", menus);
        model.addAttribute("data",menuList);
//        model.addAttribute("data",data);
//        model.addAttribute("paginate",paginate);
//        model.addAttribute("columnsTable",ct);
//        model.addAttribute("buttonAction",bt);
        model.addAttribute("url", AdminUrlString.MANAGEMENU);
        model.addAttribute("help",help);
        model.addAttribute("icon", ListIcon.icon());
        model.addAttribute("btn","Add Menu");

        return help.views("management/menus/tables");
    }

    @RequestMapping(value = "add",method = RequestMethod.GET)
    private String getAdd(Model model) throws Exception{
        CmsMenus menus = new CmsMenus();
        model.addAttribute("page_title","Tambah Menu Baru");
        model.addAttribute("menus", menus);
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGEMENU);
        model.addAttribute("edit",false);
        return help.views("management/menusNew/form");
    }

    @RequestMapping(value = "edit/{id}",method = RequestMethod.GET)
    private String getEdit(
            @PathVariable(name = "id") Long id,
            Model model
    )throws Exception{
        List<RoleMenus> menuList = service.allMenusList();
        CmsMenus menus = service.find(id);

        model.addAttribute("page_title","Menu");
        model.addAttribute("data",menuList);
        model.addAttribute("icon", ListIcon.icon());
        model.addAttribute("menus", menus);
        model.addAttribute("help",help);
        model.addAttribute("url",AdminUrlString.MANAGEMENU);
        model.addAttribute("edit",true);
        model.addAttribute("btn","Edit Menu");
        return help.views("management/menus/tables");
    }


    @RequestMapping(value = "save",method = RequestMethod.POST)
    private String postSaveCmsMenus(
            @ModelAttribute("menus") CmsMenus menus,
            @RequestParam("submit") String submit,
            @RequestParam("current_url") String currentUrl,
            @RequestParam Optional<String> edit,
            RedirectAttributes attr
    )throws Exception{
        if (menus.getParentId()==null){
            Long parrent = Long.parseLong("0");
            menus.setParentId(parrent);
        }
        service.save(menus);
        String isedit = edit.orElse("false");
        if (isedit.equals("true")){
            attr.addFlashAttribute("success", "Data menu berhasil diperbarui");
        }else{
            attr.addFlashAttribute("success", "Data menu berhasil ditambahkan");
        }
        currentUrl = help.decodedString(currentUrl);
        return "redirect:"+AdminUrlString.MANAGEMENU;
    }

    @RequestMapping(value = "delete/{id}",method = RequestMethod.GET)
    private String getDeleteCmsMenus(
            @PathVariable(name = "id") Long id,
            RedirectAttributes attr
    )throws Exception{
        CmsMenus del = service.find(id);
        cmsPrivilegesRolesService.deleteCmsPrivilegesRolesByCmsMenus(del);
        service.delete(id);
        attr.addFlashAttribute("success", "Data menu berhasil dihapus");
        return "redirect:"+AdminUrlString.MANAGEMENU;
    }

    @PostMapping("action-selected")
    private String postActionSelected(
            @RequestParam(name = "idSelected",required = false) Long [] idSelected,
            RedirectAttributes attr,
            HttpServletRequest request
    )throws Exception{
        if (idSelected != null) {
            for (Long id : idSelected) {
                CmsMenus del = service.find(id);
                cmsPrivilegesRolesService.deleteCmsPrivilegesRolesByCmsMenus(del);
                service.delete(id);
            }
            help.message(attr,"Berhasil menghapus data terpilih","success");
        } else {
            help.message(attr,"Mohon pilih salah satu data terlebih dahulu","warning");
        }
        return "redirect:"+AdminUrlString.MANAGEMENU;
    }

    @ResponseBody
    @PostMapping(value=AdminUrlString.saveMenuManagement,produces = MediaType.APPLICATION_JSON_VALUE)
    private String postSaveMenuManagement(
            @RequestParam("menus") JSONArray menus
    )throws Exception{
        JSONArray listMenu = (JSONArray) menus.get(0);
        for (int i = 0 ; i < listMenu.length(); i++) {
            int sort = i+1;
            JSONObject m = listMenu.getJSONObject(i);
            Long idParent = m.getLong("id");
            JSONArray listChild = (JSONArray) m.getJSONArray("children").get(0);
            for (int x = 0 ; x < listChild.length(); x++) {
                int sortChild = x+1;
                JSONObject c = listChild.getJSONObject(x);
                Long idChild = c.getLong("id");
                CmsMenus child = service.find(idChild);
                if (child!=null){
                    child.setSorting(sortChild);
                    child.setParentId(idParent);
                    service.save(child);
                }
            }
            CmsMenus parrent = service.find(idParent);
            if (parrent!=null){
                Long pNul = Long.parseLong("0");
                parrent.setParentId(pNul);
                parrent.setSorting(sort);
                service.save(parrent);
            }
        }
        JSONObject res = new JSONObject();
        res.put("status","OKE");
        return res.toString();
    }

}
