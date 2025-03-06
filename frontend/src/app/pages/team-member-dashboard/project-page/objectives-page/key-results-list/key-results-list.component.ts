import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeyResultDetailComponent } from '../key-result-detail/key-result-detail.component';

interface KeyResultDisplay {
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
  selector: 'app-key-results-list',
  standalone: true,
  imports: [CommonModule, KeyResultDetailComponent],
  templateUrl: './key-results-list.component.html',
  styleUrls: ['./key-results-list.component.scss']
})
export class KeyResultsListComponent {
  @Input() keyResults: any[] = [];
  showTooltip = false;
  tooltipData: { current: number; target: number; unit: string } | null = null;

  transformKeyResult(keyResult: any): KeyResultDisplay {
    return {
      keyResultId: keyResult.keyResultId,
      name: keyResult.name,
      owner: {
        name: keyResult.teamName,
        profilePic: keyResult.teamLeaderProfilePic
      },
      priority: keyResult.priority,
      progress: keyResult.progress,
      dueDate: keyResult.dueDate,
      currentValue: keyResult.currKeyResultVal,
      targetValue: keyResult.targetKeyResultVal,
      unit: keyResult.unit
    };
  }

  showValueTooltip(keyResult: any): void {
    this.tooltipData = {
      current: keyResult.currKeyResultVal,
      target: keyResult.targetKeyResultVal,
      unit: keyResult.unit
    };
    this.showTooltip = true;
  }

  hideValueTooltip(): void {
    this.showTooltip = false;
    this.tooltipData = null;
  }
}
