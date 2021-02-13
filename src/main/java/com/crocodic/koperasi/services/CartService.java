package com.crocodic.koperasi.services;


import com.crocodic.koperasi.models.Carts;
import com.crocodic.koperasi.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository repo;

    public List<Carts> listAll() {
        return repo.findAll();
    }

    public Page<Carts> findAllPaginate(String filter, Pageable pageable){
        return repo.filterQuery(
                filter,pageable
        );
    }

    public Carts get(Long id) {
        return repo.findById(id).get();

    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Carts find(Long id) {
        Optional<Carts> data = repo.findById(id);
        return data.orElse(null);
    }

}
