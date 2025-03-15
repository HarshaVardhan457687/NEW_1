import { NgxChartsModule, Color, ScaleType } from '@swimlane/ngx-charts';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../../core/services/analytics.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-distribution',
  imports: [NgxChartsModule, CommonModule, HttpClientModule],
  providers: [ApiService],
  templateUrl: './distribution.component.html',
  styleUrl: './distribution.component.css',
  standalone: true
})
export class DistributionComponent implements OnInit {
  showDropdown = false;
  headerTitle: string = 'Objective Performance';
  currentProject = 1; // Default project ID

  pieChartData: any[] = [];

  view: [number, number] = [200, 200];
  gradient = false;
  showLegend = false;
  showLabels = false;
  isDoughnut = true;
  
  pieChartColors: Color = {
    name: 'custom',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#32CD32', '#FFD700', '#2196F3']
  };

  constructor(private apiService: ApiService) {
    this.resetChartData(); // Initialize with zero values
  }

  ngOnInit(): void {
    this.loadObjectivePerformance();
  }

  loadObjectivePerformance(): void {
    this.apiService.getObjectivePerformance(this.currentProject)
      .subscribe({
        next: (data) => {
          if (data && data.length > 0) {
            this.pieChartData = data.map(item => ({
              ...item,
              value: item.value || 0 // Ensure value is 0 if null/undefined
            }));
          } else {
            this.resetChartData();
          }
        },
        error: () => {
          this.resetChartData();
        }
      });
  }

  resetChartData(): void {
    // Initialize with zero values
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