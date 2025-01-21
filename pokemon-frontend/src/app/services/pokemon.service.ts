import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pokemon } from '../models/pokemon.model';

@Injectable({
  providedIn: 'root'
})
export class PokemonService {

  private apiUrl = 'http://localhost:8081/api/pokemons/random';

  constructor(private http: HttpClient) {}

  getRandomPokemons(): Observable<Pokemon[]> {
    return this.http.get<Pokemon[]>(this.apiUrl);
  }
}
