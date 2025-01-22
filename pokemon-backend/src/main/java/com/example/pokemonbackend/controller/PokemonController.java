package com.example.pokemonbackend.controller;

import com.example.pokemonbackend.dto.PokemonDTO;
import com.example.pokemonbackend.model.Pokemon;
import com.example.pokemonbackend.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pokemons")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping("/random")
    public ResponseEntity<List<PokemonDTO>> getTwoRandomPokemons() {
        List<Pokemon> pokemons = pokemonService.getTwoRandomPokemons();
        List<PokemonDTO> dtoList = pokemons.stream().map(p -> new PokemonDTO(
                p.getName(),
                p.getType(),
                p.getImageUrl(),
                p.getStrength()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }
}

