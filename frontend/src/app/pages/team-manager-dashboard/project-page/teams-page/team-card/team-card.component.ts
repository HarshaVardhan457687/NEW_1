import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';

interface Team {
  name: string;
  progress: number;
  active: boolean;
  teamLeader: {
    name: string;
    role: string;
    image: string;
  };
  overview: {
    teamMembers: number;
    objectives: string;
  };
}

@Component({
  selector: 'app-team-card',
  standalone: true,
  imports: [
    CommonModule,
    ProgressBarLinearComponent
  ],
  templateUrl: './team-card.component.html',
  styleUrls: ['./team-card.component.scss']
})
export class TeamCard {
  @Input() team!: Team;
}
