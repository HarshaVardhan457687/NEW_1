import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActiveProjectsCardComponent } from '../active-projects-card/active-projects-card.component';
import { ProjectService } from '../../../core/services/projects.service';

@Component({
  selector: 'app-active-project-section',
  standalone: true,
  imports: [CommonModule, ActiveProjectsCardComponent],
  templateUrl: './active-project-section.component.html',
  styleUrl: './active-project-section.component.scss'
})
export class ActiveProjectSectionComponent implements OnInit {
  projects: any[] = [];

  constructor(private projectService: ProjectService) {}

  ngOnInit() {
    this.projectService.getActiveProjects().subscribe(
      projects => this.projects = projects
    );
  }
}
