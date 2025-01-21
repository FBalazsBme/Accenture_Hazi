package com.example.pokemonbackend.service;

import com.example.pokemonbackend.dto.PokemonDTO;
import com.example.pokemonbackend.exception.PokemonNotFoundException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import com.example.pokemonbackend.model.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PokemonService {

    private final RestTemplate restTemplate;
    private final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2";

    @Autowired
    public PokemonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 200))
    public Pokemon getPokemonByName(String name) {
        String url = POKEAPI_BASE_URL + "/pokemon/" + name.toLowerCase();
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            return parsePokemon(response);
        } catch (Exception e) {
            throw new PokemonNotFoundException("Pokémon with name " + name + " not found.");
        }
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 200))
    public List<Pokemon> getTwoRandomPokemons() {

        String url = POKEAPI_BASE_URL + "/pokemon-species?limit=1";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        int total = (int) response.get("count");


        Set<Integer> ids = new HashSet<>();
        while (ids.size() < 2) {
            ids.add(ThreadLocalRandom.current().nextInt(1, total + 1));
        }

        List<Pokemon> pokemons = new ArrayList<>();
        for (int id : ids) {
            String pokemonUrl = POKEAPI_BASE_URL + "/pokemon/" + id;
            Map<String, Object> pokemonResponse = restTemplate.getForObject(pokemonUrl, Map.class);
            pokemons.add(parsePokemon(pokemonResponse));
        }

        return pokemons;
    }

    public PokemonDTO getPokemonDTOByName(String name) {
        Pokemon pokemon = getPokemonByName(name);

        return new PokemonDTO(
                pokemon.getName(),
                pokemon.getType(),
                pokemon.getImageUrl(),
                0
        );
    }

    private Pokemon parsePokemon(Map<String, Object> data) {
        String name = (String) data.get("name");


        List<Map<String, Object>> typesList = (List<Map<String, Object>>) data.get("types");
        String typeName = null;
        if (typesList != null && !typesList.isEmpty()) {
            Map<String, Object> typeEntry = typesList.get(0);
            Map<String, Object> typeInfo = (Map<String, Object>) typeEntry.get("type");
            typeName = (String) typeInfo.get("name");
        }


        Map<String, Object> sprites = (Map<String, Object>) data.get("sprites");
        String imageUrl = (String) sprites.get("front_default");


        int strength = ThreadLocalRandom.current().nextInt(1, 21);

        return new Pokemon(name, typeName, imageUrl, strength);
    }
}

