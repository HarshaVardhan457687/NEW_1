import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CurrentTaskCardComponent } from '../current-task-card/current-task-card.component';
import { CurrentTasksSectionService, ActiveTask } from '../../core/services/current-tasks-section.service';
import { UserRole } from '../../core/services/role-selection.service';

@Component({
  selector: 'app-current-tasks-section',
  standalone: true,
  imports: [CommonModule, CurrentTaskCardComponent],
  templateUrl: './current-tasks-section.component.html',
  styleUrl: './current-tasks-section.component.scss'
})
export class CurrentTasksSectionComponent implements OnInit {
  @Input() role!: UserRole;
  tasks: ActiveTask[] = [];
  loading = true;
  error = false;

  constructor(private currentTasksService: CurrentTasksSectionService) {}

  ngOnInit() {
    const userEmail = localStorage.getItem('username') || '';
    
    this.currentTasksService.getActiveTasks(userEmail, this.role)
      .subscribe({
        next: (tasks) => {
          this.tasks = tasks;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading active tasks:', error);
          this.tasks = [];
          this.loading = false;
          this.error = true;
        },
        complete: () => {
          this.loading = false;
        }
      });
  }
}
