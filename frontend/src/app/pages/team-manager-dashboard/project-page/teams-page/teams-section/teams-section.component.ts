import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamCard } from '../team-card/team-card.component';

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
  @Input() teams: Team[] = [];
}
