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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;

    @MockBean
    private AnimeRepository animeRepositoryMock;


    @Mock
    private AnimeService animeServiceMock;


    @Lazy
    @TestConfiguration
    static class Config{
        @Bean(name="testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("campana","academy");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name="testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("neto","academy");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

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
        Page<Anime> animePage = testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET, null,
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
        Anime anime = testRestTemplateRoleUser.getForObject("/animes/1", Anime.class);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of animes when successful")
    public void findByname_ReturnsListOfAnime_WhenSuccesful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animeList = testRestTemplateRoleUser.exchange("/animes/find?name='tensei'", HttpMethod.GET, null,
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
        Anime anime = testRestTemplateRoleAdmin.exchange("/animes/", HttpMethod.POST, createJsonHttpEntity(animeToBeSaved), Anime.class).getBody();
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete removes an anime when successfull")
    public void delete_RemovesAnAnime_WhenSuccesful(){
        ResponseEntity<Void> responseEntity = testRestTemplateRoleAdmin.exchange("/animes/1", HttpMethod.DELETE,null, Void.class);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update change an anime when successfull")
    public void update_ChangesAnAnime_WhenSuccesful(){

        Anime animeToBeUpdated = AnimeCreator.createValidUpdatedAnime();
        ResponseEntity<Void> responseEntity = testRestTemplateRoleAdmin.exchange("/animes/", HttpMethod.PUT, createJsonHttpEntity(animeToBeUpdated),Void.class);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();

    }

    @Test
    @DisplayName("save return forbidden when user does not have the role admin")
    public void save_Returns403_WhenRoleIsUser() {
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        ResponseEntity<Anime> responseEntity = testRestTemplateRoleUser.exchange("/animes/", HttpMethod.POST,createJsonHttpEntity(animeToBeSaved), Anime.class);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @Test
    @DisplayName("delete return forbidden when user does not have the role admin")
    public void delete_Returns403_WhenRoleIsUser(){


        ResponseEntity<Void> responseEntity = testRestTemplateRoleUser.exchange("/animes/1", HttpMethod.DELETE,null, Void.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    @DisplayName("update return forbidden when user does not have the role admin")
    public void update_Returns403_WhenRoleIsUser(){
        Anime animeToBeUpdated = AnimeCreator.createValidUpdatedAnime();
        ResponseEntity<Void> responseEntity = testRestTemplateRoleUser.exchange("/animes/", HttpMethod.PUT, createJsonHttpEntity(animeToBeUpdated),Void.class);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

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