package com.netocampana.springbootessentials.repository;

import com.netocampana.springbootessentials.entities.Anime;
import com.netocampana.springbootessentials.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Repository
@RequiredArgsConstructor
public class AnimeRepository {


    private final Utils utils;
    private static List<Anime> animes;

    static {
        animes = new ArrayList<>(List.of(new Anime(1, "DBZ"),
                new Anime(2, "Yu yu Hakusho"),
                new Anime(3,"Bleach")));
    }

    public List<Anime> listAll(){
        return animes;
    }

    public Anime save(Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextInt(4,10000));
        animes.add(anime);
        return anime;
    }
    public Anime findById(int id){
         return utils.findAnimeOrThrowNotFound(id, animes);
    }

    public void delete(int id){
        animes.remove(utils.findAnimeOrThrowNotFound(id,animes));
    }
    public void update(Anime anime){
        animes.remove(utils.findAnimeOrThrowNotFound(anime.getId(),animes));
        animes.add(anime);
    }
}
