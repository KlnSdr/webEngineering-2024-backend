package com.sks.users.service.data;

import org.apache.commons.codec.digest.DigestUtils;
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

    public Optional<UsersEntity> findByIdpHash(String hash) {
        return usersRepository.findByIdpHashEquals(hash);
    }

    public String hashId(Long id) {
        if (id == null) {
            return null;
        }
        return DigestUtils.sha256Hex(id.toString());
    }
}
