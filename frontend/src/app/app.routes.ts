import { Routes } from '@angular/router';
import { RoleSelectionPageComponent } from './pages/role-selection-page/role-selection-page.component';
import { TeamMemberDashboardComponent } from './pages/team-member-dashboard/team-member-dashboard.component';
import { UnderConstructionComponent } from './pages/under-construction/under-construction.component';
import { ProjectsComponent as TeamMemberProjectsComponent } from './pages/team-member-dashboard/projects/projects.component';
import { PROJECT_ROUTES } from './pages/team-member-dashboard/project-page/project-page.routes';
import { LEADER_PROJECT_ROUTES } from './pages/team-leader-dashboard/project-page/project-page.routes';
import { MANAGER_PROJECT_ROUTES } from './pages/team-manager-dashboard/project-page/project-page.routes';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { TeamLeaderDashboardComponent } from './pages/team-leader-dashboard/team-leader-dashboard.component';
import { ProjectsComponent as TeamLeaderProjectsComponent } from './pages/team-leader-dashboard/projects/projects.component';
import { TeamManagerDashboardComponent } from './pages/team-manager-dashboard/team-manager-dashboard.component';
import { ProjectsComponent as TeamManagerProjectsComponent } from './pages/team-manager-dashboard/projects/projects.component';
import { ProfilePageComponent } from './pages/profile-page/profile-page.component';
import { ChangePasswordComponent } from './shared/change-password/change-password.component';


export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'role-selection', component: RoleSelectionPageComponent },
  {
    path: 'team-member-dashboard',
    children: [
      { path: '', component: TeamMemberDashboardComponent },
      { path: 'projects', component: TeamMemberProjectsComponent },
      {
        path: 'projects/:id',
        children: PROJECT_ROUTES
      }
    ]
  },
  {
    path: 'team-leader-dashboard',
    children: [
      { path: '', component: TeamLeaderDashboardComponent },
      { path: 'projects', component: TeamLeaderProjectsComponent },
      {
        path: 'projects/:id',
        children: LEADER_PROJECT_ROUTES
      }
    ]
  },
  {
    path: 'team-manager-dashboard',
    children: [
      { path: '', component: TeamManagerDashboardComponent },
      { path: 'projects', component: TeamManagerProjectsComponent },
      {
        path: 'projects/:id',
        children: MANAGER_PROJECT_ROUTES
      }
    ]
  },
  { path: 'profile', component: ProfilePageComponent },
  { path: 'change-password', component: ChangePasswordComponent },
  { path: 'construction', component: UnderConstructionComponent }
];
