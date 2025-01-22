package com.example.pokemonbackend.service;

import com.example.pokemonbackend.dto.BattleResultDTO;
import com.example.pokemonbackend.dto.PokemonDTO;
import com.example.pokemonbackend.model.BattleHistory;
import com.example.pokemonbackend.model.Pokemon;
import com.example.pokemonbackend.model.TypeAdvantage;
import com.example.pokemonbackend.repository.BattleHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BattleService {

    private final PokemonService pokemonService;
    private final BattleHistoryRepository battleHistoryRepository;
    private final PokemonTypeService pokemonTypeService;

    @Autowired
    public BattleService(PokemonService pokemonService,
                         BattleHistoryRepository battleHistoryRepository,
                         PokemonTypeService pokemonTypeService) {
        this.pokemonService = pokemonService;
        this.battleHistoryRepository = battleHistoryRepository;
        this.pokemonTypeService = pokemonTypeService;
    }

    public BattleResultDTO simulateBattle(String pokemon1, String pokemon2,
                                          int strength1, int strength2) {

        Pokemon p1 = pokemonService.getPokemonByName(pokemon1);
        p1.setStrength(strength1);
        Pokemon p2 = pokemonService.getPokemonByName(pokemon2);
        p2.setStrength(strength2);

        Pokemon winner = determineWinner(p1, p2);
        ZonedDateTime battleDate = ZonedDateTime.now(ZoneOffset.UTC);


        BattleHistory history = new BattleHistory();
        history.setPokemon1Name(p1.getName());
        history.setPokemon1Strength(p1.getStrength());

        history.setPokemon2Name(p2.getName());
        history.setPokemon2Strength(p2.getStrength());

        history.setWinnerName(winner.getName());
        history.setBattleTime(battleDate.toLocalDateTime());

        battleHistoryRepository.save(history);


        PokemonDTO dto1 = toDTO(p1);
        PokemonDTO dto2 = toDTO(p2);
        PokemonDTO winnerDto = toDTO(winner);

        return new BattleResultDTO(dto1, dto2, winnerDto);
    }

    private Pokemon determineWinner(Pokemon p1, Pokemon p2) {

        String p1Type = p1.getType().toLowerCase();
        String p2Type = p2.getType().toLowerCase();

        Set<String> p1DoubleDamageTo = getTypeDoubleDamageTo(p1Type);
        Set<String> p2DoubleDamageTo = getTypeDoubleDamageTo(p2Type);

        boolean p1HasAdvantage = p1DoubleDamageTo.contains(p2Type);
        boolean p2HasAdvantage = p2DoubleDamageTo.contains(p1Type);
        int p1Strength = p1.getStrength();
        int p2Strength = p2.getStrength();

        if (p1HasAdvantage && !p2HasAdvantage) {
           p1Strength = p1Strength * 2;
        } else if (p2HasAdvantage && !p1HasAdvantage) {
            p2Strength = p2Strength * 2;
        }

        if (p1Strength > p2Strength) {
            return p1;
        } else if (p2Strength > p1Strength) {
            return p2;
        } else {

            return ThreadLocalRandom.current().nextBoolean() ? p1 : p2;
        }
    }

    /**
     * Retrieves all types that the given type deals double damage to.
     *
     * @param type The type name.
     * @return Set of type names that the given type has an advantage against.
     */
    private Set<String> getTypeDoubleDamageTo(String type) {
        try {
            TypeAdvantage typeAdvantage = pokemonTypeService.getTypeAdvantage(type);
            return new HashSet<>(typeAdvantage.getDoubleDamageTo());
        } catch (Exception e) {


            return Collections.emptySet();
        }
    }

    private PokemonDTO toDTO(Pokemon pokemon) {
        return new PokemonDTO(
                pokemon.getName(),
                pokemon.getType(),
                pokemon.getImageUrl(),
                pokemon.getStrength()
        );
    }
}

