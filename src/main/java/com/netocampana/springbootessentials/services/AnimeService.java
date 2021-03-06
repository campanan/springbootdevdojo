package com.netocampana.springbootessentials.services;

import com.netocampana.springbootessentials.entities.Anime;
import com.netocampana.springbootessentials.repository.AnimeRepository;
import com.netocampana.springbootessentials.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Repository
@RequiredArgsConstructor
public class AnimeService {


    private final Utils utils;
    private final AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable){
        return animeRepository.findAll(pageable);
    }

    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

    @Transactional
    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }
    public Anime findById(int id){
         return utils.findAnimeOrThrowNotFound(id, animeRepository);
    }

    public void delete(int id){
        animeRepository.delete(utils.findAnimeOrThrowNotFound(id,animeRepository));
    }

    public void update(Anime anime){
        animeRepository.save(anime);
    }




}
