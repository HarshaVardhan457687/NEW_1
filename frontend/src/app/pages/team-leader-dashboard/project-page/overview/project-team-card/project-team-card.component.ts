import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectOverviewService, TeamDetails } from '../../../../../core/services/project-overview.service';

@Component({
  selector: 'app-project-team-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './project-team-card.component.html',
  styleUrls: ['./project-team-card.component.scss']
})
export class ProjectTeamCardComponent implements OnInit {
  @Input() projectId!: number;
  teamDetails: TeamDetails[] = [];
  isLoading = true;

  // Default placeholder data for loading state
  placeholderTeams = Array(4).fill({
    teamName: 'Loading...',
    teamLeaderName: 'Loading...',
    teamLeaderProfile: 'assets/pic1.png'
  });

  constructor(private projectService: ProjectOverviewService) {}

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
}
