package com.crocodic.koperasi.services;


import com.crocodic.koperasi.models.management.Activity;
import com.crocodic.koperasi.repository.management.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LogsService {

    @Autowired
    private ActivityRepository repo;
    public List<Activity> listAll() {
        return repo.findAll();
    }

    public void save(Activity activity) {
        repo.save(activity);
    }

    public Activity find(Long id) {
        Optional<Activity> data = repo.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }


    public Page<Activity> findAllPaginate(String filter, Pageable pageable){
        return repo.filterQuery(
                filter,pageable
        );
    }



    public Activity getIds(Long id) {
        Optional<Activity> activity = repo.findById(id);
        if (activity.isPresent()) {
            return repo.findById(id).get();
        }
        return null;
    }

    public Activity get(Long id) {
        return repo.findById(id).get();
    }
}
