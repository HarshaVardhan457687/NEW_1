import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarLinearComponent } from '../../../../../../shared/progress-bar-linear/progress-bar-linear.component';

@Component({
  selector: 'app-team-member-card',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent],
  templateUrl: './team-member-card.component.html',
  styleUrls: ['./team-member-card.component.scss']
})
export class TeamMemberCardComponent {
  @Input() name: string = '';
  @Input() role: string = '';
  @Input() image: string = '';
  @Input() progress: number = 0;
}
