package com.netocampana.springbootessentials.repository;

import com.netocampana.springbootessentials.entities.Anime;
import com.netocampana.springbootessentials.entities.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    AuthUser findByUsername (String username);
}
