package com.example.pokemonbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeAdvantage {
    private String typeName;
    private List<String> doubleDamageTo;
    private List<String> halfDamageTo;
    private List<String> noDamageTo;
}
