import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamCard } from '../team-card/team-card.component';
import { TeamResponse } from '../../../../../core/services/teams-page.service';

@Component({
  selector: 'app-teams-section',
  standalone: true,
  imports: [
    CommonModule,
    TeamCard
  ],
  templateUrl: './teams-section.component.html',
  styleUrls: ['./teams-section.component.scss']
})
export class TeamsSection {
  @Input() teams: TeamResponse[] = [];

  // Transform TeamResponse to the format TeamCard expects
  mapTeamResponseToCardFormat(team: TeamResponse) {
    return {
      id: team.teamId,//team.teamId,
      name: team.teamName,
      progress: Math.floor(team.teamProgress),
      active: team.totalKeyResults['completed'] !== team.totalKeyResults['total'], // Active if there are incomplete key results
      teamLeader: {
        name: team.teamLeaderName,
        role: 'Team Leader',
        image: team.teamLeaderProfile
      },
      overview: {
        teamMembers: team.totalMembers,
        keyResults: `${team.totalKeyResults['completed']}/${team.totalKeyResults['total']}`
      }
    };
  }
}
