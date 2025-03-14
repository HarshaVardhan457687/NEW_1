// dashboard.component.ts
import { Component } from '@angular/core';
import { NgxChartsModule, LegendPosition } from '@swimlane/ngx-charts';
import { CommonModule } from '@angular/common';
import { Color, ScaleType } from '@swimlane/ngx-charts';
import { DistributionComponent } from '../graphs/distribution/distribution.component';
import { HorizontalBarComponent } from '../graphs/horizontal-bar/horizontal-bar.component';
import { VerticalBarComponent } from '../graphs/vertical-bar/vertical-bar.component';
import { PerformanceComponent } from '../graphs/performance/performance.component';




@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  imports: [NgxChartsModule, CommonModule,PerformanceComponent,VerticalBarComponent,HorizontalBarComponent,DistributionComponent]
})
export class DashboardComponent {
  // Pie Chart Data
  pieChartData = [
    { name: 'Product A', value: 300 },
    { name: 'Product B', value: 200 },
    { name: 'Product C', value: 150 },
    { name: 'Product D', value: 100 }
  ];

  // Bar Chart Data
  barChartData = [
    {
      name: 'Monthly Revenue',
      series: [
        { name: 'Jan', value: 65 },
        { name: 'Feb', value: 59 },
        { name: 'Mar', value: 80 },
        { name: 'Apr', value: 81 },
        { name: 'May', value: 56 },
        { name: 'Jun', value: 55 }
      ]
    }
  ];

  // Horizontal Bar Chart Data
  horizontalBarData = [
    { name: 'Product A', value: 65 },
    { name: 'Product B', value: 59 },
    { name: 'Product C', value: 80 },
    { name: 'Product D', value: 81 },
    { name: 'Product E', value: 85 },
    { name: 'Product F', value: 80 },
    { name: 'Product G', value: 81 },
    { name: 'Product H', value: 85 },
  ];

  // Line Chart Data with value labels
  

  // Chart options
  view: [number, number] = [500, 300];
  gradient = false;
  showLegend = true;
  showLabels = true;
  isDoughnut = true;
  legendPosition: LegendPosition = LegendPosition.Below;
  showXAxis = true;
  showYAxis = true;
  showXAxisLabel = true;
  showYAxisLabel = true;
  xAxisLabel = 'Month';
  yAxisLabel = 'Value';
  timeline = false;
  
  // Specific options for line chart
  showRefLines = true;
  showRefLabels = true;
  roundDomains = true;
  tooltipDisabled = false;
  animations = true;

  // Different color schemes for different charts
  pieChartColors: Color = {
    name: 'custom',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#FF1493', '#32CD32', '#FFD700', '#00CED1']
  };

  defaultColors: Color = {
    name: 'custom',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#2196F3']
  };

  constructor() {}

  onSelect(data: any): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  yAxisTickFormatting = (val: any) => `${val}`;
}

export default DashboardComponent;