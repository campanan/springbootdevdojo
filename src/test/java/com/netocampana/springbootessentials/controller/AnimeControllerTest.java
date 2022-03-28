package com.netocampana.springbootessentials.controller;

import com.netocampana.springbootessentials.entities.Anime;
import com.netocampana.springbootessentials.services.AnimeService;
import com.netocampana.springbootessentials.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    public void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.findById(ArgumentMatchers.anyInt()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyInt());

        BDDMockito.doNothing().when(animeServiceMock).update(AnimeCreator.createAnimeToBeSaved());
    }


    @Test
    @DisplayName("listAll returns a pageable list of animes when successfull")
    public void listAll_ReturnListOfAnimesInsideObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeController.listAll(null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns an anime when successfull")
    public void findById_ReturnsAnAnime_WhenSuccesful(){
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns pageable list of animes when successful")
    public void findByname_ReturnsListOfAnime_WhenSuccesful(){

        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animeList = animeController.findByName("DBZ").getBody();

        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("save creates an anime when successfull")
    public void save_CreatesAnAnime_WhenSuccesful(){
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime anime = animeController.save(animeToBeSaved).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete removes an anime when successfull")
    public void delete_RemovesAnAnime_WhenSuccesful(){


        ResponseEntity<Void> responseEntity = animeController.delete(1);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update change an anime when successfull")
    public void update_ChangesAnAnime_WhenSuccesful(){

        Anime animeToBeUpdated = AnimeCreator.createValidUpdatedAnime();
        ResponseEntity<Void> responseEntity = animeController.update(animeToBeUpdated);


        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();

    }

}