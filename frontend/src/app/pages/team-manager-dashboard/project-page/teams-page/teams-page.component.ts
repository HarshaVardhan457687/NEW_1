import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamsService } from '../../../../core/services/teams.service';
import { TeamsSection } from './teams-section/teams-section.component';

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
  selector: 'app-teams-page',
  standalone: true,
  imports: [
    CommonModule,
    TeamsSection
  ],
  templateUrl: './teams-page.component.html',
  styleUrls: ['./teams-page.component.scss']
})
export class TeamsPageComponent implements OnInit {
  teams: Team[] = [];

  constructor(private teamsService: TeamsService) {}

  ngOnInit() {
    this.teamsService.getTeams().subscribe((teams: Team[]) => {
      this.teams = teams;
    });
  }
}
