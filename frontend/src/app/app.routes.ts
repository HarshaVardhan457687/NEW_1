import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RoleSelectionPageComponent } from './pages/role-selection-page/role-selection-page.component';
import { ProfilePageComponent } from './pages/profile-page/profile-page.component';
import { ChangePasswordComponent } from './shared/change-password/change-password.component';
import { UnderConstructionComponent } from './pages/under-construction/under-construction.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'role-selection', component: RoleSelectionPageComponent, canActivate: [AuthGuard] },
  {
    path: 'team-member-dashboard',
    canActivate: [AuthGuard],
    loadChildren: () => import('./pages/team-member-dashboard/team-member-dashboard.module')
      .then(m => m.TeamMemberDashboardModule)
  },
  {
    path: 'team-leader-dashboard',
    canActivate: [AuthGuard],
    loadChildren: () => import('./pages/team-leader-dashboard/team-leader-dashboard.module')
      .then(m => m.TeamLeaderDashboardModule)
  },
  {
    path: 'team-manager-dashboard',
    canActivate: [AuthGuard],
    loadChildren: () => import('./pages/team-manager-dashboard/team-manager-dashboard.module')
      .then(m => m.TeamManagerDashboardModule)
  },
  { path: 'profile', component: ProfilePageComponent },
  { path: 'change-password', component: ChangePasswordComponent },
  { path: 'construction', component: UnderConstructionComponent }
];
