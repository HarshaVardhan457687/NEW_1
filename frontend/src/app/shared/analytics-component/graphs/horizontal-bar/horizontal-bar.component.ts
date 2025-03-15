import { Component, OnInit, OnDestroy, ChangeDetectorRef, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
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
  imports: [
    NgxChartsModule, 
    CommonModule, 
    HttpClientModule,
    FormsModule
  ],
  providers: [ApiService],
  templateUrl: './horizontal-bar.component.html',
  styleUrls: ['./horizontal-bar.component.css']
})
export class HorizontalBarComponent implements OnInit, OnDestroy {
  @ViewChild('chartArea') chartArea!: ElementRef;
  
  private destroy$ = new Subject<void>();
  chartData: ChartData[] = [];
  view: [number, number] = [400, 150];
  
  // Add these properties for x-axis configuration
  xAxisTicks = [0, 25, 50, 75, 100]; // Fixed ticks for x-axis
  showGridLines = true;
  xAxisTickFormatting = (value: number) => `${value}`; // Format as simple number

  // Update the chart options
  showXAxis = true;
  showYAxis = true;
  showXAxisLabel = false;
  showYAxisLabel = false;
  xAxisLabel = '';
  yAxisLabel = '';
  xScaleMin = 0;
  xScaleMax = 100;
  colorScheme: Color = {
    name: 'custom',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#1976d2']
  };

  selectedTeamId = 889191999882; // Default selected team
  teams = [
    { id: 889191999882, name: 'Team 1' },
    { id: 955591459730, name: 'Team 2' },
    { id: 29759564189, name: 'Team 3' }
  ];

  canScrollUp = false;
  canScrollDown = false;

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

  onTeamChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedTeamId = Number(selectElement.value);
    this.loadTeamMembersProgress();
  }

  calculateChartDimensions(dataLength: number): void {
    const barHeight = 30; // Height per bar
    const padding = 40; // Additional padding
    
    // Calculate the total height needed for all bars
    const totalHeight = (dataLength * barHeight) + padding;
    
    // Set the view width and the calculated height
    this.view = [400, totalHeight];
  }

  loadTeamMembersProgress() {
    console.log('Starting to load team members progress...');
    
    const teamId = this.selectedTeamId;
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
            this.chartData = members
              .map(member => ({
                name: member.userName || 'Unknown User',
                value: Math.min(Math.max(Number(member.progress) || 0, 0), 100),
                extra: {
                  role: member.role || 'No Role',
                  totalTasks: Number(member.totalTasks) || 0,
                  completedTasks: Number(member.completedTasks) || 0
                }
              }))
              .sort((a, b) => b.value - a.value);

            // Calculate dimensions based on number of team members
            this.calculateChartDimensions(this.chartData.length);

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
     
      });
  }

  onSelect(data: any): void {
    console.log('Bar clicked:', data);
  }

  scrollUp() {
    const element = this.chartArea.nativeElement;
    element.scrollTop -= 50; // Adjust scroll amount as needed
    this.updateScrollButtons();
  }

  scrollDown() {
    const element = this.chartArea.nativeElement;
    element.scrollTop += 50; // Adjust scroll amount as needed
    this.updateScrollButtons();
  }

  updateScrollButtons() {
    const element = this.chartArea.nativeElement;
    this.canScrollUp = element.scrollTop > 0;
    this.canScrollDown = element.scrollTop < (element.scrollHeight - element.clientHeight);
  }

  ngAfterViewInit() {
    this.updateScrollButtons();
    this.chartArea.nativeElement.addEventListener('scroll', () => {
      this.updateScrollButtons();
    });
  }
}