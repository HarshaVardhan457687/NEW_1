import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface TimelineEvent {
  phase: string;
  status: 'completed' | 'in-progress' | 'upcoming';
}

@Component({
  selector: 'app-timeline-update-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './timeline-update-card.component.html',
  styleUrls: ['./timeline-update-card.component.scss']
})
export class TimelineUpdateCardComponent {
  @Input() show: boolean = false;
  @Output() showChange = new EventEmitter<boolean>();
  selectedOption: 'new' | 'update' | null = null;
  showStatusUpdate: boolean = false;
  selectedEvent?: TimelineEvent;
  
  newEvent: TimelineEvent = {
    phase: '',
    status: 'upcoming'
  };

  events: TimelineEvent[] = [
    { phase: 'Project Initiation', status: 'completed' },
    { phase: 'Requirements Gathering', status: 'completed' },
    { phase: 'Design Phase', status: 'in-progress' },
    { phase: 'Development', status: 'upcoming' }
  ];

  close() {
    this.show = false;
    this.showChange.emit(false);
    this.selectedOption = null;
    this.showStatusUpdate = false;
    this.selectedEvent = undefined;
  }

  onUpdateStatus(event: TimelineEvent) {
    this.selectedEvent = event;
    this.showStatusUpdate = true;
  }

  onDeleteEvent(event: TimelineEvent) {
    this.events = this.events.filter(e => e !== event);
  }

  saveNewEvent() {
    if (this.newEvent.phase) {
      this.events.push({ ...this.newEvent });
      this.newEvent = { phase: '', status: 'upcoming' };
      this.close();
    }
  }

  saveStatusUpdate() {
    if (this.selectedEvent) {
      const index = this.events.findIndex(e => e === this.selectedEvent);
      if (index !== -1) {
        this.events[index] = { ...this.selectedEvent };
      }
      this.showStatusUpdate = false;
      this.selectedEvent = undefined;
    }
  }
}
