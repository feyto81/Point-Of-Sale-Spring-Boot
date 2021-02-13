package com.crocodic.koperasi.services;

import com.crocodic.koperasi.models.Categories;
import com.crocodic.koperasi.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    public List<Categories> listAll() {
        return categoriesRepository.findAll();
    }

    public void save(Categories categories) {
        categoriesRepository.save(categories);
    }

    public Categories find(Long id) {
        Optional<Categories> data = categoriesRepository.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        categoriesRepository.deleteById(id);
    }



    public Page<Categories> findAllPaginate(String filter, Pageable pageable){
        return categoriesRepository.filterQuery(
                filter,pageable
        );
    }

}
