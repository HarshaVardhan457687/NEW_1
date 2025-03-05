import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AssignTaskService, KeyResult } from '../../core/services/assign-task.service';

@Component({
  selector: 'app-assign-task-card',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './assign-task-card.component.html',
  styleUrl: './assign-task-card.component.scss'
})
export class AssignTaskCardComponent {
  @Input() projectId!: number;
  @Input() userId!: number;

  isOpen = false;
  taskForm: FormGroup;
  keyResults: KeyResult[] = [];
  isLoading = false;
  errorMessage = '';
  
  constructor(
    private fb: FormBuilder,
    private assignTaskService: AssignTaskService
  ) {
    this.taskForm = this.fb.group({
      taskName: ['', Validators.required],
      description: ['', Validators.required],
      tag: ['', Validators.required],
      keyResult: [{ value: '', disabled: false }, Validators.required],
      priority: ['', Validators.required],
      dueDate: ['', Validators.required]
    });
  }

  open() {
    this.isOpen = true;
    console.log("[AssignTaskCardComponent] projectId:", this.projectId);
    console.log("[AssignTaskCardComponent] userId:", this.userId);
    if (this.projectId) {
      this.loadKeyResults();
    }
  }

  close() {
    this.isOpen = false;
  }

  private loadKeyResults() {
    this.taskForm.get('keyResult')?.disable();
    this.isLoading = true;

    this.assignTaskService.getKeyResults(this.projectId).subscribe({
      next: (results) => {
        this.keyResults = results;
        this.taskForm.get('keyResult')?.enable();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading key results:', error);
        this.errorMessage = 'Failed to load key results';
        this.isLoading = false;
        this.taskForm.get('keyResult')?.disable();
      }
    });
  }

  onAssign() {
    if (this.taskForm.valid) {
      const formValue = this.taskForm.value;
      
      const task = {
        taskHeading: formValue.taskName,
        taskDescription: formValue.description,
        taskOwner: this.userId,
        taskDueDate: formValue.dueDate,
        taskTag: formValue.tag,
        taskIsActive: true,
        taskAssociatedProject: this.projectId,
        taskAssociatedKeyResult: formValue.keyResult,
        taskStatus: 'PENDING' as 'PENDING' | 'WAITING_FOR_APPROVAL' | 'COMPLETED',
        taskPriority: formValue.priority.toUpperCase()
      };

      this.isLoading = true;
      this.assignTaskService.createTask(task).subscribe({
        next: (createdTask) => {
          console.log('Task created successfully:', createdTask);
          this.isLoading = false;
          this.close();
        },
        error: (error) => {
          console.error('Error creating task:', error);
          this.errorMessage = 'Failed to create task';
          this.isLoading = false;
        }
      });
    }
  }

  onCancel() {
    this.close();
  }
}
