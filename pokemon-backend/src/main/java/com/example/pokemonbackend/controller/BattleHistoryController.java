package com.example.pokemonbackend.controller;

import com.example.pokemonbackend.dto.BattleHistoryDTO;
import com.example.pokemonbackend.service.BattleHistoryService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class BattleHistoryController {

    private final BattleHistoryService battleHistoryService;

    @GetMapping
    public ResponseEntity<List<BattleHistoryDTO>> getBattleHistory(@RequestParam(defaultValue = "20") @Min(1) int limit) {

        List<BattleHistoryDTO> histories = battleHistoryService.getAllBattleHistories(limit);
        return ResponseEntity.ok(histories);
    }
}

