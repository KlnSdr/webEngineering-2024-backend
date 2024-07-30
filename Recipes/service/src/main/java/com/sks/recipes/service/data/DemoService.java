package com.sks.recipes.service.data;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {
    private DemoRepository demoRepository;

    public List<DemoEntity> getAll() {
        return demoRepository.findAll();
    }

    public DemoEntity save(DemoEntity demoEntity) {
        return demoRepository.save(demoEntity);
    }
}
