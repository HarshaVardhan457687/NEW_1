import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CurrentTaskCardComponent } from '../current-task-card/current-task-card.component';
import { TaskService, Task } from '../../../core/services/tasks.service';
import { ProjectService } from '../../../core/services/projects.service';
import { map, switchMap } from 'rxjs';

@Component({
  selector: 'app-current-tasks-section',
  standalone: true,
  imports: [CommonModule, CurrentTaskCardComponent],
  templateUrl: './current-tasks-section.component.html',
  styleUrl: './current-tasks-section.component.scss'
})
export class CurrentTasksSectionComponent implements OnInit {
  tasks: Task[] = [];

  constructor(
    private taskService: TaskService,
    private projectService: ProjectService
  ) {}

  ngOnInit() {
    this.taskService.getTasks().pipe(
      switchMap(tasks => 
        this.projectService.getProjects().pipe(
          map(projects => tasks.map(task => ({
            ...task,
            project: projects.find(p => p.id === task.projectId)?.title || ''
          })))
        )
      )
    ).subscribe(
      tasks => this.tasks = tasks
    );
  }
}
