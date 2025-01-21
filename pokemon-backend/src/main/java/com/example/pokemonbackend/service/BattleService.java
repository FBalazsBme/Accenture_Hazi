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
    private final TypeService typeService;

    @Autowired
    public BattleService(PokemonService pokemonService,
                         BattleHistoryRepository battleHistoryRepository,
                         TypeService typeService) {
        this.pokemonService = pokemonService;
        this.battleHistoryRepository = battleHistoryRepository;
        this.typeService = typeService;
    }

    public BattleResultDTO simulateBattle(String pokemon1Name, String pokemon2Name) {

        Pokemon p1 = pokemonService.getPokemonByName(pokemon1Name);
        Pokemon p2 = pokemonService.getPokemonByName(pokemon2Name);


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

        if (p1HasAdvantage && !p2HasAdvantage) {
            return p1;
        } else if (p2HasAdvantage && !p1HasAdvantage) {
            return p2;
        } else {

            if (p1.getStrength() > p2.getStrength()) {
                return p1;
            } else if (p2.getStrength() > p1.getStrength()) {
                return p2;
            } else {

                return ThreadLocalRandom.current().nextBoolean() ? p1 : p2;
            }
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
            TypeAdvantage typeAdvantage = typeService.getTypeAdvantage(type);
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

