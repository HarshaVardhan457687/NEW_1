import { Routes } from '@angular/router';
import { ProjectPageComponent } from './project-page.component';
import { OverviewComponent } from './overview/overview.component';

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
      // Add other tab routes here
    ]
  }
]; 