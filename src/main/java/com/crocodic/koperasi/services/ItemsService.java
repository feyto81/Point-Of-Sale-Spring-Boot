package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.Items;
import com.crocodic.koperasi.repository.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemsService {

    @Autowired
    private ItemsRepository repo;

    public List<Items> listAll() {
        return repo.findAll();
    }

    public void save(Items items) {
        repo.save(items);
    }

    public Items find(Long id) {
        Optional<Items> data = repo.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }


    public Page<Items> findAllPaginate(String filter, Pageable pageable){
        return repo.filterQuery(
                filter,pageable
        );
    }

    public Items findFirstByBarcode(String barcode){
        return repo.findByBarcode(barcode);
    }

    public Items getIds(Long id) {
        Optional<Items> items2 = repo.findById(id);
        if (items2.isPresent()) {
            return repo.findById(id).get();
        }
        return null;
    }

    public Items get(Long id) {
        return repo.findById(id).get();
    }
}
