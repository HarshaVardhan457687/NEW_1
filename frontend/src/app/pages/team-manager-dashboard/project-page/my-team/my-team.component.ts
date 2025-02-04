import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamLeaderCardComponent } from './team-leader-card/team-leader-card.component';
import { TeamMemberSectionComponent } from './team-member-section/team-member-section.component';
import { ProgressBarLinearComponent } from '../../../../shared/progress-bar-linear/progress-bar-linear.component';
import { TeamService, Team } from '../../../../core/services/team.service';

@Component({
  selector: 'app-my-team',
  standalone: true,
  imports: [
    CommonModule,
    TeamLeaderCardComponent,
    TeamMemberSectionComponent,
    ProgressBarLinearComponent
  ],
  templateUrl: './my-team.component.html',
  styleUrls: ['./my-team.component.scss']
})
export class MyTeamComponent implements OnInit {
  team?: Team;
  isLoading = true;
  error?: string;

  constructor(private teamService: TeamService) {}

  ngOnInit() {
    // For now, we'll hardcode team ID as 1. In a real app, this would come from the route or a parent component
    this.teamService.getTeamById(1).subscribe({
      next: (team) => {
        this.team = team;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading team:', err);
        this.error = 'Failed to load team data';
        this.isLoading = false;
      }
    });
  }
}
