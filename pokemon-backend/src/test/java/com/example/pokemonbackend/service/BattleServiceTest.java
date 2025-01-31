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
        Pokemon p1 = new Pokemon("Charmander", "fire", "url1", 10);
        Pokemon p2 = new Pokemon("Bulbasaur", "grass", "url2", 15);

        when(pokemonService.getPokemonByName("Charmander")).thenReturn(p1);
        when(pokemonService.getPokemonByName("Bulbasaur")).thenReturn(p2);

        BattleResultDTO result = battleService.simulateBattle("Charmander", "Bulbasaur", 15, 10);

        assertEquals("Charmander", result.getWinner().getName());
    }

    @Test
    public void testBattle_NoTypeAdvantage_HigherStrengthWins() {
        Pokemon p1 = new Pokemon("Pidgey", "normal", "url1", 10);
        Pokemon p2 = new Pokemon("Rattata", "normal", "url2", 15);

        when(pokemonService.getPokemonByName("Pidgey")).thenReturn(p1);
        when(pokemonService.getPokemonByName("Rattata")).thenReturn(p2);

        BattleResultDTO result = battleService.simulateBattle("Pidgey", "Rattata", 10, 15);

        assertEquals("Rattata", result.getWinner().getName());
    }

    @Test
    public void testBattle_NoTypeAdvantage_SameStrength_RandomWinner() {
        Pokemon p1 = new Pokemon("Pidgey", "normal", "url1", 10);
        Pokemon p2 = new Pokemon("Rattata", "normal", "url2", 10);

        when(pokemonService.getPokemonByName("Pidgey")).thenReturn(p1);
        when(pokemonService.getPokemonByName("Rattata")).thenReturn(p2);

        BattleResultDTO result = battleService.simulateBattle("Pidgey", "Rattata", 10, 10);

        assertTrue(
                result.getWinner().getName().equals("Pidgey")
                        || result.getWinner().getName().equals("Rattata")
        );
    }

    @Test
    public void testBattle_PokemonNotFound() {
        when(pokemonService.getPokemonByName("Unknown")).thenThrow(new PokemonNotFoundException("Not found"));

        assertThrows(PokemonNotFoundException.class, () -> {
            battleService.simulateBattle("Unknown", "Pikachu", 10, 10);
        });
    }
}
