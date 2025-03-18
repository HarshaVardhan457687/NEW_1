// vertical-bar.component.ts
import { Component, OnInit, OnDestroy, PLATFORM_ID, Inject } from '@angular/core';
import { NgxChartsModule, LegendPosition } from '@swimlane/ngx-charts';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Color, ScaleType } from '@swimlane/ngx-charts';
import { ApiService } from '../../../../core/services/analytics.service';
import { ProjectSelectionService } from '../../../../core/services/project-selection.service';
import { TeamsPageService, TeamResponse } from '../../../../core/services/teams-page.service';
import { HttpClientModule } from '@angular/common/http';
import { forkJoin, Subject, takeUntil, catchError, of } from 'rxjs';
import { switchMap, map } from 'rxjs/operators';

interface ChartData {
  name: string;
  value: number;
}

@Component({
  selector: 'app-vertical-bar',
  standalone: true,
  imports: [NgxChartsModule, CommonModule, HttpClientModule],
  providers: [ApiService, TeamsPageService],
  templateUrl: './vertical-bar.component.html',
  styleUrls: ['./vertical-bar.component.css']
})
export class VerticalBarComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  barChartData: ChartData[] = [];
  currentProject: number | null = null;
  teams: TeamResponse[] = [];
  isBrowser: boolean;

  view: [number, number] = [400, 150];
  errorMessage: string | null = null;

  gradient = false;
  showLegend = false;
  showXAxis = true;
  showYAxis = true;
  showXAxisLabel = false;
  showYAxisLabel = false;
  xAxisLabel = '';
  yAxisLabel = '';
  barPadding = 20;
  roundDomains = true;
  animations = true;

  defaultColors: Color = {
    name: 'custom',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#2196F3', '#4CAF50', '#FFC107']
  };

  constructor(
    private apiService: ApiService,
    private projectService: ProjectSelectionService,
    private teamsService: TeamsPageService,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  ngOnInit(): void {
    if (!this.isBrowser) {
      return; // Skip initialization on server-side
    }

    // Get initial project ID and load data if available
    this.currentProject = this.projectService.getSelectedProject();
    if (this.currentProject) {
      this.loadTeamsAndProgress(this.currentProject);
    }

    // Subscribe to project changes
    this.projectService.selectedProject$
      .pipe(takeUntil(this.destroy$))
      .subscribe(projectId => {
        console.log('Project ID changed:', projectId);
        this.currentProject = projectId;
        if (projectId) {
          this.loadTeamsAndProgress(projectId);
        } else {
          this.resetChartData();
        }
      });
  }

  ngOnDestroy(): void {
    if (this.isBrowser) {
      this.destroy$.next();
      this.destroy$.complete();
    }
  }

  private calculateChartWidth(): void {
    if (!this.isBrowser) return;

    const minBarWidth = 100;
    const totalBars = this.barChartData.length;
    const totalPadding = (totalBars + 1) * this.barPadding;
    const calculatedWidth = (minBarWidth * totalBars) + totalPadding;
    
    this.view = [Math.max(calculatedWidth, 400), 150];
  }

  private loadTeamsAndProgress(projectId: number): void {
    if (!this.isBrowser) return;

    console.log("Loading teams and progress for project:", projectId);
    this.errorMessage = null;

    // First get all teams for the project
    this.teamsService.getTeamsForProject(projectId)
      .pipe(
        catchError(error => {
          console.error('Error fetching teams:', error);
          this.errorMessage = 'Error loading teams data.';
          return of([]);
        }),
        switchMap(teams => {
          this.teams = teams;
          console.log('Teams loaded:', teams);
          
          if (!teams || teams.length === 0) {
            this.errorMessage = 'No teams found for this project.';
            return of([]);
          }

          // Create observables for each team's progress
          const progressObservables = teams.map(team => 
            this.apiService.getTeamProgress(projectId, team.teamId)
              .pipe(
                map(response => {
                  console.log('Team progress response:', response);
                  // Handle both possible response structures
                  const progress = typeof response === 'number' ? response : response?.progress;
                  return typeof progress === 'number' ? progress : 0;
                }),
                catchError(error => {
                  console.error('Error fetching team progress:', error);
                  return of(0);
                })
              )
          );

          return forkJoin(progressObservables).pipe(
            map(progressResults => {
              console.log('All progress results:', progressResults);
              return teams.map((team, index) => {
                const progress = progressResults[index];
                return {
                  name: team.teamName,
                  value: Number(progress.toFixed(2))
                };
              });
            })
          );
        })
      )
      .subscribe({
        next: (chartData) => {
          console.log('Final chart data:', chartData);
          this.barChartData = chartData;
          this.calculateChartWidth();
        },
        error: (error) => {
          console.error('Error in data pipeline:', error);
          this.errorMessage = 'Error loading team progress data.';
          this.resetChartData();
        }
      });
  }

  private resetChartData(): void {
    this.barChartData = [];
    this.calculateChartWidth();
  }

  onSelect(data: any): void {
    if (!this.isBrowser) return;
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  yAxisTickFormatting = (val: any) => `${val}%`;
}