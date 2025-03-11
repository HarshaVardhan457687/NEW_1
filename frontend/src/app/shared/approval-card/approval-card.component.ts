import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskPriority } from '../../core/services/my-tasks.service';
import { ApprovalStatus } from '../../core/services/approvals.service';
import { ApprovalsService } from '../../core/services/approvals.service';

@Component({
  selector: 'app-approval-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './approval-card.component.html',
  styleUrls: ['./approval-card.component.scss']
})
export class ApprovalCardComponent {
  @Input() taskApprovalId!: number;
  @Input() taskName: string = '';
  @Input() tag: string = '';
  @Input() priority: TaskPriority = TaskPriority.MEDIUM;
  @Input() description: string = '';
  @Input() dueDate!: Date;
  @Input() owner: string = '';
  @Input() status: ApprovalStatus = ApprovalStatus.PENDING;
  @Output() statusChanged = new EventEmitter<void>();

  error: string | null = null;
  isProcessing = false;

  constructor(private approvalsService: ApprovalsService) {}

  onApprove() {
    if (this.isProcessing) return;
    this.isProcessing = true;
    this.error = null;

    this.approvalsService.approveTask(this.taskApprovalId).subscribe({
      next: () => {
        this.status = ApprovalStatus.APPROVED;
        this.isProcessing = false;
        this.statusChanged.emit();
      },
      error: (error) => {
        this.error = error.message || 'Failed to approve task';
        this.isProcessing = false;
      }
    });
  }

  onReject() {
    if (this.isProcessing) return;
    this.isProcessing = true;
    this.error = null;

    this.approvalsService.rejectTask(this.taskApprovalId).subscribe({
      next: () => {
        this.status = ApprovalStatus.REJECTED;
        this.isProcessing = false;
        this.statusChanged.emit();
      },
      error: (error) => {
        this.error = error.message || 'Failed to reject task';
        this.isProcessing = false;
      }
    });
  }

  get priorityClass(): string {
    return `priority-${this.priority.toLowerCase()}`;
  }

  get isPendingStatus(): boolean {
    return this.status === ApprovalStatus.PENDING;
  }

  get statusClass(): string {
    return `${this.status.toLowerCase()}`;
  }
}
