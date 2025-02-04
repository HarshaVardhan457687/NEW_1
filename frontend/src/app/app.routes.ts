import { Routes } from '@angular/router';
import { RoleSelectionPageComponent } from './pages/role-selection-page/role-selection-page.component';
import { TeamMemberDashboardComponent } from './pages/team-member-dashboard/team-member-dashboard.component';
import { UnderConstructionComponent } from './pages/under-construction/under-construction.component';
import { ProjectsComponent } from './pages/team-member-dashboard/projects/projects.component';
import { ProjectPageComponent } from './pages/team-member-dashboard/project-page/project-page.component';
import { PROJECT_ROUTES } from './pages/team-member-dashboard/project-page/project-page.routes';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { TeamLeaderDashboardComponent } from './pages/team-leader-dashboard/team-leader-dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'role-selection', component: RoleSelectionPageComponent },
  { path: 'team-member-dashboard', component: TeamMemberDashboardComponent },
  { path: 'team-leader-dashboard', component: TeamLeaderDashboardComponent },
  {
    path: 'projects',
    children: [
      { path: '', component: ProjectsComponent },
      ...PROJECT_ROUTES
    ]
  },
  { path: 'construction', component: UnderConstructionComponent }
];
