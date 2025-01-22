import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';

import { BattleResult } from '../models/battle.model';

@Injectable({
  providedIn: 'root'
})
export class BattleService {
  private apiUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  simulateBattle(pokemon1Name: string,
                 pokemon2Name: string,
                 strength1: number,
                 strength2: number): Observable<BattleResult> {
    const params = new HttpParams()
      .set('pokemon1', pokemon1Name)
      .set('pokemon2', pokemon2Name)
      .set('strength1', strength1.toString())
      .set('strength2', strength2.toString());

    return this.http.post<BattleResult>(`${this.apiUrl}/battle/simulate`, {}, { params });
  }

  getBattleHistory(): Observable<BattleResult[]> {
    return this.http.get<BattleResult[]>(`${this.apiUrl}/history`);
  }
}
