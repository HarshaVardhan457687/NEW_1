import { Routes } from '@angular/router';
import { ProjectPageComponent } from './project-page.component';
import { OverviewComponent } from './overview/overview.component';
//import { MyTeamComponent } from './my-team/my-team.component';
import { UnderConstructionComponent } from '../../../pages/under-construction/under-construction.component';
import { ObjectivesPageComponent } from './objectives-page/objectives-page.component';
import { MyTasksComponent } from './my-tasks/my-tasks.component';
import { TeamsPageComponent } from './teams-page/teams-page.component';
import { TeamPageComponent } from './teams-page/team-page/team-page.component';

export const MANAGER_PROJECT_ROUTES: Routes = [
  {
    path: '',
    component: ProjectPageComponent,
    children: [
      { path: '', redirectTo: 'overview', pathMatch: 'full' },
      { path: 'overview', component: OverviewComponent },
      { path: 'objectives', component: ObjectivesPageComponent },
      { path: 'tasks', component: MyTasksComponent },
      { path: 'calendar', component: UnderConstructionComponent },
      //{ path: 'my-team', component: MyTeamComponent },
      { 
        path: 'teams',
        children: [
          { path: '', component: TeamsPageComponent },
          { path: ':id', component: TeamPageComponent }
        ]
      },
      { path: 'approvals', component: UnderConstructionComponent },
      { path: 'analytics', component: UnderConstructionComponent }
    ]
  }
]; 