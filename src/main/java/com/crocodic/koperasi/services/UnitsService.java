package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.Units;
import com.crocodic.koperasi.repository.UnitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UnitsService {

    @Autowired
    private UnitsRepository unitsRepository;

    public List<Units> listAll() {
        return unitsRepository.findAll();
    }

    public void save(Units units) {
        unitsRepository.save(units);
    }

    public Units find(Long id) {
        Optional<Units> data = unitsRepository.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        unitsRepository.deleteById(id);
    }



    public Page<Units> findAllPaginate(String filter, Pageable pageable){
        return unitsRepository.filterQuery(
                filter,pageable
        );
    }

}
