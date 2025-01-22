package com.example.pokemonbackend.service;

import com.example.pokemonbackend.exception.TypeNotFoundException;
import com.example.pokemonbackend.model.TypeAdvantage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PokemonTypeService {

    private final RestTemplate restTemplate;
    private final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/type/";

    @Autowired
    public PokemonTypeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "typeAdvantages", key = "#typeName.toLowerCase()")
    public TypeAdvantage getTypeAdvantage(String typeName) {
        String url = POKEAPI_BASE_URL + typeName.toLowerCase();
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            return parseTypeAdvantage(response);
        } catch (Exception e) {
            throw new TypeNotFoundException("Type with name " + typeName + " not found.");
        }
    }

    private TypeAdvantage parseTypeAdvantage(Map<String, Object> data) {
        String typeName = (String) data.get("name");

        Map<String, Object> damageRelations = (Map<String, Object>) data.get("damage_relations");

        List<String> doubleDamageTo = extractTypes((List<Map<String, Object>>) damageRelations.get("double_damage_to"));
        List<String> halfDamageTo = extractTypes((List<Map<String, Object>>) damageRelations.get("half_damage_to"));
        List<String> noDamageTo = extractTypes((List<Map<String, Object>>) damageRelations.get("no_damage_to"));

        return new TypeAdvantage(typeName, doubleDamageTo, halfDamageTo, noDamageTo);
    }

    private List<String> extractTypes(List<Map<String, Object>> typesList) {
        if (typesList == null) return Collections.emptyList();
        return typesList.stream()
                .map(typeMap -> (String) typeMap.get("name"))
                .collect(Collectors.toList());
    }
}
