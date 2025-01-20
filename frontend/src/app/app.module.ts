import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';

import { AppComponent } from './app.component';
import { DashboardContainerComponent } from './containers/dashboard-container/dashboard-container.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { FeatureToggleDirective } from './directives/feature-toggle.directive';
import { FeatureToggleService } from './services/feature-toggle.service';
import { DashboardService } from './services/dashboard.service';

@NgModule({
  declarations: [
    AppComponent,
    DashboardContainerComponent,
    DashboardComponent,
    FeatureToggleDirective
  ],
  imports: [
    BrowserModule,
    CommonModule
  ],
  providers: [
    FeatureToggleService,
    DashboardService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { } 