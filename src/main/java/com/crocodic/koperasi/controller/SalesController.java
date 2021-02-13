package com.crocodic.koperasi.controller;


import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.*;
import com.crocodic.koperasi.models.management.Activity;
import com.crocodic.koperasi.models.management.CmsUsers;
import com.crocodic.koperasi.repository.CartRepository;
import com.crocodic.koperasi.repository.KasRepository;
import com.crocodic.koperasi.repository.SalesDetailsRepository;
import com.crocodic.koperasi.repository.SalesRepository;
import com.crocodic.koperasi.services.*;
import com.crocodic.koperasi.string.AdminUrlString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(AdminUrlString.MANAGESALES)
public class SalesController {

    private Helpers help = new Helpers();

    @Autowired
    private CustomersService customersService;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private CartService cartService;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private SalesDetailsRepository salesDetailsRepository;

    @Autowired
    private SaleDetailsService saleDetailsService;

    @Autowired
    private KasRepository kasRepository;


    @Autowired
    private LogsService logsService;


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
        //date
        List<Carts> carts = cartRepository.findAll();
        if (carts != null) {
            for (Carts cms : carts) {
                String kd = cms.getTransaction_code();
                Date currentSqlDate = new Date(System.currentTimeMillis());
                SimpleDateFormat sdd = new SimpleDateFormat();
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                String date = sdf1.format(currentSqlDate);

                //created_at
                Date currentSqlDatee = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                String created_at = sdf2.format(currentSqlDatee);
                List<Customers> customers = customersService.listAll();
                List<Items> listItems = itemsService.listAll();
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
                Date datee = new Date(System.currentTimeMillis());
                long l = datee.getTime();
                String transaction_code = kd;
                model.addAttribute("page_title","Sales");
                model.addAttribute("url", AdminUrlString.MANAGESALES);
                model.addAttribute("help",help);
                model.addAttribute("date",date);
                model.addAttribute("customers",customers);
                model.addAttribute("listItems",listItems);
                model.addAttribute("transaction_code",transaction_code);
                model.addAttribute("help",help);
                model.addAttribute("created_at",created_at);
                return help.views("sales/form");
            }
        } else {
            Date currentSqlDate = new Date(System.currentTimeMillis());
            SimpleDateFormat sdd = new SimpleDateFormat();
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            String date = sdf1.format(currentSqlDate);

            //created_at
            Date currentSqlDatee = new Date(System.currentTimeMillis());
            System.out.println("currentsqldate = " + currentSqlDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            String created_at = sdf2.format(currentSqlDatee);

            List<Customers> customers = customersService.listAll();
            List<Items> listItems = itemsService.listAll();
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
            Date datee = new Date(System.currentTimeMillis());
            long l = datee.getTime();
            String transaction_code = "INV" + l;
//                String transaction_code = help.nofaktur();
//                double rp = 2000;
//                String rupiah = help.rupiah(rp);
//                System.out.append(rupiah);
            model.addAttribute("page_title","Sales");
            model.addAttribute("url", AdminUrlString.MANAGESALES);
            model.addAttribute("help",help);
            model.addAttribute("date",date);
            model.addAttribute("customers",customers);
            model.addAttribute("listItems",listItems);
            model.addAttribute("transaction_code",transaction_code);
            model.addAttribute("help",help);
            model.addAttribute("created_at",created_at);
            return help.views("sales/form");
        }

        Date currentSqlDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdd = new SimpleDateFormat();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf1.format(currentSqlDate);

        //created_at
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String created_at = sdf2.format(currentSqlDatee);

        List<Customers> customers = customersService.listAll();
        List<Items> listItems = itemsService.listAll();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        Date datee = new Date(System.currentTimeMillis());
        long l = datee.getTime();
        String transaction_code = "INV" + l;
//                String transaction_code = help.nofaktur();
//                double rp = 2000;
//                String rupiah = help.rupiah(rp);
//                System.out.append(rupiah);
        model.addAttribute("page_title","Sales");
        model.addAttribute("url", AdminUrlString.MANAGESALES);
        model.addAttribute("help",help);
        model.addAttribute("date",date);
        model.addAttribute("customers",customers);
        model.addAttribute("listItems",listItems);
        model.addAttribute("transaction_code",transaction_code);
        model.addAttribute("help",help);
        model.addAttribute("created_at",created_at);
        return help.views("sales/form");

    }

    @RequestMapping(value = "get",method = RequestMethod.GET)
    private String getData(Model model,HttpSession session) throws Exception{
        List<Carts> carts = cartService.listAll();
        model.addAttribute("carts",carts);
        return help.views("sales/getData");
    }

