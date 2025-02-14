import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { TeamLeaderCardComponent } from './team-leader-card/team-leader-card.component';
import { TeamMemberSectionComponent } from './team-member-section/team-member-section.component';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';
import { TeamService, Team } from '../../../../../core/services/team.service';

@Component({
  selector: 'app-team-page',
  standalone: true,
  imports: [
    CommonModule,
    TeamLeaderCardComponent,
    TeamMemberSectionComponent,
    ProgressBarLinearComponent
  ],
  templateUrl: './team-page.component.html',
  styleUrls: ['./team-page.component.scss']
})
export class TeamPageComponent implements OnInit {
  teamId!: number;
  team?: Team;
  isLoading = true;
  error?: string;

  constructor(private route: ActivatedRoute,private teamService: TeamService) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.teamId = +id; // Convert string to number if necessary
        
      }
    });
    // For now, we'll hardcode team ID as 1. In a real app, this would come from the route or a parent component
    this.teamService.getTeamById(this.teamId).subscribe({
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
