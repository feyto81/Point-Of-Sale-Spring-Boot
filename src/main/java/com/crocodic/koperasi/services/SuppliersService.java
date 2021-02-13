package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.Suppliers;
import com.crocodic.koperasi.repository.SuppliersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SuppliersService {

    @Autowired
    private SuppliersRepository suppliersRepository;

    public List<Suppliers> listAll() {
        return suppliersRepository.findAll();
    }

    public void save(Suppliers suppliers) {
        suppliersRepository.save(suppliers);
    }

    public Suppliers find(Long id) {
        Optional<Suppliers> data = suppliersRepository.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        suppliersRepository.deleteById(id);
    }



    public Page<Suppliers> findAllPaginate(String filter, Pageable pageable){
        return suppliersRepository.filterQuery(
                filter,pageable
        );
    }

}
