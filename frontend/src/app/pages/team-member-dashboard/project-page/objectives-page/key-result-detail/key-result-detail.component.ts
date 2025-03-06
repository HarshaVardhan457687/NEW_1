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

  getPriorityClass(priority: string): { [key: string]: boolean } {
    const priorityLower = priority.toLowerCase();
    return {
      'high': priorityLower === 'high',
      'medium': priorityLower === 'medium',
      'low': priorityLower === 'low'
    };
  }
}
