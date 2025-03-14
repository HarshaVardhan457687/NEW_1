import { Component, OnInit } from '@angular/core';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { CommonModule } from '@angular/common';
import { Color, ScaleType } from '@swimlane/ngx-charts';
import { ApiService } from '../../../../core/services/analytics.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-objective-performance',
  standalone: true,
  imports: [NgxChartsModule, CommonModule, HttpClientModule],
  providers: [ApiService],
  templateUrl: './objective-performance.component.html',
  styleUrls: ['./objective-performance.component.css']
})
export class ObjectivePerformanceComponent implements OnInit {
  barChartData = [
    {
      name: 'On Track',
      value: 0
    },
    {
      name: 'At Risk',
      value: 0
    },
    {
      name: 'Completed',
      value: 0
    }
  ];

  currentProject = 888290452986;
  view: [number, number] = [500, 250];
  errorMessage: string | null = null;

  gradient = false;
  showLegend = false;
  showXAxis = true;
  showYAxis = true;
  showXAxisLabel = false;
  showYAxisLabel = false;
  xAxisLabel = '';
  yAxisLabel = '';
  barPadding = 4;
  roundDomains = true;
  animations = true;

  defaultColors: Color = {
    name: 'custom',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#4CAF50', '#FFC107', '#2196F3']  // Green for On Track, Yellow for At Risk, Blue for Completed
  };

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadObjectivePerformance();
  }

  loadObjectivePerformance(): void {
    console.log("Loading objective performance for project:", this.currentProject);
    this.errorMessage = null;
    
    this.apiService.getObjectivePerformance(this.currentProject).subscribe({
      next: (data) => {
        console.log("Objective performance data:", data);
        this.barChartData = [...data];
      },
      error: (error) => {
        console.error('Error loading objective performance:', error);
        this.errorMessage = error.error?.message || 'An error occurred while loading objective performance.';
      }
    });
  }

  onSelect(data: any): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  yAxisTickFormatting = (val: any) => `${val}`;
} 