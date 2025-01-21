package com.example.pokemonbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattleResultDTO {
    private PokemonDTO pokemon1;
    private PokemonDTO pokemon2;
    private PokemonDTO winner;
}

