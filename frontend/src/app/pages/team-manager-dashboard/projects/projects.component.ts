import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectCardComponent } from '../../../shared/project-card/project-card.component';
import { NavbarComponent } from '../../../shared/navbar/navbar.component';
import { ProjectService, ProjectWithManager } from '../../../core/services/projects.service';
import { AddProjectCardComponent } from '../../../shared/add-project-card/add-project-card.component';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, ProjectCardComponent, NavbarComponent, AddProjectCardComponent],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent implements OnInit {
  projects: ProjectWithManager[] = [];
  showAddProjectModal = false;

  constructor(private projectService: ProjectService) {}

  ngOnInit() {
    this.projectService.getProjects().subscribe(
      projects => this.projects = projects
    );
  }

  openAddProjectModal() {
    this.showAddProjectModal = true;
  }

  closeAddProjectModal() {
    this.showAddProjectModal = false;
  }
}
