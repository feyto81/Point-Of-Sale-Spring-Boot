package com.crocodic.koperasi.services;


import com.crocodic.koperasi.models.StockOut;
import com.crocodic.koperasi.repository.StockOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StockOutService {

    @Autowired
    private StockOutRepository repo;

    public List<StockOut> listAll() {
        return repo.findAll();
    }

    public void save(StockOut stockOut) {
        repo.save(stockOut);
    }

    public StockOut find(Long id) {
        Optional<StockOut> data = repo.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }


    public Page<StockOut> findAllPaginate(String filter, Pageable pageable){
        return repo.filterQuery(
                filter,pageable
        );
    }

}
