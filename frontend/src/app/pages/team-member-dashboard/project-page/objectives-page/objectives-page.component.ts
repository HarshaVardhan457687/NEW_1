import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveStatCardComponent } from '../../../../shared/objective-stat-card/objective-stat-card.component';
import { ActivatedRoute } from '@angular/router';
import { ObjectivePageService, ObjectivePageStats } from '../../../../core/services/objective-page.service';
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
}
