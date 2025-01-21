package com.example.pokemonbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BattleHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pokemon1Name;
    private int pokemon1Strength;

    private String pokemon2Name;
    private int pokemon2Strength;

    private String winnerName;

    private LocalDateTime battleTime;
}

