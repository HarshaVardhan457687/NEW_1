import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { TeamsSection } from './teams-section/teams-section.component';
import { AddTeamCardComponent } from '../../../../shared/add-team-card/add-team-card.component';
import { TeamsPageService, TeamResponse } from '../../../../core/services/teams-page.service';

@Component({
  selector: 'app-teams-page',
  standalone: true,
  imports: [
    CommonModule,
    TeamsSection,
    AddTeamCardComponent
  ],
  templateUrl: './teams-page.component.html',
  styleUrls: ['./teams-page.component.scss']
})
export class TeamsPageComponent implements OnInit {
  teams: TeamResponse[] = [];
  isLoading = true;
  projectId!: number;

  constructor(
    private teamsPageService: TeamsPageService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // Get project ID from URL - going up two levels to get to project-page route
    this.projectId = Number(this.route.parent?.parent?.snapshot.paramMap.get('id'));
    console.log('[TeamsPage] Initializing with projectId:', this.projectId);

    if (this.projectId) {
      this.loadTeams();
    } else {
      console.error('[TeamsPage] No project ID found in URL');
    }
  }

  private loadTeams() {
    this.isLoading = true;
    this.teamsPageService.getTeamsForProject(this.projectId).subscribe({
      next: (teams) => {
        console.log('[TeamsPage] Teams loaded:', teams);
        this.teams = teams;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('[TeamsPage] Error loading teams:', error);
        this.isLoading = false;
      }
    });
  }

  showAddTeamCard = false;
  onAddTeam() {
    this.showAddTeamCard = true;
  }
}
