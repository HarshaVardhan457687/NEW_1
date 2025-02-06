import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApprovalCardComponent } from '../approval-card/approval-card.component';
import { ProjectService } from '../../core/services/projects.service';

@Component({
  selector: 'app-approvals-section',
  standalone: true,
  imports: [CommonModule, ApprovalCardComponent],
  templateUrl: './approvals-section.component.html',
  styleUrls: ['./approvals-section.component.scss']
})
export class ApprovalsSectionComponent implements OnInit {
  approvals: any[] = [];

  constructor(private projectService: ProjectService) {}

  ngOnInit() {
    this.projectService.getTasks().subscribe(tasks => {
      // Convert tasks to approval format
      this.approvals = tasks.map(task => ({
        title: task.title,
        tag: task.tag || 'Development',
        priority: this.getPriority(task.priority),
        description: task.description || 'No description provided',
        dueDate: task.dueDate,
        owner: task.assignee || 'Unassigned'
      }));
    });
  }

  private getPriority(priority: number): 'High' | 'Medium' | 'Low' {
    if (priority >= 7) return 'High';
    if (priority >= 4) return 'Medium';
    return 'Low';
  }
}
