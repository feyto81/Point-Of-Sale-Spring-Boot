package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.Customers;
import com.crocodic.koperasi.repository.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomersService {

    @Autowired
    private CustomersRepository customersRepository;

    public List<Customers> listAll() {
        return customersRepository.findAll();
    }

    public void save(Customers customers) {
        customersRepository.save(customers);
    }

    public Customers find(Long id) {
        Optional<Customers> data = customersRepository.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        customersRepository.deleteById(id);
    }



    public Page<Customers> findAllPaginate(String filter, Pageable pageable){
        return customersRepository.filterQuery(
                filter,pageable
        );
    }

}
