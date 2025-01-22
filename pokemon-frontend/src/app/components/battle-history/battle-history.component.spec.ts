// src/app/components/battle-history/battle-history.component.spec.ts

import { ComponentFixture, TestBed, fakeAsync, tick, flush } from '@angular/core/testing';
import { BattleHistoryComponent } from './battle-history.component';
import { BattleService } from '../../services/battle.service';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { of, throwError } from 'rxjs';
import { BattleResult } from '../../models/battle.model';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {FormControl, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule, NoopAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule, By} from '@angular/platform-browser';
import {HttpClientModule} from "@angular/common/http";
import {AppRoutingModule} from "../../app-routing.module";
import {MatIconModule} from "@angular/material/icon";
import {MatCardModule} from "@angular/material/card";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";

describe('BattleHistoryComponent', () => {
  let component: BattleHistoryComponent;
  let fixture: ComponentFixture<BattleHistoryComponent>;
  let battleService: jasmine.SpyObj<BattleService>;
  let snackBar: jasmine.SpyObj<MatSnackBar>;

  const mockBattleHistory: BattleResult[] = [
    {
      pokemon1: { name: 'pikachu', type: 'electric', image: 'https://...', strength: 55 },
      pokemon2: { name: 'bulbasaur', type: 'grass', image: 'https://...', strength: 49 },
      winner: { name: 'pikachu', type: 'electric', image: 'https://...', strength: 55 },
      date: new Date('2025-01-17T12:34:56.789Z')
    },
    {
      pokemon1: { name: 'charmander', type: 'fire', image: 'https://...', strength: 52 },
      pokemon2: { name: 'squirtle', type: 'water', image: 'https://...', strength: 48 },
      winner: { name: 'squirtle', type: 'water', image: 'https://...', strength: 48 },
      date: new Date('2025-01-16T10:20:30.456Z')
    },
  ];

  beforeEach(async () => {
    const battleServiceSpy = jasmine.createSpyObj('BattleService', ['getBattleHistory']);
    const snackBarSpy = jasmine.createSpyObj('MatSnackBar', ['open']);

    await TestBed.configureTestingModule({
      declarations: [ BattleHistoryComponent ],
      imports: [
        MatSortModule,
        FormsModule,
        NoopAnimationsModule,
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        MatTableModule,
        MatCardModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatToolbarModule,
        HttpClientModule,
        MatProgressSpinnerModule,
        MatPaginatorModule
      ],
      providers: [
        { provide: BattleService, useValue: battleServiceSpy },
        { provide: MatSnackBar, useValue: snackBarSpy }
      ]
    })
      .compileComponents();

    battleService = TestBed.inject(BattleService) as jasmine.SpyObj<BattleService>;
    snackBar = TestBed.inject(MatSnackBar) as jasmine.SpyObj<MatSnackBar>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BattleHistoryComponent);
    component = fixture.componentInstance;
    component.searchControl = new FormControl('');
  });

  it('should create the BattleHistoryComponent', () => {
    battleService.getBattleHistory.and.returnValue(of([]));
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should fetch battle history on initialization', () => {
    battleService.getBattleHistory.and.returnValue(of(mockBattleHistory));
    fixture.detectChanges();
    expect(battleService.getBattleHistory).toHaveBeenCalledTimes(1);
    expect(component.battles).toEqual(mockBattleHistory);
    expect(component.dataSource.data).toEqual(mockBattleHistory);
  });

  it('should display battle data in the table', fakeAsync(() => {
    battleService.getBattleHistory.and.returnValue(of(mockBattleHistory));
    fixture.detectChanges();
    tick(); // Simulate async

    fixture.detectChanges(); // Update the view

    const tableRows = fixture.debugElement.queryAll(By.css('table mat-row'));
    expect(tableRows.length).toBe(mockBattleHistory.length);

    // Check first row data
    const firstRowCells = tableRows[0].queryAll(By.css('mat-cell'));
    expect(firstRowCells[0].nativeElement.textContent).toContain('Pikachu (electric)');
    expect(firstRowCells[1].nativeElement.textContent).toContain('Bulbasaur (grass)');
    expect(firstRowCells[2].nativeElement.textContent).toContain('Pikachu');
    expect(firstRowCells[3].nativeElement.textContent).toContain('1/17/25'); // Depending on locale
  }));

  it('should handle API errors gracefully', fakeAsync(() => {
    const errorResponse = { message: 'Internal Server Error' };
    battleService.getBattleHistory.and.returnValue(throwError(() => errorResponse));
    fixture.detectChanges();
    tick(); // Simulate async

    fixture.detectChanges(); // Update the view

    expect(battleService.getBattleHistory).toHaveBeenCalledTimes(1);
    expect(component.battles).toEqual([]);
    expect(component.dataSource.data).toEqual([]);

    expect(snackBar.open).toHaveBeenCalledWith('Failed to load battle history. Please try again later.', 'Close', {
      duration: 3000,
    });

    // Check that no table rows are rendered
    const tableRows = fixture.debugElement.queryAll(By.css('table mat-row'));
    expect(tableRows.length).toBe(0);
  }));

  it('should filter battles based on search input', fakeAsync(() => {
    battleService.getBattleHistory.and.returnValue(of(mockBattleHistory));
    fixture.detectChanges();
    tick(); // Simulate async

    fixture.detectChanges(); // Update the view

    const input = fixture.debugElement.query(By.css('input')).nativeElement;
    input.value = 'pikachu';
    input.dispatchEvent(new Event('input'));

    tick(300); // Simulate debounce time
    fixture.detectChanges();

    expect(component.dataSource.data.length).toBe(1);
    expect(component.dataSource.data[0].pokemon1.name).toBe('pikachu');

    const tableRows = fixture.debugElement.queryAll(By.css('table mat-row'));
    expect(tableRows.length).toBe(1);
    expect(tableRows[0].query(By.css('mat-cell')).nativeElement.textContent).toContain('Pikachu (electric)');
  }));

  it('should display "No battles found" message when search yields no results', fakeAsync(() => {
    battleService.getBattleHistory.and.returnValue(of(mockBattleHistory));
    fixture.detectChanges();
    tick(); // Simulate async

    fixture.detectChanges(); // Update the view

    const input = fixture.debugElement.query(By.css('input')).nativeElement;
    input.value = 'mewtwo';
    input.dispatchEvent(new Event('input'));

    tick(300); // Simulate debounce time
    fixture.detectChanges();

    expect(component.dataSource.data.length).toBe(0);

    const noResults = fixture.debugElement.query(By.css('.no-results'));
    expect(noResults).toBeTruthy();
    expect(noResults.nativeElement.textContent).toContain('No battles found matching your search.');
  }));

  it('should reset the table when search input is cleared', fakeAsync(() => {
    battleService.getBattleHistory.and.returnValue(of(mockBattleHistory));
    fixture.detectChanges();
    tick(); // Simulate async

    fixture.detectChanges(); // Update the view

    const input = fixture.debugElement.query(By.css('input')).nativeElement;
    input.value = 'charmander';
    input.dispatchEvent(new Event('input'));

    tick(300); // Simulate debounce time
    fixture.detectChanges();

    expect(component.dataSource.data.length).toBe(1);
    let tableRows = fixture.debugElement.queryAll(By.css('table mat-row'));
    expect(tableRows.length).toBe(1);
    expect(tableRows[0].query(By.css('mat-cell')).nativeElement.textContent).toContain('Charmander (fire)');

    input.value = '';
    input.dispatchEvent(new Event('input'));

    tick(300); // Simulate debounce time
    fixture.detectChanges();

    expect(component.dataSource.data.length).toBe(mockBattleHistory.length);

    tableRows = fixture.debugElement.queryAll(By.css('table mat-row'));
    expect(tableRows.length).toBe(mockBattleHistory.length);
  }));

  it('should initialize paginator and sort', fakeAsync(() => {
    battleService.getBattleHistory.and.returnValue(of(mockBattleHistory));
    fixture.detectChanges();
    tick(); // Simulate async

    fixture.detectChanges(); // Update the view

    const paginator = component.paginator as MatPaginator;
    const sort = component.sort as MatSort;

    expect(paginator).toBeTruthy();
    expect(sort).toBeTruthy();

    expect(component.dataSource.paginator).toBe(paginator);
    expect(component.dataSource.sort).toBe(sort);
  }));
});
