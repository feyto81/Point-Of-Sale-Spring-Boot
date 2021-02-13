package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.Kas;
import com.crocodic.koperasi.repository.KasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KasService {

    @Autowired
    private KasRepository repo;
    public List<Kas> listAll() {
        return repo.findAll();
    }

    public void save(Kas kas) {
        repo.save(kas);
    }

    public Kas find(Long id) {
        Optional<Kas> data = repo.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }


    public Page<Kas> findAllPaginate(String filter, Pageable pageable){
        return repo.filterQuery(
                filter,pageable
        );
    }



    public Kas getIds(Long id) {
        Optional<Kas> kas = repo.findById(id);
        if (kas.isPresent()) {
            return repo.findById(id).get();
        }
        return null;
    }

    public Kas get(Long id) {
        return repo.findById(id).get();
    }
}
