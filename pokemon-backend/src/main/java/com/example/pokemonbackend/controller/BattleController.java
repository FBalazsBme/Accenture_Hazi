package com.example.pokemonbackend.controller;

import com.example.pokemonbackend.dto.BattleResultDTO;
import com.example.pokemonbackend.service.BattleService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/battle")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;

    @PostMapping("/simulate")
    public ResponseEntity<BattleResultDTO> simulateBattle(
            @RequestParam @NotBlank String pokemon1,
            @RequestParam @NotBlank String pokemon2,
            @RequestParam @Min(0) @Max(20) int strength1,
            @RequestParam @Min(0) @Max(20) int strength2) {
        BattleResultDTO result = battleService.simulateBattle(pokemon1, pokemon2, strength1, strength2);
        return ResponseEntity.ok(result);
    }
}

