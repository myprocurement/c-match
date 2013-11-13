package com.avricot.cboost.repository;

import com.avricot.cboost.domain.project.Field;
import com.avricot.cboost.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface FieldRepository extends JpaRepository<Field, Long> {

    @Modifying
    @Query("delete from Field f where f.project.id=?1")
    void deleteByProjectId(final Long projectId);
}
