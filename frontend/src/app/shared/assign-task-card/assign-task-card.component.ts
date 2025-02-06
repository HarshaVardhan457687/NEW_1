import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalService } from '../../core/services/modal.service';

@Component({
  selector: 'app-assign-task-card',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './assign-task-card.component.html',
  styleUrl: './assign-task-card.component.scss'
})
export class AssignTaskCardComponent {
  taskForm: FormGroup;
  
  constructor(
    private fb: FormBuilder,
    private modalService: ModalService
  ) {
    this.taskForm = this.fb.group({
      taskName: ['', Validators.required],
      description: ['', Validators.required],
      tag: ['', Validators.required],
      keyResult: ['', Validators.required],
      priority: ['', Validators.required],
      dueDate: ['', Validators.required]
    });
  }

  onAssign() {
    if (this.taskForm.valid) {
      // Handle task assignment
      console.log(this.taskForm.value);
      this.modalService.close();
    }
  }

  onCancel() {
    this.modalService.close();
  }
}
