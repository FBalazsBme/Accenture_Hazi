package com.example.pokemonbackend.controller;

import com.example.pokemonbackend.dto.BattleResultDTO;
import com.example.pokemonbackend.service.BattleService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/battle")
public class BattleController {

    private final BattleService battleService;

    @Autowired
    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @PostMapping("/simulate")
    public ResponseEntity<BattleResultDTO> simulateBattle(
            @RequestParam @NotBlank String pokemon1,
            @RequestParam @NotBlank String pokemon2) {
        BattleResultDTO result = battleService.simulateBattle(pokemon1, pokemon2);
        return ResponseEntity.ok(result);
    }
}

