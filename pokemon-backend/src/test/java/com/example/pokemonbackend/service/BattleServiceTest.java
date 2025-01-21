package com.example.pokemonbackend.service;

import com.example.pokemonbackend.dto.BattleResultDTO;
import com.example.pokemonbackend.exception.PokemonNotFoundException;
import com.example.pokemonbackend.model.Pokemon;
import com.example.pokemonbackend.repository.BattleHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BattleServiceTest {

    @Mock
    private PokemonService pokemonService;

    @Mock
    private BattleHistoryRepository battleHistoryRepository;

    @InjectMocks
    private BattleService battleService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBattle_Pokemon1HasTypeAdvantage() {
        Pokemon p1 = new Pokemon("Charmander", Arrays.asList("fire"), "url1", 10);
        Pokemon p2 = new Pokemon("Bulbasaur", Arrays.asList("grass"), "url2", 15);

        when(pokemonService.getPokemonByName("Charmander")).thenReturn(p1);
        when(pokemonService.getPokemonByName("Bulbasaur")).thenReturn(p2);

        BattleResultDTO result = battleService.simulateBattle("Charmander", "Bulbasaur");

        assertEquals("Charmander", result.getWinner().getName());
    }

    @Test
    public void testBattle_Pokemon2HasTypeAdvantage() {
        Pokemon p1 = new Pokemon("Squirtle", Arrays.asList("water"), "url1", 12);
        Pokemon p2 = new Pokemon("Charmander", Arrays.asList("fire"), "url2", 14);

        when(pokemonService.getPokemonByName("Squirtle")).thenReturn(p1);
        when(pokemonService.getPokemonByName("Charmander")).thenReturn(p2);

        BattleResultDTO result = battleService.simulateBattle("Squirtle", "Charmander");

        assertEquals("Squirtle", result.getWinner().getName());
    }

    @Test
    public void testBattle_NoTypeAdvantage_HigherStrengthWins() {
        Pokemon p1 = new Pokemon("Pidgey", Arrays.asList("normal"), "url1", 10);
        Pokemon p2 = new Pokemon("Rattata", Arrays.asList("normal"), "url2", 15);

        when(pokemonService.getPokemonByName("Pidgey")).thenReturn(p1);
        when(pokemonService.getPokemonByName("Rattata")).thenReturn(p2);

        BattleResultDTO result = battleService.simulateBattle("Pidgey", "Rattata");

        assertEquals("Rattata", result.getWinner().getName());
    }

    @Test
    public void testBattle_NoTypeAdvantage_SameStrength_RandomWinner() {
        Pokemon p1 = new Pokemon("Pidgey", Arrays.asList("normal"), "url1", 10);
        Pokemon p2 = new Pokemon("Rattata", Arrays.asList("normal"), "url2", 10);

        when(pokemonService.getPokemonByName("Pidgey")).thenReturn(p1);
        when(pokemonService.getPokemonByName("Rattata")).thenReturn(p2);

        BattleResultDTO result = battleService.simulateBattle("Pidgey", "Rattata");

        assertTrue(
                result.getWinner().getName().equals("Pidgey") ||
                        result.getWinner().getName().equals("Rattata")
        );
    }

    @Test
    public void testBattle_PokemonNotFound() {
        when(pokemonService.getPokemonByName("Unknown")).thenThrow(new PokemonNotFoundException("Not found"));

        assertThrows(PokemonNotFoundException.class, () -> {
            battleService.simulateBattle("Unknown", "Pikachu");
        });
    }
}

