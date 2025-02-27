import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeamMemberDashboardComponent } from './team-member-dashboard.component';
import { ProjectsComponent as TeamMemberProjectsComponent } from './projects/projects.component';
import { PROJECT_ROUTES } from './project-page/project-page.routes';

const routes: Routes = [
  { path: '', component: TeamMemberDashboardComponent },
  { path: 'projects', component: TeamMemberProjectsComponent },
  {
    path: 'projects/:id',
    children: PROJECT_ROUTES
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TeamMemberDashboardModule { } 