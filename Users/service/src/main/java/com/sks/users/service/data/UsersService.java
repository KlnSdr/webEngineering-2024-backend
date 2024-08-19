package com.sks.users.service.data;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<UsersEntity> getAll() {
        return usersRepository.findAll();
    }

    public UsersEntity save(UsersEntity usersEntity) {
        return usersRepository.save(usersEntity);
    }

    public Optional<UsersEntity> findById(Long id) {
        return usersRepository.findById(id);
    }

    public Optional<UsersEntity> findByIdpHash(Long id) {
        // TODO hash the id
        return usersRepository.findByIdpHashEquals(id.toString());
    }
}
