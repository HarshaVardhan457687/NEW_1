import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectCardComponent } from '../../../shared/project-card/project-card.component';
import { NavbarComponent } from '../../../shared/navbar/navbar.component';
import { ProjectsPageService, ProjectData } from '../../../core/services/projects-page.service';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, ProjectCardComponent, NavbarComponent],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent implements OnInit {
  projects: ProjectData[] = [];
  isLoading = true;

  constructor(private projectsPageService: ProjectsPageService) {}

  ngOnInit() {
    this.projectsPageService.getProjects().subscribe({
      next: (projects) => {
        this.projects = projects;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }
}
