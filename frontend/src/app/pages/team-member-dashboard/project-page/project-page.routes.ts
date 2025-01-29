import { Routes } from '@angular/router';
import { ProjectPageComponent } from './project-page.component';
import { OverviewComponent } from './overview/overview.component';
import { MyTeamComponent } from './my-team/my-team.component';
import { UnderConstructionComponent } from '../../../pages/under-construction/under-construction.component';

export const PROJECT_ROUTES: Routes = [
  {
    path: ':id',
    component: ProjectPageComponent,
    children: [
      {
        path: '',
        redirectTo: 'overview',
        pathMatch: 'full'
      },
      {
        path: 'overview',
        component: OverviewComponent
      },
      {
        path: 'objectives',
        component: UnderConstructionComponent
      },
      {
        path: 'tasks',
        component: UnderConstructionComponent
      },
      {
        path: 'calendar',
        component: UnderConstructionComponent
      },
      {
        path: 'my-team',
        component: MyTeamComponent
      }
      // Add other tab routes here
    ]
  }
]; 