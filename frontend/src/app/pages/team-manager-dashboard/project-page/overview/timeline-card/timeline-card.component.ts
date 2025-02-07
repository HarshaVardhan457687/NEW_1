import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TimelineUpdateCardComponent } from '../../../../../shared/timeline-update-card/timeline-update-card.component';

@Component({
  selector: 'app-timeline-card',
  standalone: true,
  imports: [CommonModule, TimelineUpdateCardComponent],
  templateUrl: './timeline-card.component.html',
  styleUrl: './timeline-card.component.scss'
})
export class TimelineCardComponent {
  @Input() projectId!: number;
  showUpdateModal = false;
}
