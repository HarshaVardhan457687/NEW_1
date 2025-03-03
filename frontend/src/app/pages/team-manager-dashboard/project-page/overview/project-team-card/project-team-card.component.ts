import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectOverviewService, TeamDetails } from '../../../../../core/services/project-overview.service';
import { AddTeamCardComponent } from '../../../../../shared/add-team-card/add-team-card.component';
import { AddTeamService } from '../../../../../core/services/add-team.service';

@Component({
  selector: 'app-project-team-card',
  standalone: true,
  imports: [CommonModule, AddTeamCardComponent],
  templateUrl: './project-team-card.component.html',
  styleUrls: ['./project-team-card.component.scss']
})
export class ProjectTeamCardComponent implements OnInit {
  @Input() projectId!: number;
  teamDetails: TeamDetails[] = [];
  isLoading = true;
  showAddTeamCard = false;

  // Default placeholder data for loading state
  placeholderTeams = Array(4).fill({
    teamName: 'Loading...',
    teamLeaderName: 'Loading...',
    teamLeaderProfile: 'assets/pic1.png'
  });

  constructor(
    private projectService: ProjectOverviewService,
    private addTeamService: AddTeamService
  ) {}

  ngOnInit() {
    if (this.projectId) {
      this.loadTeamDetails();
    }
  }

  private loadTeamDetails() {
    this.isLoading = true;
    this.projectService.getProjectTeamDetails(this.projectId).subscribe({
      next: (details) => {
        this.teamDetails = details;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading team details:', err);
        this.isLoading = false;
      }
    });
  }

  onAddTeam() {
    this.showAddTeamCard = true;
  }

  onTeamSaved(teamData: any) {
    const createTeamRequest = {
      teamName: teamData.name,
      teamLead: teamData.leader.userId,
      teamMembers: teamData.members.map((member: any) => member.userId),
      assignedProject: this.projectId,
      assignedKeyResult: []
    };

    this.addTeamService.createTeam(createTeamRequest).subscribe({
      next: (createdTeam) => {
        console.log('Team created successfully:', createdTeam);
        // Refresh the team list
        this.loadTeamDetails();
      },
      error: (err) => {
        console.error('Error creating team:', err);
      }
    });
  }
}
