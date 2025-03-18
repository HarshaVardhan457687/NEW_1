import { NgxChartsModule, Color, ScaleType } from '@swimlane/ngx-charts';
import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ApiService } from '../../../../core/services/analytics.service';
import { ProjectSelectionService } from '../../../../core/services/project-selection.service';
import { HttpClientModule } from '@angular/common/http';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-distribution',
  imports: [NgxChartsModule, CommonModule, HttpClientModule],
  providers: [ApiService],
  templateUrl: './distribution.component.html',
  styleUrl: './distribution.component.css',
  standalone: true
})
export class DistributionComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  showDropdown = false;
  headerTitle: string = 'Objective Performance';
  currentProject: number | null = null;
  pieChartData: any[] = [];

  view: [number, number] = [400, 300];
  gradient = false;
  showLegend = false;
  showLabels = true;
  isDoughnut = true;
  
  pieChartColors: Color = {
    name: 'custom',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#32CD32', '#FFD700', '#2196F3']
  };

  constructor(
    private apiService: ApiService,
    private projectService: ProjectSelectionService
  ) {
    // Initialize with default empty data
    this.resetChartData();
  }

  ngOnInit(): void {
    // Get initial project ID
    this.currentProject = this.projectService.getSelectedProject();
    if (this.currentProject !== null) {
      this.loadObjectivePerformance();
    }

    // Subscribe to project changes
    this.projectService.selectedProject$
      .pipe(takeUntil(this.destroy$))
      .subscribe(projectId => {
        console.log('Project ID changed:', projectId);
        this.currentProject = projectId;
        if (projectId !== null) {
          this.loadObjectivePerformance();
        } else {
          this.resetChartData();
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadObjectivePerformance(): void {
    console.log('Loading objective performance for project:', this.currentProject);
    if (!this.currentProject) {
      console.log('No project selected, resetting chart data');
      this.resetChartData();
      return;
    }

    this.apiService.getObjectivePerformance(this.currentProject)
      .subscribe({
        next: (data) => {
          console.log('Received objective performance data:', data);
          if (data && data.length > 0) {
            this.pieChartData = data.map(item => ({
              name: item.name,
              value: Number(item.value)
            }));
          } else {
            console.log('No data received, resetting chart');
            this.resetChartData();
          }
        },
        error: (error) => {
          console.error('Error loading objective performance:', error);
          this.resetChartData();
        }
      });
  }

  resetChartData(): void {
    this.pieChartData = [
      { name: 'On Track', value: 0 },
      { name: 'At Risk', value: 0 },
      { name: 'Completed', value: 0 }
    ];
  }

  getColor(name: string): string {
    switch (name) {
      case 'On Track':
        return '#32CD32';
      case 'At Risk':
        return '#FFD700';
      case 'Completed':
        return '#2196F3';
      default:
        return '#000000';
    }
  }

  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
  }

  onSelect(data: any): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }
}