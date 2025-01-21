package com.example.pokemonbackend.dto;

import com.example.pokemonbackend.model.Pokemon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokemonDTO {
    private String name;
    private String type;
    private String image;
    private int strength;

    public static PokemonDTO fromPokemon(Pokemon pokemon) {
        if (pokemon == null) {
            return null;
        }

        return new PokemonDTO(
                pokemon.getName(),
                pokemon.getType(),
                pokemon.getImageUrl(),
                pokemon.getStrength()
        );
    }
}

