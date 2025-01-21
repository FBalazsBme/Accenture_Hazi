package com.example.pokemonbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattleHistoryDTO {
    private PokemonDTO pokemon1;
    private PokemonDTO pokemon2;
    private String winner;

    private LocalDateTime date;
}

