import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectOverviewService, TimelineEvent } from '../../../../../core/services/project-overview.service';

@Component({
  selector: 'app-timeline-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './timeline-card.component.html',
  styleUrl: './timeline-card.component.scss'
})
export class TimelineCardComponent implements OnInit {
  @Input() projectId!: number;
  timelineEvents: TimelineEvent[] = [];

  constructor(private projectService: ProjectOverviewService) {}

  ngOnInit() {
    if (this.projectId) {
      this.loadTimeline();
    }
  }

  private loadTimeline() {
    this.projectService.getProjectTimeline(this.projectId).subscribe({
      next: (events) => {
        this.timelineEvents = events;
      },
      error: (err) => {
        console.error('Error loading timeline:', err);
      }
    });
  }
  
  getFormattedStatus(status: string): string {
    return status ? status.replace(/_/g, ' ') : '';
  }

  getStatusClass(status: string): string {
    return status ? status.toLowerCase() : '';
  }
}
