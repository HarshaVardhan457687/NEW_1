import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AddObjectiveService } from '../../core/services/add-objective.service';

export interface ObjectiveFormData {
  name: string;
  dueDate: string;
  priority: 'high' | 'medium' | 'low';
}

@Component({
  selector: 'app-add-objective-card',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-objective-card.component.html',
  styleUrls: ['./add-objective-card.component.scss']
})
export class AddObjectiveCardComponent {
  @Input() projectId!: number;
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<ObjectiveFormData>();

  objectiveForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private addObjectiveService: AddObjectiveService
  ) {
    this.objectiveForm = this.fb.group({
      name: ['', [Validators.required]],
      dueDate: ['', [Validators.required]],
      priority: ['medium', [Validators.required]]
    });
  }

  onOverlayClick(event: MouseEvent): void {
    if ((event.target as HTMLElement).classList.contains('modal-overlay')) {
      this.closeForm();
    }
  }

  closeForm(): void {
    this.close.emit();
  }

  onSubmit(): void {
    if (this.objectiveForm.valid) {
      const formData = this.objectiveForm.value;
      const objectiveRequest = {
        objectiveName: formData.name,
        objectiveDueDate: formData.dueDate,
        objectivePriority: formData.priority.toUpperCase(),
        mappedProject: this.projectId
      };

      this.addObjectiveService.createObjective(objectiveRequest).subscribe({
        next: (response) => {
          this.save.emit(formData);
          this.closeForm();
        },
        error: (error) => {
          console.error('Error creating objective:', error);
        }
      });
    } else {
      this.markFormGroupTouched(this.objectiveForm);
    }
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.objectiveForm.get(fieldName);
    return field ? field.invalid && (field.dirty || field.touched) : false;
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
} 