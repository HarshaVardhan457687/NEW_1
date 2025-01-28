import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectTitleCardComponent } from './project-title-card/project-title-card.component';
import { ProjectWithManager } from '../../../../core/services/projects.service';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../../../core/services/projects.service';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [CommonModule, ProjectTitleCardComponent],
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {
  project?: ProjectWithManager;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService
  ) {}

  ngOnInit() {
    // Get the project ID from the parent route
    const projectId = Number(this.route.parent?.snapshot.paramMap.get('id'));
    if (!isNaN(projectId)) {
      this.projectService.getProjectById(projectId).subscribe({
        next: (project) => {
          this.project = project;
        },
        error: (err) => {
          console.error('Error loading project in overview:', err);
        }
      });
    }
  }
} 