import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgxChartsModule, Color, ScaleType } from '@swimlane/ngx-charts';
import { ApiService } from '../../../../core/services/analytics.service';
import { Subject, takeUntil, timeout } from 'rxjs';

interface ChartData {
  name: string;
  value: number;
  extra: {
    role: string;
    totalTasks: number;
    completedTasks: number;
  };
}

@Component({
  selector: 'app-horizontal-bar',
  standalone: true,
  imports: [NgxChartsModule, CommonModule, HttpClientModule],
  providers: [ApiService],
  templateUrl: './horizontal-bar.component.html',
  styleUrls: ['./horizontal-bar.component.css']
})
export class HorizontalBarComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  chartData: ChartData[] = [];
  view: [number, number] = [600, 500];  // Default view size
  errorMessage: string | null = null;
  isBackendAvailable = true;

  // Chart options
  showXAxis = true;
  showYAxis = true;
  showXAxisLabel = true;
  showYAxisLabel = false;
  xAxisLabel = 'Progress (%)';
  yAxisLabel = 'Team Members';
  xScaleMin = 0;
  xScaleMax = 100;
  colorScheme: Color = {
    name: 'custom',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#1976d2']
  };

  constructor(
    private apiService: ApiService,
    private cdr: ChangeDetectorRef
  ) {
    console.log('Component constructor called');
  }

  ngOnInit() {
    console.log('ngOnInit called');
    this.loadTeamMembersProgress();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadTeamMembersProgress() {
    console.log('Starting to load team members progress...');
    
    const teamId = 889191999882;
    const projectId = 888290452986;
    
    console.log('Making API call with teamId:', teamId, 'projectId:', projectId);
    
    this.apiService.getTeamMembersProgress(teamId, projectId)
      .pipe(
        takeUntil(this.destroy$),
        timeout(3000)
      )
      .subscribe({
        next: (members) => {
          console.log('Raw API response:', members);
          if (members && Array.isArray(members) && members.length > 0) {
            // Map and sort the data in descending order
            this.chartData = members
              .map(member => {
                const progress = Number(member.progress) || 0;
                console.log('Processing member:', member.userName, 'Progress:', progress);
                return {
                  name: member.userName || 'Unknown User',
                  value: Math.min(Math.max(progress, 0), 100),
                  extra: {
                    role: member.role || 'No Role',
                    totalTasks: Number(member.totalTasks) || 0,
                    completedTasks: Number(member.completedTasks) || 0
                  }
                };
              })
              .sort((a, b) => b.value - a.value);  // Sort in descending order

            // Adjust view height based on number of items
            const itemHeight = 66;  // Approximate height per item
            const minHeight = 200;  // Minimum height for 3 items
            const calculatedHeight = Math.max(this.chartData.length * itemHeight, minHeight);
            this.view = [600, calculatedHeight];

            // Ensure all values are numbers and within valid range
            this.chartData = this.chartData.map(item => ({
              ...item,
              value: Number(item.value)
            }));

            console.log('Final chart data:', this.chartData);
            this.isBackendAvailable = true;
            this.errorMessage = null;
          } else {
            console.log('No valid members data received');
            this.chartData = [];
            this.errorMessage = 'No data available';
            this.isBackendAvailable = true;
          }
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error('Error loading data:', error);
          this.chartData = [];
          this.isBackendAvailable = false;
          this.errorMessage = 'Backend service is not available. Please check if the server is running.';
          this.cdr.detectChanges();
        }
      });
  }

  onSelect(data: any): void {
    console.log('Bar clicked:', data);
  }
}