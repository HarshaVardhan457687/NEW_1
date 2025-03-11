import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApprovalCardComponent } from '../approval-card/approval-card.component';
import { ApprovalsService, TaskApprovalResponseDTO, ApprovalStatus } from '../../core/services/approvals.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-approvals-section',
  standalone: true,
  imports: [CommonModule, ApprovalCardComponent],
  templateUrl: './approvals-section.component.html',
  styleUrls: ['./approvals-section.component.scss']
})
export class ApprovalsSectionComponent implements OnInit {
  approvals: TaskApprovalResponseDTO[] = [];
  isLoading = true;
  error?: string;

  constructor(
    private approvalsService: ApprovalsService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.parent?.params.subscribe(params => {
      const projectId = Number(params['id']);
      
      if (!projectId) {
        this.error = 'Project ID is required';
        this.isLoading = false;
        return;
      }

      this.approvalsService.getApprovals(projectId).subscribe({
        next: (approvals) => {
          this.approvals = this.sortApprovals(approvals);
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading approvals:', error);
          this.error = error.message || 'Failed to load approvals';
          this.isLoading = false;
        }
      });
    });
  }

  private sortApprovals(approvals: TaskApprovalResponseDTO[]): TaskApprovalResponseDTO[] {
    const statusOrder = {
      [ApprovalStatus.PENDING]: 0,
      [ApprovalStatus.APPROVED]: 1,
      [ApprovalStatus.REJECTED]: 2
    };

    return [...approvals].sort((a, b) => 
      statusOrder[a.approvalStatus] - statusOrder[b.approvalStatus]
    );
  }

  onStatusChanged() {
    this.ngOnInit();
  }
}
