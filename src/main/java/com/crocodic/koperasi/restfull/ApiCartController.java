package com.crocodic.koperasi.restfull;


import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.Carts;
import com.crocodic.koperasi.models.Items;
import com.crocodic.koperasi.repository.CartRepository;
import com.crocodic.koperasi.repository.ItemsRepository;
import com.crocodic.koperasi.services.CartService;
import com.crocodic.koperasi.services.ItemsService;
import com.crocodic.koperasi.string.ApiUrlString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(ApiUrlString.CART)
public class ApiCartController {

    private Helpers help = new Helpers();

    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private ItemsService itemsService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ItemsRepository itemsRepository;

    @RequestMapping(value = "add",method = RequestMethod.POST)
    private String getAdd(Model model, HttpSession session,
                          @RequestParam(value = "customer") String customer,
                          @RequestParam(value = "price") Integer price,
                          @RequestParam(value = "qty") Integer qty,
                          @RequestParam(value = "transaction_code") String transaction_code,
                          @RequestParam(value = "id_users") String id_users,
                          @RequestParam(value = "item_id") Items item_idd,
                          @RequestParam(value = "item_id") String item_id1,
                          @RequestParam(value = "item_id") Long item_id2,
                          HttpServletRequest request) throws Exception{

        Carts cart = cartRepository.findByItem(item_id1);
        Optional<Items> items = itemsRepository.findById(item_id2);
        if (cart != null) {
            Long cart_idd = cart.getCart_id();
            Integer qtyy = cart.getQty();
            String pricee = cart.getItems().getPrice();
            String total_lam = cart.getTotal();
            BigInteger a;
            BigInteger b;

            // total
            b = new BigInteger(String.valueOf(qty));
            a = new BigInteger(String.valueOf(price));
            String total = String.valueOf(b.multiply(a));
            BigInteger total_lama = new BigInteger(String.valueOf(total_lam));
            BigInteger total_neww = new BigInteger(String.valueOf(total));
            String total_new = String.valueOf(total_neww.add(total_lama));
            Carts cartss = cartService.get(cart_idd);
            cartss.setTotal(total_new);

            // qty
            BigInteger qty_lama = new BigInteger(String.valueOf(qtyy));
            BigInteger qty_request_new = new BigInteger(String.valueOf(qty));
            String qty_new = String.valueOf(qty_lama.add(qty_request_new));
            Integer inum = Integer.parseInt(qty_new);

            cartss.setQty(inum);
            cartRepository.save(cartss);

            //relation
            Items items1 = itemsService.get(item_id2);
            String stoc = items.get().getStock();
            BigInteger stock = new BigInteger(String.valueOf(stoc));
            String kurangi_stock = String.valueOf(stock.subtract(b));
            items1.setStock(kurangi_stock);
            itemsRepository.save(items1);

            return "sukses";

        } else if (cart == null) {
            Date currentSqlDate = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            String created_at = sdf2.format(currentSqlDate);
            BigInteger a;
            BigInteger b;

            b = new BigInteger(String.valueOf(qty));
            a = new BigInteger(String.valueOf(price));
            String total = String.valueOf(b.multiply(a));
            String discount = "0";
            cartRepository.save(new Carts(customer,qty,transaction_code,id_users,item_idd,created_at,discount,price,total,item_id1));

            //relation
            Items items1 = itemsService.get(item_id2);
            String stoc = items.get().getStock();
            BigInteger stock = new BigInteger(String.valueOf(stoc));
            String kurangi_stock = String.valueOf(stock.subtract(b));
            items1.setStock(kurangi_stock);
            itemsRepository.save(items1);
            return "sukses";
        }
        return null;
    }

    @RequestMapping(value = "delete/{cart_id}",method = RequestMethod.GET)
    private String getDeleteCart(
            @PathVariable(name = "cart_id") Long id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request
    )throws Exception{

        Optional<Carts> carts = cartRepository.findById(id);
        Carts carts1 = cartService.get(id);

        String idd = carts.get().getItem();
        long iddd = Long.parseLong(idd);
        Optional<Items> items = itemsRepository.findById(iddd);
        Items items1 = itemsService.get(iddd);

        String stock_awa = items.get().getStock();
        Integer qty_cart = carts.get().getQty();

        BigInteger qty_carts = new BigInteger(String.valueOf(qty_cart));

        BigInteger stock_awal = new BigInteger(String.valueOf(stock_awa));
        String kembali = String.valueOf(stock_awal.add(qty_carts));
//        Integer kembali1=Integer.parseInt(kembali);

        items1.setStock(kembali);
        itemsRepository.save(items1);
        cartService.delete(id);
        return "sukses";
    }

