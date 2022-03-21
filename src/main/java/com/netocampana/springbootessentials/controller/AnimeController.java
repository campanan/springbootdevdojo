package com.netocampana.springbootessentials.controller;


import com.netocampana.springbootessentials.entities.Anime;
import com.netocampana.springbootessentials.repository.AnimeRepository;
import com.netocampana.springbootessentials.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {


    private final Utils utils;
    private final AnimeRepository animeRepository;

    @GetMapping
    public ResponseEntity<List<Anime>> listAll(){
        log.info("Date formatted {}", utils.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeRepository.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable int id){
        return ResponseEntity.ok(animeRepository.findById(id));
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody Anime anime){
        return ResponseEntity.ok(animeRepository.save(anime));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        animeRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Anime anime){
        animeRepository.update(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
