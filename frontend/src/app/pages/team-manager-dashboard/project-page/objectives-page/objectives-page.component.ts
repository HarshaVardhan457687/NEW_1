import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveStatCardComponent } from '../../../../shared/objective-stat-card/objective-stat-card.component';
import { ActivatedRoute } from '@angular/router';
import { ObjectivePageService, ObjectivePageStats } from '../../../../core/services/objective-page.service';
import { ObjectivesSectionComponent } from './objectives-section/objectives-section.component';
import { AddObjectiveCardComponent, ObjectiveFormData } from '../../../../shared/add-objective-card/add-objective-card.component';

@Component({
  selector: 'app-objectives-page',
  standalone: true,
  imports: [CommonModule, ObjectiveStatCardComponent, ObjectivesSectionComponent, AddObjectiveCardComponent],
  templateUrl: './objectives-page.component.html',
  styleUrls: ['./objectives-page.component.scss']
})
export class ObjectivesPageComponent implements OnInit {
  @ViewChild(ObjectivesSectionComponent) objectiveSection!: ObjectivesSectionComponent;
  
  projectId!: number;
  showAddObjective: boolean = false;
  isLoading: boolean = true;
  objectiveStats: ObjectivePageStats = {
    totalObjectives: 0,
    completedObjectives: 0,
    totalKeys: 0,
    completedKeys: 0
  };

  constructor(
    private route: ActivatedRoute,
    private objectivePageService: ObjectivePageService
  ) {}

  ngOnInit(): void {
    this.route.parent?.params.subscribe(params => {
      this.projectId = +params['id'];
      this.loadObjectiveStats();
    });
  }

  private loadObjectiveStats(): void {
    this.isLoading = true;
    this.objectivePageService.getObjectiveStats(this.projectId).subscribe({
      next: (stats: ObjectivePageStats) => {
        this.objectiveStats = stats;
        this.isLoading = false;
      },
      error: (error: Error) => {
        console.error('Error loading objective stats:', error);
        this.isLoading = false;
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
    if (this.objectiveSection) {
      this.objectiveSection.loadObjectives();
    }
    this.loadObjectiveStats();
  }
}
