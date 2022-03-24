package com.netocampana.springbootessentials.util;

import com.netocampana.springbootessentials.entities.Anime;
import com.netocampana.springbootessentials.exceptions.ResourceNotFoundException;
import com.netocampana.springbootessentials.repository.AnimeRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Utils {

    public String formatLocalDateTimeToDatabaseStyle(LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public Anime findAnimeOrThrowNotFound (int id, AnimeRepository animeRepository){
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anime not found"));
    }
}
