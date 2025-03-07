import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';

interface TransformedKeyResult {
  keyResultId: number;
  name: string;
  owner: {
    name: string;
    profilePic: string | null;
  };
  priority: string;
  progress: number;
  dueDate: string;
  currentValue: number;
  targetValue: number;
  unit: string;
}

@Component({
  selector: 'app-key-result-detail',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent],
  templateUrl: './key-result-detail.component.html',
  styleUrls: ['./key-result-detail.component.scss']
})
export class KeyResultDetailComponent {
  @Input() keyResult!: TransformedKeyResult;
  showTooltip = false;
  tooltipData: { current: number; target: number; unit: string } | null = null;
  tooltipPosition = { top: 0, left: 0 };

  showValueTooltip(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const rect = target.getBoundingClientRect();
    
    this.tooltipPosition = {
      top: rect.top - 60,  // Position above the icon with some padding
      left: rect.left
    };

    this.tooltipData = {
      current: this.keyResult.currentValue,
      target: this.keyResult.targetValue,
      unit: this.keyResult.unit
    };
    this.showTooltip = true;
  }

  hideValueTooltip(): void {
    this.showTooltip = false;
    this.tooltipData = null;
  }

  getPriorityClass(priority: string): { [key: string]: boolean } {
    const priorityLower = priority.toLowerCase();
    return {
      'high': priorityLower === 'high',
      'medium': priorityLower === 'medium',
      'low': priorityLower === 'low'
    };
  }
}
