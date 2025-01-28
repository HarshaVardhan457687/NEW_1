import { Routes } from '@angular/router';
import { RoleSelectionPageComponent } from './pages/role-selection-page/role-selection-page.component';
import { TeamMemberDashboardComponent } from './pages/team-member-dashboard/team-member-dashboard.component';
import { UnderConstructionComponent } from './pages/under-construction/under-construction.component';
import { ProjectsComponent } from './pages/team-member-dashboard/projects/projects.component';
import { ProjectPageComponent } from './pages/team-member-dashboard/project-page/project-page.component';
import { PROJECT_ROUTES } from './pages/team-member-dashboard/project-page/project-page.routes';

export const routes: Routes = [
  { path: '', redirectTo: '/role-selection', pathMatch: 'full' },
  { path: 'role-selection', component: RoleSelectionPageComponent },
  { path: 'team-member-dashboard', component: TeamMemberDashboardComponent },
  {
    path: 'projects',
    children: [
      { path: '', component: ProjectsComponent },
      ...PROJECT_ROUTES
    ]
  },
  { path: 'construction', component: UnderConstructionComponent }
];
