package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.Pembelian;
import com.crocodic.koperasi.repository.PembelianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PembelianService {

    @Autowired
    private PembelianRepository pembelianRepository;

    public List<Pembelian> listAll() {
        return pembelianRepository.findAll();
    }

    public void save(Pembelian pembelian) {
        pembelianRepository.save(pembelian);
    }

    public Pembelian find(Long id) {
        Optional<Pembelian> data = pembelianRepository.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        pembelianRepository.deleteById(id);
    }



    public Page<Pembelian> findAllPaginate(String filter, Pageable pageable){
        return pembelianRepository.filterQuery(
                filter,pageable
        );
    }
}
