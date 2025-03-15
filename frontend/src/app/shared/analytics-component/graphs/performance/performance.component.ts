// performance.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgxChartsModule, LegendPosition } from '@swimlane/ngx-charts';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Color, ScaleType } from '@swimlane/ngx-charts';
import { ApiService } from '../../../../core/services/analytics.service';
import { interval, Subscription } from 'rxjs';

interface MonthlyTask {
  name: string;
  value: number;
}

interface TeamData {
  name: string;
  series: MonthlyTask[];
}

@Component({
  selector: 'app-performance',
  standalone: true,
  imports: [NgxChartsModule, CommonModule, FormsModule],
  templateUrl: './performance.component.html',
  styleUrl: './performance.component.css'
})
export class PerformanceComponent implements OnInit, OnDestroy {
  allMonthsData: TeamData[] = [{
    name: 'Tasks',
    series: []
  }];

  currentTeamData: TeamData[] = [];
  
  view: [number, number] = [300, 150];
  gradient = false;
  showLegend = false;
  showLabels = true;
  isDoughnut = false;
  legendPosition: LegendPosition = LegendPosition.Below;
  showXAxis = true;
  showYAxis = true;
  showXAxisLabel = true;
  showYAxisLabel = true;
  xAxisLabel = '';
  yAxisLabel = 'Tasks';
  timeline = false;

  showRefLines = true;
  showRefLabels = true;
  roundDomains = true;
  tooltipDisabled = false;
  animations = true;
  
  currentStartIndex = 0;
  monthsToShow = 6;
  
  defaultColors: Color = {
    name: 'custom',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#2196F3']
  };

  private refreshSubscription: Subscription | undefined;
  private readonly REFRESH_INTERVAL = 30000; // Refresh every 30 seconds

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadApprovedTasks();
    // Set up periodic refresh
    this.refreshSubscription = interval(this.REFRESH_INTERVAL)
      .subscribe(() => {
        this.loadApprovedTasks();
      });
  }

  ngOnDestroy(): void {
    // Clean up subscription when component is destroyed
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
    }
  }

  private loadApprovedTasks(): void {
    this.apiService.getApprovedTasksByMonth(888290452986).subscribe(data => {
      if (data && data.length > 0) {
        // Sort data by year and month
        data.sort((a, b) => {
          if (a.year !== b.year) return a.year - b.year;
          return a.month - b.month;
        });

        // Convert the data to the format needed for the chart
        const series = data.map(item => ({
          name: this.getMonthName(item.month),
          value: item.totalTasks
        }));

        // Update the data
        this.allMonthsData = [{
          name: 'Tasks',
          series: series
        }];

        // Update the visible data while maintaining the current view position
        this.updateVisibleData();
      }
    });
  }

  private getMonthName(month: number): string {
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return months[month - 1];
  }

  initializeData(): void {
    this.currentTeamData = [{
      name: 'Tasks',
      series: this.allMonthsData[0].series.slice(0, this.monthsToShow)
    }];
  }

  updateVisibleData(): void {
    const visibleSeries = this.allMonthsData[0].series
      .slice(this.currentStartIndex, this.currentStartIndex + this.monthsToShow);
    
    this.currentTeamData = [{
      name: 'Tasks',
      series: visibleSeries
    }];
  }

  onSelect(data: any): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  yAxisTickFormatting = (val: number): string => `${val}`;
  
  scrollRight(): void {
    const maxStartIndex = this.allMonthsData[0].series.length - this.monthsToShow;
    if (this.currentStartIndex < maxStartIndex) {
      this.currentStartIndex++;
      this.updateVisibleData();
    }
  }
  
  scrollLeft(): void {
    if (this.currentStartIndex > 0) {
      this.currentStartIndex--;
      this.updateVisibleData();
    }
  }

  get canScrollLeft(): boolean {
    return this.currentStartIndex > 0;
  }

  get canScrollRight(): boolean {
    const maxStartIndex = this.allMonthsData[0].series.length - this.monthsToShow;
    return this.currentStartIndex < maxStartIndex;
  }
}