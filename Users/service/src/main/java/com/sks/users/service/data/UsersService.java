package com.sks.users.service.data;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private UsersRepository usersRepository;

    public List<UsersEntity> getAll() {
        return usersRepository.findAll();
    }

    public UsersEntity save(UsersEntity usersEntity) {
        return usersRepository.save(usersEntity);
    }
}
