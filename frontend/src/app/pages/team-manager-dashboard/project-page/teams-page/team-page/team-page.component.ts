import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { TeamLeaderCardComponent } from './team-leader-card/team-leader-card.component';
import { TeamMemberSectionComponent } from './team-member-section/team-member-section.component';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';
import { MyTeamService, TeamResponse, TeamMemberProgress } from '../../../../../core/services/my-team.service';
import { forkJoin } from 'rxjs';

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
    this.teamId = Number(this.route.snapshot.paramMap.get('id'));
    
    if (this.projectId && this.teamId) {
      this.loadTeamDetails();
    } else {
      console.error('[TeamPage] Missing projectId or teamId');
    }
  }

  private loadTeamDetails() {
    console.log('[TeamPage] Loading team details for teamId:', this.teamId);
    this.isLoading = true;

    forkJoin({
      details: this.myTeamService.getTeamById(this.projectId, this.teamId),
      members: this.myTeamService.getTeamMembersProgress(this.teamId, this.projectId)
    }).subscribe({
      next: (response) => {
        console.log('[TeamPage] Loaded team data:', response);
        this.teamDetails = response.details;
        this.teamMembers = response.members;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('[TeamPage] Error loading team data:', err);
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
