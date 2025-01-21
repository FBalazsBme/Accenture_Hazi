

import { Component, OnInit, ViewChild } from '@angular/core';
import { BattleService } from '../../services/battle.service';
import { BattleResult } from '../../models/battle.model';
import { FormControl } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-battle-history',
  templateUrl: './battle-history.component.html',
  styleUrls: ['./battle-history.component.scss']
})
export class BattleHistoryComponent implements OnInit {
  battles: BattleResult[] = [];
  dataSource = new MatTableDataSource<BattleResult>();
  searchControl = new FormControl('');


  displayedColumns: string[] = ['pokemon1', 'pokemon2', 'winner', 'date'];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private battleService: BattleService, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.fetchBattleHistory();

    this.searchControl.valueChanges.pipe(
      debounceTime(300)
    ).subscribe(searchTerm => {
      this.filterBattles(searchTerm!!);
    });
  }

  fetchBattleHistory() {
    this.battleService.getBattleHistory().subscribe({
      next: (data) => {
        this.battles = data;
        this.dataSource.data = this.battles;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        console.error('Error fetching battle history:', error);
        this.snackBar.open('Failed to load battle history. Please try again later.', 'Close', {
          duration: 3000,
        });
      }
    });
  }

  filterBattles(searchTerm: string) {
    if (!searchTerm) {
      this.dataSource.data = this.battles;
      return;
    }

    const lowerTerm = searchTerm.toLowerCase();
    const filtered = this.battles.filter(battle =>
      battle.pokemon1.name.toLowerCase().includes(lowerTerm) ||
      battle.pokemon2.name.toLowerCase().includes(lowerTerm)
    );

    this.dataSource.data = filtered;


    if (filtered.length === 0) {
      this.snackBar.open('No battles match your search criteria.', 'Close', {
        duration: 2000,
      });
    }
  }
}
