package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.SaleDetails;
import com.crocodic.koperasi.repository.SalesDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SaleDetailsService {

    @Autowired
    private SalesDetailsRepository repo;

    public List<SaleDetails> listAll(String keyword) {
        if (keyword != null) {
            return repo.findAll(keyword);
        }
        return repo.findAll();
    }

}
