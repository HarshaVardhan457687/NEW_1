import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectTitleCardComponent } from './project-title-card/project-title-card.component';
import { ProjectWithManager } from '../../../../core/services/projects.service';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../../../core/services/projects.service';
import { ObjectiveStatCardComponent, ObjectiveStatus } from './objective-stat-card/objective-stat-card.component';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [CommonModule, ProjectTitleCardComponent, ObjectiveStatCardComponent],
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {
  project?: ProjectWithManager;
  objectiveStats: ObjectiveStatus = {
    total: 0,
    completed: 0,
    inProgress: 0,
    notStarted: 0
  };

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
          if (project.objectives) {
            // Calculate objective stats based on project progress
            const totalObjectives = project.objectives;
            const completedPercentage = project.progress / 100;
            
            this.objectiveStats = {
              total: totalObjectives,
              completed: Math.floor(totalObjectives * completedPercentage),
              inProgress: Math.floor(totalObjectives * 0.3),
              notStarted: Math.max(0, totalObjectives - Math.floor(totalObjectives * completedPercentage) - Math.floor(totalObjectives * 0.3))
            };
          }
        },
        error: (err) => {
          console.error('Error loading project in overview:', err);
        }
      });
    }
  }
} 