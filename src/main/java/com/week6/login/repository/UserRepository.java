package com.week6.login.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.week6.login.model.UserDtls;


@Repository
public interface UserRepository extends JpaRepository<UserDtls,Integer>{
    
    public boolean existsByUsername(String username);

    public UserDtls findByUsername(String username);

    public UserDtls findById(int id);

        @Query("SELECT c FROM UserDtls c WHERE c.name=:name")
        public List<UserDtls> findByName(@Param("name") String name);

}
