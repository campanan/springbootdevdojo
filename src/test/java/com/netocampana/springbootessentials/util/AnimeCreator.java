package com.netocampana.springbootessentials.util;

import com.netocampana.springbootessentials.entities.Anime;

public class AnimeCreator {
    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Tensei Shitara Slime Datta ken")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .name("Tensei Shitara Slime Datta ken")
                .id(1)
                .build();
    }
    public static Anime createValidUpdatedAnime(){
        return Anime.builder()
                .name("Tensei Shitara Slime Datta ken Updated")
                .id(1)
                .build();
    }
}
