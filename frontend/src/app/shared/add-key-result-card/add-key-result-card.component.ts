import { Component, EventEmitter, Output, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AddKeyResultService, TeamSelectDTO } from '../../core/services/add-key-result.service';

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
export class AddKeyResultCardComponent implements OnInit {
  @Input() projectId!: number;
  @Input() objectiveId!: number;
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<KeyResultFormData>();

  keyResultForm!: FormGroup;
  teams: TeamSelectDTO[] = [];
  isLoadingTeams: boolean = false;

  constructor(
    private fb: FormBuilder,
    private addKeyResultService: AddKeyResultService
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.loadTeams();
  }

  private loadTeams(): void {
    this.isLoadingTeams = true;
    this.addKeyResultService.getTeamsForProject(this.projectId).subscribe({
      next: (teams) => {
        this.teams = teams;
        this.isLoadingTeams = false;
      },
      error: (error) => {
        console.error('Error loading teams:', error);
        this.isLoadingTeams = false;
      }
    });
  }

  private initializeForm() {
    this.keyResultForm = this.fb.group({
      name: ['', Validators.required],
      targetValue: ['', Validators.required],
      unit: ['', Validators.required],
      dueDate: ['', Validators.required],
      priority: ['MEDIUM', Validators.required],
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
      const formData = this.keyResultForm.value;
      const keyResultRequest = {
        keyResultName: formData.name,
        keyResultTargetVal: formData.targetValue,
        keyResultUnit: formData.unit,
        keyResultDueDate: formData.dueDate,
        keyResultPriority: formData.priority.toUpperCase(),
        associatedObjectiveId: this.objectiveId,
        teamId: parseInt(formData.assignedTeam)
      };

      this.addKeyResultService.createKeyResult(keyResultRequest).subscribe({
        next: (response) => {
          this.save.emit(formData);
          this.closeForm();
        },
        error: (error) => {
          console.error('Error creating key result:', error);
          // Add animation class to form for visual feedback
          const formElement = document.querySelector('.key-result-modal');
          if (formElement) {
            formElement.classList.add('shake');
            setTimeout(() => {
              formElement.classList.remove('shake');
            }, 500);
          }
        }
      });
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