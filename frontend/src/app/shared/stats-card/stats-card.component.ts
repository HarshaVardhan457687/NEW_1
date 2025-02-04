import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarCircularComponent } from '../progress-bar-circular/progress-bar-circular.component';

@Component({
  selector: 'app-stats-card',
  standalone: true,
  imports: [CommonModule, ProgressBarCircularComponent],
  templateUrl: './stats-card.component.html',
  styleUrl: './stats-card.component.scss'
})
export class StatsCardComponent {
  @Input() title: string = '';
  @Input() activeCount: number = 0;
  @Input() totalCount: number = 0;
  @Input() percentage: number = 0;
  @Input() icon?: string;
}