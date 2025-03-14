import { Routes } from '@angular/router';
import { ProjectPageComponent } from './project-page.component';
import { OverviewComponent } from './overview/overview.component';
import { MyTeamComponent } from './my-team/my-team.component';
import { UnderConstructionComponent } from '../../../pages/under-construction/under-construction.component';
import { ObjectivesPageComponent } from './objectives-page/objectives-page.component';
import { ApprovalsPageComponent } from './approvals-page/approvals-page.component';
import { MyTasksComponent } from './my-tasks/my-tasks.component';
import { CalenderComponent } from '../../../shared/calender/calender.component';

export const LEADER_PROJECT_ROUTES: Routes = [
  {
    path: '',
    component: ProjectPageComponent,
    children: [
      { path: '', redirectTo: 'overview', pathMatch: 'full' },
      { path: 'overview', component: OverviewComponent },
      { path: 'objectives', component: ObjectivesPageComponent },
      { path: 'tasks', component: MyTasksComponent },
      { path: 'calendar', component: CalenderComponent },
      { path: 'my-team', component: MyTeamComponent },
      { path: 'analytics', component: UnderConstructionComponent },
      { path: 'approvals', component: ApprovalsPageComponent }
    ]
  }
]; 