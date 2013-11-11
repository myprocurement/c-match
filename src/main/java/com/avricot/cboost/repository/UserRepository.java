package com.avricot.cboost.repository;

import com.avricot.cboost.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(final String login);
}
