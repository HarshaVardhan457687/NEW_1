import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyTaskCardComponent } from '../my-task-card/my-task-card.component';
import { TaskDetailsDTO } from '../../core/services/my-tasks.service';

@Component({
  selector: 'app-my-tasks-section',
  standalone: true,
  imports: [CommonModule, MyTaskCardComponent],
  templateUrl: './my-tasks-section.component.html',
  styleUrl: './my-tasks-section.component.scss'
})
export class MyTasksSectionComponent {
  @Input() tasks: TaskDetailsDTO[] = [];
}
