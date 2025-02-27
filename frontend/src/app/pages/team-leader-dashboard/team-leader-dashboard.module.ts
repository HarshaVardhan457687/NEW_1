import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeamLeaderDashboardComponent } from './team-leader-dashboard.component';
import { ProjectsComponent as TeamLeaderProjectsComponent } from './projects/projects.component';
import { LEADER_PROJECT_ROUTES } from './project-page/project-page.routes';

const routes: Routes = [
  { path: '', component: TeamLeaderDashboardComponent },
  { path: 'projects', component: TeamLeaderProjectsComponent },
  {
    path: 'projects/:id',
    children: LEADER_PROJECT_ROUTES
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TeamLeaderDashboardModule { } 