import { Routes } from '@angular/router';
import { TeamMemberDashboardComponent } from './pages/team-member-dashboard/team-member-dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: TeamMemberDashboardComponent },
  // Add more routes as needed
];
