package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.CartPembelian;
import com.crocodic.koperasi.repository.CartPembelianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartPembelianService {

    @Autowired
    private CartPembelianRepository repo;

    public List<CartPembelian> listAll() {
        return repo.findAll();
    }

    public Page<CartPembelian> findAllPaginate(String filter, Pageable pageable){
        return repo.filterQuery(
                filter,pageable
        );
    }

    public CartPembelian get(Long id) {
        return repo.findById(id).get();

    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public CartPembelian find(Long id) {
        Optional<CartPembelian> data = repo.findById(id);
        return data.orElse(null);
    }
}
