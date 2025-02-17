import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApprovalsSectionComponent } from '../../../../shared/approvals-section/approvals-section.component';

@Component({
  selector: 'app-approvals-page',
  standalone: true,
  imports: [CommonModule, ApprovalsSectionComponent],
  templateUrl: './approvals-page.component.html',
  styleUrls: ['./approvals-page.component.scss']
})
export class ApprovalsPageComponent {
}