    @RequestMapping(value = "update/{cart_id}",method = RequestMethod.POST)
    private String getUpdateCart(
            @PathVariable(name = "cart_id") Long id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request,
            @RequestParam(value = "price") Integer price,
            @RequestParam(value = "qty") Integer qty,
            @RequestParam(value = "discount_item") String discount_item,
            @RequestParam(value = "total") String total
    )throws Exception{
        Carts carts = cartService.find(id);
        carts.setPrice(price);
        carts.setQty(qty);
        carts.setDiscount_item(discount_item);
        carts.setTotal(total);
        cartRepository.save(carts);
        return "sukses";
    }

    @RequestMapping(value = "min/{cart_id}",method = RequestMethod.GET)
    private String getMinCart(
            @PathVariable(name = "cart_id") Long id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request
    )throws Exception{
        Optional<Carts> carts = cartRepository.findById(id);
        Carts cartss = cartService.find(id);
        Integer qty_lam = carts.get().getQty();
        BigInteger qty_lama = new BigInteger(String.valueOf(qty_lam));
        String nilaiStr = "1";
        BigInteger qty_new = new BigInteger(nilaiStr);
        String qty_new_save = String.valueOf(qty_lama.subtract(qty_new));
        Integer inum = Integer.parseInt(qty_new_save);
        cartss.setQty(inum);

        //total
        String total_lam = carts.get().getTotal();
        BigInteger total_lama = new BigInteger(String.valueOf(total_lam));
        BigInteger qty_neww = new BigInteger(nilaiStr);
        String pricee = carts.get().getItems().getPrice();
        BigInteger priceee = new BigInteger(String.valueOf(pricee));
        String total_bar = String.valueOf(priceee.multiply(qty_neww));
        BigInteger total_n = new BigInteger(String.valueOf(total_bar));
        String total_baru = String.valueOf(total_lama.subtract(total_n));
        cartss.setTotal(total_baru);

        //update stock when min
        String idd = carts.get().getItem();
        long iddd = Long.parseLong(idd);
        Optional<Items> items = itemsRepository.findById(iddd);
        Items items1 = itemsService.get(iddd);
        String stock_awa = items.get().getStock();
        BigInteger stock_awal = new BigInteger(String.valueOf(stock_awa));
        String nilaiStr1 = "1";
        BigInteger qty_new8 = new BigInteger(nilaiStr1);
        String kembali = String.valueOf(stock_awal.add(qty_new8));
        items1.setStock(kembali);
        itemsRepository.save(items1);

        //
        cartRepository.save(cartss);


        return "sukses";
    }

    @RequestMapping(value = "plus/{cart_id}",method = RequestMethod.GET)
    private String getPlusCart(
            @PathVariable(name = "cart_id") Long id,
            RedirectAttributes redirAttrs,
            HttpServletRequest request
    )throws Exception{
        Optional<Carts> carts = cartRepository.findById(id);
        Carts cartss = cartService.find(id);
        Integer qty_lam = carts.get().getQty();
        BigInteger qty_lama = new BigInteger(String.valueOf(qty_lam));
        String nilaiStr = "1";
        BigInteger qty_new = new BigInteger(nilaiStr);
        String qty_new_save = String.valueOf(qty_lama.add(qty_new));
        Integer inum = Integer.parseInt(qty_new_save);
        cartss.setQty(inum);

        //total
        String total_lam = carts.get().getTotal();
        BigInteger total_lama = new BigInteger(String.valueOf(total_lam));
        BigInteger qty_neww = new BigInteger(nilaiStr);
        String pricee = carts.get().getItems().getPrice();
        BigInteger priceee = new BigInteger(String.valueOf(pricee));
        String total_bar = String.valueOf(priceee.multiply(qty_neww));
        BigInteger total_n = new BigInteger(String.valueOf(total_bar));
        String total_baru = String.valueOf(total_lama.add(total_n));
        cartss.setTotal(total_baru);

        //update stock when min
        String idd = carts.get().getItem();
        long iddd = Long.parseLong(idd);
        Optional<Items> items = itemsRepository.findById(iddd);
        Items items1 = itemsService.get(iddd);
        String stock_awa = items.get().getStock();
        BigInteger stock_awal = new BigInteger(String.valueOf(stock_awa));
        String nilaiStr1 = "1";
        BigInteger qty_new8 = new BigInteger(nilaiStr1);
        String kembali = String.valueOf(stock_awal.subtract(qty_new8));
        items1.setStock(kembali);
        itemsRepository.save(items1);

        //
        cartRepository.save(cartss);
        return "sukses";
    }
}
