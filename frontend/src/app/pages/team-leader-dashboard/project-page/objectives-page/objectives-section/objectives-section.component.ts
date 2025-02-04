import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Objective, KeyResult, ObjectivesService } from '../../../../../core/services/objectives.service';
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
  objectives: Objective[] = [];
  keyResults: KeyResult[] = [];

  constructor(private objectivesService: ObjectivesService) {}

  ngOnInit(): void {
    this.loadObjectives();
  }

  private loadObjectives(): void {
    this.objectivesService.getProjectObjectives(this.projectId).subscribe({
      next: (objectives) => {
        this.objectives = objectives;
        this.loadKeyResults();
      },
      error: (error) => {
        console.error('Error loading objectives:', error);
      }
    });
  }

  private loadKeyResults(): void {
    this.objectivesService.getKeyResults().subscribe({
      next: (keyResults) => {
        this.keyResults = keyResults;
      },
      error: (error) => {
        console.error('Error loading key results:', error);
      }
    });
  }

  getKeyResultsForObjective(objective: Objective): KeyResult[] {
    return this.keyResults.filter(kr => objective.keyResults.includes(kr.id));
  }
}
