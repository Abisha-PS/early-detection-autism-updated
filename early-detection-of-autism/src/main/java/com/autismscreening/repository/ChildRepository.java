package com.autismscreening.repository;

import com.autismscreening.model.Child;
import com.autismscreening.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByParent(User parent);
    Optional<Child> findByAnonymousCode(String anonymousCode);
}

