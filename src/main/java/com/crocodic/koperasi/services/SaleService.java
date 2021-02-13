package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.Sales;
import com.crocodic.koperasi.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SaleService {

    @Autowired
    private SalesRepository salesRepository;

    public List<Sales> listAll() {
        return salesRepository.findAll();
    }

    public void save(Sales sales) {
        salesRepository.save(sales);
    }

    public Sales find(Long id) {
        Optional<Sales> data = salesRepository.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        salesRepository.deleteById(id);
    }



    public Page<Sales> findAllPaginate(String filter, Pageable pageable){
        return salesRepository.filterQuery(
                filter,pageable
        );
    }
}