    @RequestMapping(value = "save",method = RequestMethod.POST)
    private String postSaveItems(
            @RequestParam(name = "sale_id",required = false) String sale_id,
//            @RequestParam(name = "sale_id",required = false) Sales sale,
            @RequestParam(name = "sub_total", required = false) String sub_total,
            @RequestParam(name = "discount", required = false) String discount,
            @RequestParam(name = "grand_total", required = false) Integer grand_total,
            @RequestParam(name = "cash", required = false) String cash,
            @RequestParam(name = "change", required = false) String change,
            @RequestParam(name = "note", required = false) String note,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "user_id", required = false) CmsUsers user_id,
            @RequestParam(name = "created_at", required = false) String created_at,
            @RequestParam(name = "customer_name", required = false) String customer,
            @RequestParam Optional<String> edit,
            RedirectAttributes redirAttrs,
            HttpServletRequest request,
            Model model,
            String ip,
            HttpSession session
    )throws Exception, IOException {
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-yyyy");
        String month = sdf2.format(currentSqlDatee);
        Year t = Year.now(ZoneId.of("Asia/Jakarta"));
        String y = String.valueOf(t);
        SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd");
        String das = sdf5.format(currentSqlDatee);
        Sales sales = new Sales();
        sales.setInvoice(sale_id);
        sales.setTotal_price(sub_total);
        sales.setDiscount(discount);
        sales.setFinal_price(grand_total);
        sales.setCash(cash);
        sales.setRemaining(change);
        sales.setNote(note);
        sales.setDate(das);
        sales.setCmsUsers(user_id);
        sales.setCreated_at(created_at);
        sales.setCustomer(customer);
        sales.setYear(y);
        sales.setMonth(month);
        salesRepository.save(sales);

        Sales sales1 = salesRepository.findByInvoice(sale_id);
        Long sales3 = sales.getSale_id();
        String sales2 = String.valueOf(sales3);
        //
        String userAgent = request.getHeader("User-Agent");
//        String method = request.getMethod();
        String ipAddress = help.getClientIpAddress(request);
        String url = request.getRequestURI();
        URL requestURL = new URL(request.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        String host = requestURL.getProtocol() + "://" + requestURL.getHost() + port;
        Date currentSqlDateee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf6 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dasn = sdf6.format(currentSqlDateee);
        Object namee = session.getAttribute("name");
//        System.out.println(host + url);
        InetAddress ipss = InetAddress.getByName(ip);
        String ips = ipss.getHostAddress();
        Activity activity = new Activity();
        activity.setIpAddress(ips);
        activity.setUserAgent(userAgent);
        activity.setUrl(host + url);
        activity.setCreated(dasn);
        activity.setCmsUsers(user_id);
        activity.setDescription("Add Sales Transaction "+sale_id+" at Sales");
        logsService.save(activity);
        //
//        Long sale_id_relation = sales1.getSale_id();
        List<Carts> carts = cartRepository.findAll();
        for (Carts carts1 : carts) {
            Long cart_id = carts1.getCart_id();
//            String transaction_cod = carts1.getTransaction_code();
//            Cart transaction_code = transaction_cod;
            Items items = carts1.getItems();
            Integer i = carts1.getPrice();
            String price = Integer.toString(i);
            Integer i1 = carts1.getQty();
            String qty = Integer.toString(i1);
            String discount_item = carts1.getDiscount_item();
            String total = carts1.getTotal();
            String customer_name = carts1.getCustomer_name();
            String created_att = carts1.getCreated_at();
            SaleDetails saleDetails = new SaleDetails();
            saleDetails.setCreated_at(created_att);
            saleDetails.setCustomer_name(customer_name);
            saleDetails.setDiscount_item(discount_item);
            saleDetails.setPrice(price);
            saleDetails.setQty(qty);
            saleDetails.setTotal(total);
            saleDetails.setItems(items);
//            saleDetails.setSales(sale_id_relation);
//            saleDetails.setSales(b);
            saleDetails.setSale(sales2);
            salesDetailsRepository.save(saleDetails);
            cartService.delete(cart_id);
//            String referer = request.getHeader("Referer");
//            return "redirect:"+ referer;
        }

        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
        String da = sdf4.format(currentSqlDatee);
        Kas kas = new Kas();
        kas.setTanggal(da);
        kas.setFaktur(sale_id);
        kas.setType("pendapatan");
        kas.setJenis("penjualan");
        kas.setPemasukan(grand_total);
        kas.setPengeluaran(0);
        kas.setKeterangan("Penjualan "+sale_id);
        kas.setCreatedAt(created_at);
        kasRepository.save(kas);
//        String referer = request.getHeader("Referer");
//        return "redirect:"+ referer;

        return "redirect:"+AdminUrlString.MANAGESALES+"/detail/"+sales2;
    }

    @RequestMapping(value = "detail/{sales2}",method = RequestMethod.GET)
    private String detDetailsOrder(
            @PathVariable(name = "sales2") Long sale_id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request,
            Model model,
            HttpSession session
    )throws Exception, IOException {
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String datee = sdf2.format(currentSqlDatee);
        Optional<Sales> sales = salesRepository.findById(sale_id);
        String s=String.valueOf(sale_id);
//        SaleDetails saleDetails = salesDetailsRepository.findBySale(s);
        List<SaleDetails> listDetails = saleDetailsService.listAll(s);
        if (sales != null) {
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
            model.addAttribute("date_now",datee);
            model.addAttribute("page_title","Detail");
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
            model.addAttribute("url", AdminUrlString.MANAGESALES);
            return help.views("sales/detail_order");
        }
        model.addAttribute("date_now",datee);
        model.addAttribute("page_title","Detail");
        System.out.println("sale_Id = "+sale_id);
        return help.views("sales/detail_order");
    }

    @RequestMapping(value = "cetak_struk/{sale_id}",method = RequestMethod.GET)
    private String getPrintOrder(
            @PathVariable(name = "sale_id") Long sale_id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request,
            Model model,
            HttpSession session
    )throws Exception, IOException {
        Date currentSqlDatee = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String datee = sdf2.format(currentSqlDatee);
        Optional<Sales> sales = salesRepository.findById(sale_id);
        String s=String.valueOf(sale_id);
//        SaleDetails saleDetails = salesDetailsRepository.findBySale(s);
        List<SaleDetails> listDetails = saleDetailsService.listAll(s);
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
            model.addAttribute("date_now",datee);
            model.addAttribute("page_title","Cetak Struk");
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
            return help.views("sales/cetak_struk");
        }
        model.addAttribute("date_now",datee);
        model.addAttribute("page_title","Cetak Struk");
        System.out.println("sale_Id = "+sale_id);
        return help.views("sales/cetak_struk");
    }
}
