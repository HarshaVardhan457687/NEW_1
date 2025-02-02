import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeyResult } from '../../../../../core/services/objectives.service';
import { KeyResultDetailComponent } from '../key-result-detail/key-result-detail.component';

@Component({
  selector: 'app-key-results-list',
  standalone: true,
  imports: [CommonModule, KeyResultDetailComponent],
  templateUrl: './key-results-list.component.html',
  styleUrls: ['./key-results-list.component.scss']
})
export class KeyResultsListComponent {
  @Input() keyResults: KeyResult[] = [];
}
