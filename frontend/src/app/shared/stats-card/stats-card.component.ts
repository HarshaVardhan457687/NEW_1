import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarCircularComponent } from '../progress-bar-circular/progress-bar-circular.component';

@Component({
  selector: 'app-stats-card',
  standalone: true,
  imports: [CommonModule, ProgressBarCircularComponent],
  templateUrl: './stats-card.component.html',
  styleUrl: './stats-card.component.scss'
})
export class StatsCardComponent implements OnInit {
  @Input() title: string = '';
  @Input() activeCount: number | null = 0;
  @Input() totalCount: number | null = 0;
  @Input() percentage: number | null = 0;
  @Input() icon?: string;

  displayedActiveCount = 0;
  displayedTotalCount = 0;
  displayedPercentage = 0;

  ngOnInit() {
    this.setDisplayValues();
  }

  ngOnChanges() {
    this.setDisplayValues();
  }

  private setDisplayValues() {
    // Set safe values for display, defaulting to 0 if null/undefined/NaN
    this.displayedActiveCount = this.getSafeNumber(this.activeCount);
    this.displayedTotalCount = this.getSafeNumber(this.totalCount);
    this.displayedPercentage = this.getSafeNumber(this.percentage);
  }

  private getSafeNumber(value: number | null): number {
    if (value === null || value === undefined || isNaN(value)) {
      return 0;
    }
    return value;
  }
}