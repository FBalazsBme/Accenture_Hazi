package com.example.pokemonbackend.controller;

import com.example.pokemonbackend.dto.BattleHistoryDTO;
import com.example.pokemonbackend.service.BattleHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class BattleHistoryController {

    private final BattleHistoryService battleHistoryService;

    @Autowired
    public BattleHistoryController(BattleHistoryService battleHistoryService) {
        this.battleHistoryService = battleHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<BattleHistoryDTO>> getBattleHistory(@RequestParam(defaultValue = "20") String limit) {
        int parsedLimit;


        try {
            parsedLimit = Integer.parseInt(limit);
            if (parsedLimit < 1) {
                throw new IllegalArgumentException("Limit must be greater than or equal to 1.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Limit must be a valid positive integer.");
        }


        List<BattleHistoryDTO> histories = battleHistoryService.getAllBattleHistories(parsedLimit);
        return ResponseEntity.ok(histories);
    }
}

