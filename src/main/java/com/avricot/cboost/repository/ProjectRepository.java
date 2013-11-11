package com.avricot.cboost.repository;

import com.avricot.cboost.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select p from Project p left join fetch p.mapping where p.id=?1")
    Project findOneFetchingMapping(final Long id);
}
