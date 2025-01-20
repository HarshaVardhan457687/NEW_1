import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';

import { DashboardContainerComponent } from './containers/dashboard-container/dashboard-container.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { MetricCardComponent } from './components/metric-card/metric-card.component';
import { FeatureToggleDirective } from './directives/feature-toggle.directive';

@NgModule({
  declarations: [
    DashboardContainerComponent,
    DashboardComponent,
    MetricCardComponent,
    FeatureToggleDirective
  ],
  imports: [
    BrowserModule,
    CommonModule
  ],
  providers: [],
  bootstrap: [DashboardContainerComponent]
})
export class AppModule { } 