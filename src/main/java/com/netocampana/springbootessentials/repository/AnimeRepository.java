package com.netocampana.springbootessentials.repository;

import com.netocampana.springbootessentials.entities.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Integer> {
    List<Anime> findByName(String name);
}
