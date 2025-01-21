package com.example.pokemonbackend.service;

import com.example.pokemonbackend.dto.BattleHistoryDTO;
import com.example.pokemonbackend.dto.PokemonDTO;
import com.example.pokemonbackend.model.BattleHistory;
import com.example.pokemonbackend.repository.BattleHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BattleHistoryService {

    private final BattleHistoryRepository battleHistoryRepository;
    private final PokemonService pokemonService;

    @Autowired
    public BattleHistoryService(BattleHistoryRepository battleHistoryRepository, PokemonService pokemonService) {
        this.battleHistoryRepository = battleHistoryRepository;
        this.pokemonService = pokemonService;
    }

    public List<BattleHistoryDTO> getAllBattleHistories(int limit) {
        List<BattleHistory> histories =
                battleHistoryRepository.findTopHistories(PageRequest.of(0, limit,
                        Sort.by(Sort.Direction.DESC, "battleTime")));
        return histories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private BattleHistoryDTO toDTO(BattleHistory history) {

        PokemonDTO p1Details = fetchPokemonDetails(history.getPokemon1Name(), history.getPokemon1Strength());
        PokemonDTO p2Details = fetchPokemonDetails(history.getPokemon2Name(), history.getPokemon2Strength());

        LocalDateTime battleDate = history.getBattleTime();

        return new BattleHistoryDTO(
                p1Details,
                p2Details,
                history.getWinnerName(),
                battleDate
        );
    }

    private PokemonDTO fetchPokemonDetails(String name, int strength) {

        PokemonDTO dto;
        try {
            dto = PokemonDTO.fromPokemon(pokemonService.getPokemonByName(name));
            dto.setStrength(strength);
        } catch (Exception e) {
            // Handle exceptions, possibly set default values
            dto = new PokemonDTO();
            dto.setName(name);
            dto.setType("unknown");
            dto.setImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"); // Placeholder image
            dto.setStrength(strength);
        }
        return dto;
    }
}

