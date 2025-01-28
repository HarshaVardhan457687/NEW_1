import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface ObjectiveStatus {
  total: number;
  completed: number;
  inProgress: number;
  notStarted: number;
}

@Component({
  selector: 'app-objective-stat-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './objective-stat-card.component.html',
  styleUrls: ['./objective-stat-card.component.scss']
})
export class ObjectiveStatCardComponent {
  @Input() title!: string;
  @Input() value!: number;
  @Input() color!: string;
}
