// vertical-bar.component.ts
import { Component, OnInit } from '@angular/core';
import { NgxChartsModule, LegendPosition } from '@swimlane/ngx-charts';
import { CommonModule } from '@angular/common';
import { Color, ScaleType } from '@swimlane/ngx-charts';
import { ApiService } from '../../../../core/services/analytics.service';
import { HttpClientModule } from '@angular/common/http';
import { forkJoin } from 'rxjs';

interface ChartData {
  name: string;
  value: number;
}

@Component({
  selector: 'app-vertical-bar',
  standalone: true,
  imports: [NgxChartsModule, CommonModule, HttpClientModule],
  providers: [ApiService],
  templateUrl: './vertical-bar.component.html',
  styleUrls: ['./vertical-bar.component.css']
})
export class VerticalBarComponent implements OnInit {
  barChartData = [
    {
      name: 'Team 1',
      value: 0
    },
    {
      name: 'Team 2',
      value: 0
    },
    {
      name: 'Team 3',
      value: 0
    }
  ];

  currentProject = 888290452986;
  teams = [
    { id: 889191999882, name: 'Team 1' },
    { id: 955591459730, name: 'Team 2' },
    { id: 29759564189, name: 'Team 3' }
  ];

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
    domain: ['#2196F3', '#4CAF50', '#FFC107']
  };

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadAllTeamsProgress();
  }

  loadAllTeamsProgress(): void {
    console.log("Loading progress for all teams");
    this.errorMessage = null;
    
    const teamProgressObservables = this.teams.map(team => 
      this.apiService.getTeamProgress(this.currentProject, team.id)
    );

    forkJoin(teamProgressObservables).subscribe({
      next: (results: any[]) => {
        console.log("Raw progress responses:", results);
        
        // Create a new array to trigger change detection
        const newData = this.teams.map((team, index) => ({
          name: team.name,
          value: Number(results[index].toFixed(2))
        }));
        
        // Update the data with a new reference
        this.barChartData = [...newData];
        console.log("Updated chart data:", this.barChartData);
      },
      error: (error) => {
        console.error('Error loading team progress:', error);
        this.errorMessage = error.error?.message || 'An error occurred while loading team progress.';
        this.barChartData = this.teams.map(team => ({
          name: team.name,
          value: 0
        }));
      }
    });
  }

  onSelect(data: any): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  yAxisTickFormatting = (val: any) => `${val}%`;
}