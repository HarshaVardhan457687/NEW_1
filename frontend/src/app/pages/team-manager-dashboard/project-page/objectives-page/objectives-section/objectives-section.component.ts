import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectivePageService, ObjectiveSummary } from '../../../../../core/services/objective-page.service';
import { ObjectivesDetailCardComponent } from '../objectives-detail-card/objectives-detail-card.component';

@Component({
  selector: 'app-objectives-section',
  standalone: true,
  imports: [CommonModule, ObjectivesDetailCardComponent],
  templateUrl: './objectives-section.component.html',
  styleUrls: ['./objectives-section.component.scss']
})
export class ObjectivesSectionComponent implements OnInit {
  @Input() projectId!: number;
  objectives: ObjectiveSummary[] = [];
  isLoading: boolean = true;

  constructor(private objectivePageService: ObjectivePageService) {}

  ngOnInit(): void {
    this.loadObjectives();
  }

  loadObjectives(): void {
    this.isLoading = true;
    this.objectivePageService.getObjectivesWithKeyResults(this.projectId).subscribe({
      next: (objectives) => {
        this.objectives = objectives;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading objectives:', error);
        this.isLoading = false;
      }
    });
  }

  transformObjective(objective: ObjectiveSummary) {
    return {
      id: objective.objectiveId,
      name: objective.objectiveName,
      status: objective.objectiveStatus,
      progress: objective.objectiveProgress
    };
  }
}
