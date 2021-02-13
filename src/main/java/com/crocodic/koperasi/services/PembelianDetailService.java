package com.crocodic.koperasi.services;


import com.crocodic.koperasi.models.PembelianDetails;
import com.crocodic.koperasi.repository.PembelianDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PembelianDetailService {

    @Autowired
    private PembelianDetailsRepository repo;

    public List<PembelianDetails> listAll(String keyword) {
        if (keyword != null) {
            return repo.findAll(keyword);
        }
        return repo.findAll();
    }

}
