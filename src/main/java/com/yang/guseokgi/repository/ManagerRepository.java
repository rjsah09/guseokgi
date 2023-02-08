package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    @Query("delete from Manager m")
    @Modifying(clearAutomatically = true)
    public void deleteAll();

    @Query("select m from Manager m where m.uid = :uid and m.password = :password")
    Optional<Manager> findByIdAndPassword(@Param("uid")String uid, @Param("password")String password);
}
