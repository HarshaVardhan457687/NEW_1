import { Routes } from '@angular/router';
import { RoleSelectionPageComponent } from './pages/role-selection-page/role-selection-page.component';
import { TeamMemberDashboardComponent } from './pages/team-member-dashboard/team-member-dashboard.component';
import { UnderConstructionComponent } from './pages/under-construction/under-construction.component';

export const routes: Routes = [
  { path: '', redirectTo: '/role-selection', pathMatch: 'full' },
  { path: 'role-selection', component: RoleSelectionPageComponent },
  { path: 'team-member-dashboard', component: TeamMemberDashboardComponent },
  { path: 'construction', component: UnderConstructionComponent }
];
