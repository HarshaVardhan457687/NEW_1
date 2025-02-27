import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeamManagerDashboardComponent } from './team-manager-dashboard.component';
import { ProjectsComponent as TeamManagerProjectsComponent } from './projects/projects.component';
import { MANAGER_PROJECT_ROUTES } from './project-page/project-page.routes';

const routes: Routes = [
  { path: '', component: TeamManagerDashboardComponent },
  { path: 'projects', component: TeamManagerProjectsComponent },
  {
    path: 'projects/:id',
    children: MANAGER_PROJECT_ROUTES
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TeamManagerDashboardModule { } 