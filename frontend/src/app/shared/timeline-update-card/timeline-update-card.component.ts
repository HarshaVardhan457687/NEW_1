import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TimelineUpdateService } from '../../core/services/timeline-update.service';
import { TimelineEvent } from '../../core/services/project-overview.service';

@Component({
  selector: 'app-timeline-update-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './timeline-update-card.component.html',
  styleUrls: ['./timeline-update-card.component.scss']
})
export class TimelineUpdateCardComponent {
  @Input() show: boolean = false;
  @Input() projectId!: number;
  @Input() events: TimelineEvent[] = [];
  @Output() showChange = new EventEmitter<boolean>();
  @Output() updated = new EventEmitter<void>();

  selectedOption: 'new' | 'update' | null = null;
  showStatusUpdate: boolean = false;
  selectedEvent?: TimelineEvent;
  selectedStatus: 'UPCOMING' | 'IN_PROGRESS' | 'COMPLETED' = 'UPCOMING';
  
  newEvent = {
    phase: '',
    status: 'UPCOMING' as 'UPCOMING' | 'IN_PROGRESS' | 'COMPLETED'
  };

  constructor(private timelineService: TimelineUpdateService) {}

  close() {
    this.show = false;
    this.showChange.emit(false);
    this.selectedOption = null;
    this.showStatusUpdate = false;
    this.selectedEvent = undefined;
    this.selectedStatus = 'UPCOMING';
  }

  onUpdateStatus(event: TimelineEvent) {
    console.log('Selected event for update:', event);
    this.selectedEvent = { ...event };
    this.selectedStatus = event.timeLineStatus;
    this.showStatusUpdate = true;
    console.log('Current selected status:', this.selectedStatus);
  }

  onDeleteEvent(event: TimelineEvent) {
    this.timelineService.deleteTimeline(event.timeLineId).subscribe({
      next: () => {
        this.updated.emit();
        this.close();
      },
      error: (err) => {
        console.error('Error deleting timeline event:', err);
      }
    });
  }

  saveNewEvent() {
    if (this.newEvent.phase) {
      const request = {
        timeLineHeading: this.newEvent.phase,
        timeLineAssociatedProject: this.projectId,
        timeLineStatus: this.newEvent.status
      };

      console.log('Creating new event:', request);
      this.timelineService.createTimeline(request).subscribe({
        next: () => {
          this.updated.emit();
          this.close();
        },
        error: (err) => {
          console.error('Error creating timeline event:', err);
        }
      });
    }
  }

  saveStatusUpdate() {
    if (this.selectedEvent) {
      const request = {
        timeLineStatus: this.selectedStatus
      };

      console.log('Updating event status:', {
        eventId: this.selectedEvent.timeLineId,
        currentStatus: this.selectedEvent.timeLineStatus,
        newStatus: this.selectedStatus,
        request
      });

      this.timelineService.updateTimeline(this.selectedEvent.timeLineId, request).subscribe({
        next: () => {
          this.updated.emit();
          this.showStatusUpdate = false;
          this.selectedEvent = undefined;
          this.selectedStatus = 'UPCOMING';
        },
        error: (err) => {
          console.error('Error updating timeline event:', err);
        }
      });
    }
  }
}
