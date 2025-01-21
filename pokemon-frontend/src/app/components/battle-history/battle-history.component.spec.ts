import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BattleHistoryComponent } from './battle-history.component';

describe('BattleHistoryComponent', () => {
  let component: BattleHistoryComponent;
  let fixture: ComponentFixture<BattleHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BattleHistoryComponent]
    });
    fixture = TestBed.createComponent(BattleHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
