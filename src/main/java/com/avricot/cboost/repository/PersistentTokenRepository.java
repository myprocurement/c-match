package com.avricot.cboost.repository;

import com.avricot.cboost.domain.user.PersistentToken;
import com.avricot.cboost.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    List<PersistentToken> findByUser(User user);

}
