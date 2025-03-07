import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveStatCardComponent } from '../../../../shared/objective-stat-card/objective-stat-card.component';
import { ActivatedRoute } from '@angular/router';
import { ObjectivesService, ObjectiveStats } from '../../../../core/services/objectives.service';
import { ObjectivesSectionComponent } from './objectives-section/objectives-section.component';
import { AddObjectiveCardComponent } from '../../../../shared/add-objective-card/add-objective-card.component';
import { ObjectiveFormData } from '../../../../shared/add-objective-card/add-objective-card.component';

@Component({
  selector: 'app-objectives-page',
  standalone: true,
  imports: [CommonModule, ObjectiveStatCardComponent, ObjectivesSectionComponent, AddObjectiveCardComponent],
  templateUrl: './objectives-page.component.html',
  styleUrls: ['./objectives-page.component.scss']
})
export class ObjectivesPageComponent implements OnInit {
  projectId!: number;
  showAddObjective = false;
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

  onAddObjective(): void {
    this.showAddObjective = true;
  }

  onCloseObjective(): void {
    this.showAddObjective = false;
  }

  onSaveObjective(data: ObjectiveFormData): void {
    // TODO: Implement save objective logic
    console.log('Saving objective:', data);
    this.showAddObjective = false;
    // After saving, reload objectives
    this.loadObjectiveStats();
  }
}