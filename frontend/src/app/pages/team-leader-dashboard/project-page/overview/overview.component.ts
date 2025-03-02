import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectTitleCardComponent } from './project-title-card/project-title-card.component';
import { ActivatedRoute } from '@angular/router';
import { ProjectOverviewService, Project } from '../../../../core/services/project-overview.service';
import { ObjectiveStatCardComponent, ObjectiveStatus } from '../../../../shared/objective-stat-card/objective-stat-card.component';
import { TasksOverviewCardComponent } from './tasks-overview-card/tasks-overview-card.component';
import { TimelineCardComponent } from './timeline-card/timeline-card.component';
import { ProjectTeamCardComponent } from './project-team-card/project-team-card.component';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [
    CommonModule, 
    ProjectTitleCardComponent, 
    ObjectiveStatCardComponent,
    TasksOverviewCardComponent,
    TimelineCardComponent,
    ProjectTeamCardComponent
  ],
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {
  project?: Project;
  isLoading = true;
  objectiveStats: ObjectiveStatus = {
    total: 0,
    completed: 0,
    inProgress: 0,
    notStarted: 0
  };

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectOverviewService
  ) {}

  ngOnInit() {
    const projectId = Number(this.route.parent?.snapshot.paramMap.get('id'));
    if (!isNaN(projectId)) {
      forkJoin({
        project: this.projectService.getProjectById(projectId),
        stats: this.projectService.getObjectiveStats(projectId)
      }).subscribe({
        next: ({project, stats}) => {
          this.project = project;
          this.objectiveStats = {
            total: project.objectivesCount,
            completed: stats.completed,
            inProgress: stats.inProgress,
            notStarted: stats.notStarted
          };
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error loading project in overview:', err);
          this.isLoading = false;
        }
      });
    }
  }
}