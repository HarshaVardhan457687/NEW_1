import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamLeaderCardComponent } from './team-leader-card/team-leader-card.component';
import { TeamMemberSectionComponent } from './team-member-section/team-member-section.component';
import { ProgressBarLinearComponent } from '../../../../shared/progress-bar-linear/progress-bar-linear.component';
import { MyTeamService, TeamResponse, TeamMemberProgress } from '../../../../core/services/my-team.service';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';

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
  teamDetails?: TeamResponse;
  teamMembers: TeamMemberProgress[] = [];
  isLoading = true;
  projectId!: number;
  teamId!: number;

  constructor(
    private myTeamService: MyTeamService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.projectId = Number(this.route.parent?.parent?.snapshot.paramMap.get('id'));
    console.log('[MyTeam] Initializing with projectId from URL:', this.projectId);
    
    if (this.projectId) {
      this.myTeamService.getMappedTeamId(this.projectId).subscribe({
        next: (teamId) => {
          console.log('[MyTeam] Retrieved teamId:', teamId);
          this.teamId = teamId;
          this.loadTeamDetails();
        },
        error: (err) => console.error('[MyTeam] Error getting team id:', err)
      });
    } else {
      console.error('[MyTeam] No projectId found in URL');
    }
  }

  private loadTeamDetails() {
    if (!this.projectId || !this.teamId) {
      console.warn('[MyTeam] Missing projectId or teamId');
      return;
    }

    console.log('[MyTeam] Loading team details for teamId:', this.teamId);
    this.isLoading = true;

    // Load both team details and member progress
    forkJoin({
      details: this.myTeamService.getTeamById(this.projectId, this.teamId),
      members: this.myTeamService.getTeamMembersProgress(this.teamId, this.projectId)
    }).subscribe({
      next: (response) => {
        console.log('[MyTeam] Loaded team data:', response);
        this.teamDetails = response.details;
        this.teamMembers = response.members;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('[MyTeam] Error loading team data:', err);
        this.isLoading = false;
      }
    });
  }

  getCompletedKeyResults(): string {
    const completed = this.teamDetails?.totalKeyResults['completed'] ?? 0;
    const total = this.teamDetails?.totalKeyResults['total'] ?? 0;
    return `${completed}/${total}`;
  }

  getCompletedTasks(): string {
    const completed = this.teamDetails?.teamTasksCount['completedTasks'] ?? 0;
    const total = this.teamDetails?.teamTasksCount['totalTasks'] ?? 0;
    return `${completed}/${total}`;
  }

  get teamProgress(): number {
    return Math.floor(this.teamDetails?.teamProgress ?? 0);
  }
  
}
