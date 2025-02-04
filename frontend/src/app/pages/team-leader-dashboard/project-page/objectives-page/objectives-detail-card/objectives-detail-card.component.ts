import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Objective, KeyResult } from '../../../../../core/services/objectives.service';
import { KeyResultsListComponent } from '../key-results-list/key-results-list.component';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';

@Component({
  selector: 'app-objectives-detail-card',
  standalone: true,
  imports: [CommonModule, KeyResultsListComponent, ProgressBarLinearComponent],
  templateUrl: './objectives-detail-card.component.html',
  styleUrls: ['./objectives-detail-card.component.scss']
})
export class ObjectivesDetailCardComponent {
  @Input() objective!: Objective;
  @Input() keyResults: KeyResult[] = [];
}
