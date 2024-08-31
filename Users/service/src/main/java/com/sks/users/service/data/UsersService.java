package com.sks.users.service.data;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UsersService {
    private final UsersRepository usersRepository;

    /**
     * Constructs a UsersService with the specified UsersRepository.
     *
     * @param usersRepository the repository to use for user operations
     */
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users
     */
    public List<UsersEntity> getAll() {
        return usersRepository.findAll();
    }

    /**
     * Saves a user entity to the repository.
     *
     * @param usersEntity the user entity to save
     * @return the saved user entity
     */
    public UsersEntity save(UsersEntity usersEntity) {
        return usersRepository.save(usersEntity);
    }

    /**
     * Finds a user by their unique identifier.
     *
     * @param id the unique identifier of the user
     * @return an Optional containing the found user, or empty if not found
     */
    public Optional<UsersEntity> findById(Long id) {
        return usersRepository.findById(id);
    }

    /**
     * Finds a user by their IDP hash.
     *
     * @param hash the IDP hash of the user
     * @return an Optional containing the found user, or empty if not found
     */
    public Optional<UsersEntity> findByIdpHash(String hash) {
        return usersRepository.findByIdpHashEquals(hash);
    }

    /**
     * Generates a SHA-256 hash of the user's ID.
     *
     * @param id the unique identifier of the user
     * @return the SHA-256 hash of the ID, or null if the ID is null
     */
    public String hashId(Long id) {
        if (id == null) {
            return null;
        }
        return DigestUtils.sha256Hex(id.toString());
    }
}