package com.netocampana.springbootessentials.client;

import com.netocampana.springbootessentials.entities.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class SpringClient {

    public static void main(String[] args) {


        ResponseEntity<Anime> animeResponseEntity = new RestTemplate()
                .getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);


        log.info("Response Entity {} ", animeResponseEntity);

        log.info("Response Data {} ", animeResponseEntity.getBody());

        Anime anime = new RestTemplate()
                .getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);

        log.info("Anime {}", anime);

//       ResponseEntity<List<Anime>> exchangeAnime = new RestTemplate()
//                .exchange("http://localhost:8080/animes/", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
//                });
//
//        log.info("Anime List {}", exchangeAnime.getBody());
//        ResponseEntity<PageableResponse<Anime>> exchangeAnime = new RestTemplate()
//                .exchange("http://localhost:8080/animes/", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
//                });
//
//        log.info("Anime List {}", exchangeAnime.getBody());
    }
}

