import { Component } from '@angular/core';
import { BattleService } from '../../services/battle.service';
import { BattleResult } from '../../models/battle.model';
import {Pokemon} from "../../models/pokemon.model";
import {PokemonService} from "../../services/pokemon.service";

@Component({
  selector: 'app-battle',
  templateUrl: './battle.component.html',
  styleUrls: ['./battle.component.scss']
})
export class BattleComponent {
  isLoading = false;
  battleResult?: BattleResult;
  errorMessage = '';

  pokemon1?: Pokemon;
  pokemon2?: Pokemon;

  constructor(
    private battleService: BattleService,
    private pokemonService: PokemonService
  ) {}

  initiateBattle() {
    this.isLoading = true;
    this.errorMessage = '';
    this.battleResult = undefined;

    this.pokemonService.getRandomPokemons().subscribe({
      next: (pokemons) => {
        if (pokemons.length !== 2) {
          this.errorMessage = 'Unexpected number of Pokémon received.';
          this.isLoading = false;
          return;
        }

        this.pokemon1 = pokemons[0];
        this.pokemon2 = pokemons[1];

        this.battleService.simulateBattle(this.pokemon1.name, this.pokemon2.name,
          this.pokemon1.strength, this.pokemon2.strength).subscribe({
          next: (result) => {
            this.battleResult = result;
            this.isLoading = false;
          },
          error: (error) => {
            this.errorMessage = 'Failed to simulate battle. Please try again.';
            console.error(error);
            this.isLoading = false;
          }
        });
      },
      error: (error) => {
        this.errorMessage = 'Failed to fetch Pokémon. Please try again.';
        console.error(error);
        this.isLoading = false;
      }
    });
  }
}
