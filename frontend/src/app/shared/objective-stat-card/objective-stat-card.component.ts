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
  @Input() isCompact: boolean = false;

  get cardStyles() {
    if (this.isCompact) {
      return {
        'height': '90px',
        'padding': '1rem',
        'font-size': '12px'
      };
    }
    return {
      'height': '112.32px',
      'padding': '1.25rem',
      'font-size': '14px'
    };
  }

  get titleStyles() {
    if (this.isCompact) {
      return {
        'font-size': '12px',
        'line-height': '14px',
        'margin-bottom': '6px'
      };
    }
    return {
      'font-size': '14px',
      'line-height': '17px',
      'margin-bottom': '8px'
    };
  }

  get valueStyles() {
    if (this.isCompact) {
      return {
        'font-size': '20px',
        'line-height': '24px'
      };
    }
    return {
      'font-size': '24.5071px',
      'line-height': '30px'
    };
  }
}
