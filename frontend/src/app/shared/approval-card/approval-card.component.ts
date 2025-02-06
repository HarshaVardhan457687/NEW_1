import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-approval-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './approval-card.component.html',
  styleUrls: ['./approval-card.component.scss']
})
export class ApprovalCardComponent {
  @Input() taskName: string = '';
  @Input() tag: string = '';
  @Input() priority: 'High' | 'Medium' | 'Low' = 'Medium';
  @Input() description: string = '';
  @Input() dueDate: string = '';
  @Input() owner: string = '';

  status: 'pending' | 'approved' | 'rejected' = 'pending';

  onApprove() {
    this.status = 'approved';
  }

  onReject() {
    this.status = 'rejected';
  }

  get priorityClass(): string {
    return `priority-${this.priority.toLowerCase()}`;
  }
}
