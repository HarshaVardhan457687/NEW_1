import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectCardComponent } from '../../../shared/project-card/project-card.component';
import { NavbarComponent } from '../../../shared/navbar/navbar.component';
import { AddProjectCardComponent } from '../../../shared/add-project-card/add-project-card.component';
import { ProjectsPageService, ProjectData } from '../../../core/services/projects-page.service';
import { ProjectSelectionService } from '../../../core/services/project-selection.service';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, ProjectCardComponent, NavbarComponent, AddProjectCardComponent],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent implements OnInit {
  projects: ProjectData[] = [];
  showAddProjectModal = false;
  isLoading = true;

  constructor(
    private projectsPageService: ProjectsPageService,
    private projectSelectionService: ProjectSelectionService
  ) {}

  ngOnInit() {
    this.loadProjects();
  }

  private loadProjects() {
    if (!this.projectsPageService) {
      console.error('ProjectsPageService not available');
      this.isLoading = false;
      return;
    }

    this.projectsPageService.getProjects().subscribe({
      next: (projects) => {
        this.projects = projects;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading projects:', error);
        this.isLoading = false;
      }
    });
  }

  openAddProjectModal() {
    this.showAddProjectModal = true;
  }

  closeAddProjectModal() {
    this.showAddProjectModal = false;
    this.loadProjects();
  }
}
