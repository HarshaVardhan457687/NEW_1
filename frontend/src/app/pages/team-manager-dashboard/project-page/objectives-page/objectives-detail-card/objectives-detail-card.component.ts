import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeyResultsListComponent } from '../key-results-list/key-results-list.component';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';
import { AddKeyResultCardComponent } from '../../../../../shared/add-key-result-card/add-key-result-card.component';

interface ObjectiveDisplay {
  id: number;
  name: string;
  status: string;
  progress: number;
}

interface KeyResultDisplay {
  keyResultId: number;
  name: string;
  priority: string;
  currKeyResultVal: number;
  targetKeyResultVal: number;
  unit: string;
  dueDate: string;
  progress: number;
  teamName: string;
  teamLeaderProfilePic: string | null;
}

@Component({
  selector: 'app-objectives-detail-card',
  standalone: true,
  imports: [CommonModule, KeyResultsListComponent, ProgressBarLinearComponent, AddKeyResultCardComponent],
  templateUrl: './objectives-detail-card.component.html',
  styleUrls: ['./objectives-detail-card.component.scss']
})
export class ObjectivesDetailCardComponent {
  @Input() projectId!: number;
  @Input() objective!: ObjectiveDisplay;
  @Input() keyResults: KeyResultDisplay[] = [];

  showAddKeyResult: boolean = false;

  getStatusClass(status: string): string {
    if(!status) {
      return '';
    }
    return status.toLowerCase();
  }

  formatStatus(status: string): string {
    if(!status) {
      return '';
    }
    return status.replace('_', ' ').toLowerCase()
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }

  formatProgress(progess:number):number{
    return Math.floor(progess);
  }

  onAddKeyResult() {
    this.showAddKeyResult = true;
  }

  onCloseKeyResult() {
    this.showAddKeyResult = false;
  }

  onSaveKeyResult(data: any) {
    // TODO: Handle the saved key result data
    console.log('Key result saved:', data);
    this.showAddKeyResult = false;
  }
}
