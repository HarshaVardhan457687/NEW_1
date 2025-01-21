import { Routes } from '@angular/router';
import { TeamMemberDashboardComponent } from './pages/team-member-dashboard/team-member-dashboard.component';
import { UnderConstructionComponent } from './pages/under-construction/under-construction.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    component: TeamMemberDashboardComponent
  },
  {
    path: 'under-construction',
    component: UnderConstructionComponent
  }
];
