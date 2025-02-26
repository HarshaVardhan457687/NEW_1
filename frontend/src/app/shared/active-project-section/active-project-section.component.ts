import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActiveProjectsCardComponent } from '../active-projects-card/active-projects-card.component';
import { ActiveProject } from '../../core/services/active-project.service';
import { UserRole } from '../../core/services/role-selection.service';
import { DashboardPageService } from '../../core/services/dashboard.page.service';

@Component({
  selector: 'app-active-project-section',
  standalone: true,
  imports: [CommonModule, ActiveProjectsCardComponent],
  templateUrl: './active-project-section.component.html',
  styleUrl: './active-project-section.component.scss'
})
export class ActiveProjectSectionComponent implements OnInit {
  @Input() role!: UserRole;
  projects: ActiveProject[] = [];
  loading = true;
  error = false;

  constructor(private dashboardService: DashboardPageService) {}

  ngOnInit() {
    this.dashboardService.getActiveProjects(this.role)
      .subscribe({
        next: (projects) => {
          this.projects = projects;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading active projects:', error);
          this.projects = [];
          this.loading = false;
          this.error = true;
        },
        complete: () => {
          this.loading = false;
        }
      });
  }
}
