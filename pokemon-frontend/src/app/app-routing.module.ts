import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {BattleComponent} from "./components/battle/battle.component";
import {BattleHistoryComponent} from "./components/battle-history/battle-history.component";

const routes: Routes = [
  { path: '', redirectTo: '/battle', pathMatch: 'full' }, // **Default Route**
  { path: 'battle', component: BattleComponent }, // **Battle Route**
  { path: 'history', component: BattleHistoryComponent }, // **History Route**
  { path: '**', redirectTo: '/battle' } // **Wildcard Route**
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
