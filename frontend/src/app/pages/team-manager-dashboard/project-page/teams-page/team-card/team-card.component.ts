import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
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
  @Input() team: any;

  constructor(private router: Router) {}

  onTeamClick() {
    // Get the current project ID from the URL
    const urlSegments = this.router.url.split('/');
    const projectId = urlSegments[urlSegments.indexOf('projects') + 1];
    
    this.router.navigate(['/team-manager-dashboard', 'projects', projectId, 'teams', this.team.id]);
  }
}
