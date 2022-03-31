package com.netocampana.springbootessentials.controller;

import com.netocampana.springbootessentials.entities.Anime;
import com.netocampana.springbootessentials.repository.AnimeRepository;
import com.netocampana.springbootessentials.services.AnimeService;
import com.netocampana.springbootessentials.util.AnimeCreator;
import com.netocampana.springbootessentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimeControllerITest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private AnimeRepository animeRepositoryMock;


    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    public void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));

    }


    @Test
    @DisplayName("listAll returns a pageable list of animes when successfull")
    public void listAll_ReturnListOfAnimesInsideObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
        }).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns an anime when successfull")
    public void findById_ReturnsAnAnime_WhenSuccesful(){
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime anime = testRestTemplate.getForObject("/animes/1", Anime.class);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of animes when successful")
    public void findByname_ReturnsListOfAnime_WhenSuccesful(){

        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animeList = testRestTemplate.exchange("/animes/find?name='tensei'", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();


        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("save creates an anime when successfull")
    public void save_CreatesAnAnime_WhenSuccesful(){
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

        Anime anime = testRestTemplate.exchange("/animes/", HttpMethod.POST, createJsonHttpEntity(animeToBeSaved), Anime.class).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete removes an anime when successfull")
    public void delete_RemovesAnAnime_WhenSuccesful(){


        ResponseEntity<Void> responseEntity = testRestTemplate.exchange("/animes/1", HttpMethod.DELETE,null, Void.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update change an anime when successfull")
    public void update_ChangesAnAnime_WhenSuccesful(){

        Anime animeToBeUpdated = AnimeCreator.createValidUpdatedAnime();
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange("/animes/", HttpMethod.PUT, createJsonHttpEntity(animeToBeUpdated),Void.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();

    }

    private HttpEntity<Anime> createJsonHttpEntity(Anime anime){
        return new HttpEntity<>(anime, createJsonHeader());
    }

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }





}