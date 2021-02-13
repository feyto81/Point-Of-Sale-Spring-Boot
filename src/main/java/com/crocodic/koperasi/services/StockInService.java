package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.StockIn;
import com.crocodic.koperasi.repository.StockInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StockInService {

    @Autowired
    private StockInRepository repo;

    public List<StockIn> listAll() {
        return repo.findAll();
    }

    public void save(StockIn stockIn) {
        repo.save(stockIn);
    }

    public StockIn find(Long id) {
        Optional<StockIn> data = repo.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }


    public Page<StockIn> findAllPaginate(String filter, Pageable pageable){
        return repo.filterQuery(
                filter,pageable
        );
    }

}
