import { Component, EventEmitter, Output, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

export interface KeyResultFormData {
  name: string;
  targetValue: number;
  unit: string;
  dueDate: string;
  priority: 'high' | 'medium' | 'low';
  assignedTeam?: string;
}

@Component({
  selector: 'app-add-key-result-card',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-key-result-card.component.html',
  styleUrls: ['./add-key-result-card.component.scss']
})
export class AddKeyResultCardComponent {
  @Input() savedTeams: any[] = [];
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<KeyResultFormData>();

  keyResultForm!: FormGroup;

  constructor(private fb: FormBuilder) {
    this.initializeForm();
  }

  private initializeForm() {
    this.keyResultForm = this.fb.group({
      name: ['', Validators.required],
      targetValue: ['', Validators.required],
      unit: ['', Validators.required],
      dueDate: ['', Validators.required],
      priority: ['medium', Validators.required],
      assignedTeam: ['', Validators.required]
    });
  }

  onOverlayClick(event: MouseEvent): void {
    if ((event.target as HTMLElement).classList.contains('modal-overlay')) {
      this.closeForm();
    }
  }

  closeForm(): void {
    this.keyResultForm.reset({
      priority: 'medium'
    });
    this.close.emit();
  }

  onSubmit(): void {
    if (this.keyResultForm.valid) {
      this.save.emit(this.keyResultForm.value);
      this.closeForm();
    } else {
      this.markFormGroupTouched(this.keyResultForm);
      // Add animation class to form for visual feedback
      const formElement = document.querySelector('.key-result-modal');
      if (formElement) {
        formElement.classList.add('shake');
        setTimeout(() => {
          formElement.classList.remove('shake');
        }, 500);
      }
    }
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.keyResultForm.get(fieldName);
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