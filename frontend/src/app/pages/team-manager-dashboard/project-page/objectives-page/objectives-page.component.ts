import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveStatCardComponent } from '../../../../shared/objective-stat-card/objective-stat-card.component';
import { ActivatedRoute } from '@angular/router';
import { ObjectivesService, ObjectiveStats } from '../../../../core/services/objectives.service';
import { ObjectivesSectionComponent } from './objectives-section/objectives-section.component';

@Component({
  selector: 'app-objectives-page',
  standalone: true,
  imports: [CommonModule, ObjectiveStatCardComponent, ObjectivesSectionComponent],
  templateUrl: './objectives-page.component.html',
  styleUrls: ['./objectives-page.component.scss']
})
export class ObjectivesPageComponent implements OnInit {
  projectId!: number;
  objectiveStats: ObjectiveStats = {
    totalObjectives: 0,
    inProgress: 0,
    completed: 0,
    completedKeys: 0
  };

  constructor(
    private route: ActivatedRoute,
    private objectivesService: ObjectivesService
  ) {}

  ngOnInit(): void {
    this.route.parent?.params.subscribe(params => {
      this.projectId = +params['id'];
      this.loadObjectiveStats();
    });
  }

  private loadObjectiveStats(): void {
    this.objectivesService.getObjectiveStats(this.projectId).subscribe({
      next: (stats: ObjectiveStats) => {
        this.objectiveStats = stats;
      },
      error: (error: Error) => {
        console.error('Error loading objective stats:', error);
      }
    });
  }

  onAddObjective(){}
}
