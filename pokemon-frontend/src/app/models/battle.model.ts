import { Pokemon } from './pokemon.model';

export interface BattleResult {
  pokemon1: Pokemon;
  pokemon2: Pokemon;
  winner: Pokemon;
  date: Date;
}
