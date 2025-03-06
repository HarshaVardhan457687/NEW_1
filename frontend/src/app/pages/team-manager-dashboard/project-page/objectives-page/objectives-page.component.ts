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
    this.objectivePageService.getObjectiveStats(this.projectId).subscribe({
      next: (stats: ObjectivePageStats) => {
        this.objectiveStats = stats;
      },
      error: (error: Error) => {
        console.error('Error loading objective stats:', error);
      }
    });
  }
}
