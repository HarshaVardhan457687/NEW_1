import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';

@Component({
  selector: 'app-team-leader-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './team-leader-card.component.html',
  styleUrls: ['./team-leader-card.component.scss']
})
export class TeamLeaderCardComponent {
  @Input() name: string = '';
  @Input() role: string = '';
  @Input() image: string = '';
  @Input() progress: number = 0;
}
