package com.netocampana.springbootessentials.controller;


import com.netocampana.springbootessentials.entities.Anime;
import com.netocampana.springbootessentials.services.AnimeService;
import com.netocampana.springbootessentials.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {


    private final Utils utils;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> listAll(Pageable pageable){
//        log.info("Date formatted {}", utils.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.listAll(pageable));
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable int id){
        return ResponseEntity.ok(animeService.findById(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam(value = "name") String name){
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid Anime anime){
        return ResponseEntity.ok(animeService.save(anime));
    }


    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable int id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Anime anime){
        animeService.update(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
