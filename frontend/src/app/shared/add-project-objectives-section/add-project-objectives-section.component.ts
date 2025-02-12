import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormBuilder, FormArray, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-add-project-objectives-section',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './add-project-objectives-section.component.html',
  styleUrls: ['./add-project-objectives-section.component.scss']
})
export class AddProjectObjectivesSectionComponent implements OnInit {
  @Input() projectForm!: FormGroup;
  objectiveForm: FormGroup;
  showObjectiveForm = false;
  showKeyResultModal = false;
  currentObjectiveIndex: number | null = null;
  savedObjectives: any[] = [];
  keyResultForm!: FormGroup;

  constructor(private fb: FormBuilder) {
    this.initializeKeyResultForm();
    this.objectiveForm = this.fb.group({
      objectives: this.fb.array([])
    });
  }

  ngOnInit() {
    // No need to modify projectForm directly
    console.log('Form initialized');
  }

  private initializeKeyResultForm() {
    this.keyResultForm = this.fb.group({
      name: ['', Validators.required],
      targetValue: ['', Validators.required],
      unit: ['', Validators.required],
      dueDate: ['', Validators.required],
      priority: ['medium', Validators.required],
      assignedTeam: ['']
    });
  }

  get objectives() {
    return this.objectiveForm.get('objectives') as FormArray;
  }

  get currentObjective(): FormGroup {
    return this.objectives.at(this.objectives.length - 1) as FormGroup;
  }

  addObjective() {
    const objective = this.fb.group({
      name: ['', Validators.required],
      dueDate: ['', Validators.required],
      priority: ['medium', Validators.required],
      keyResults: this.fb.array([])
    });

    this.objectives.push(objective);
    this.showObjectiveForm = true;

    // Set default priority after a short delay to ensure form is initialized
    setTimeout(() => {
      objective.patchValue({ priority: 'medium' });
      console.log('Priority set to:', objective.get('priority')?.value);
    });
  }

  cancelObjective() {
    if (this.objectives.length > 0) {
      this.objectives.removeAt(this.objectives.length - 1);
    }
    this.showObjectiveForm = false;
  }

  saveObjective(index: number) {
    const objective = this.objectives.at(index) as FormGroup;
    
    // Log form values
    console.log('Saving objective with values:', {
      name: objective.get('name')?.value,
      dueDate: objective.get('dueDate')?.value,
      priority: objective.get('priority')?.value
    });

    // Mark all fields as touched to trigger validation
    Object.keys(objective.controls).forEach(key => {
      const control = objective.get(key);
      if (control) {
        control.markAsTouched();
        control.updateValueAndValidity();
      }
    });

    // Check individual field validity
    const nameValid = objective.get('name')?.valid;
    const dueDateValid = objective.get('dueDate')?.valid;
    const priorityValid = objective.get('priority')?.valid;

    console.log('Field validation:', {
      nameValid,
      dueDateValid,
      priorityValid,
      formValid: objective.valid
    });

    if (nameValid && dueDateValid && priorityValid) {
      // Update the project form's objectives
      const objectives = this.projectForm.get('objectives')?.value || [];
      objectives.push(objective.value);
      this.projectForm.patchValue({ objectives });

      // Update local saved objectives
      this.savedObjectives.push({
        ...objective.value,
        keyResults: []
      });
      
      this.objectives.removeAt(index);
      this.showObjectiveForm = false;
    } else {
      console.log('Form validation failed:', {
        nameErrors: objective.get('name')?.errors,
        dueDateErrors: objective.get('dueDate')?.errors,
        priorityErrors: objective.get('priority')?.errors
      });
    }
  }

  openKeyResultModal(objectiveIndex: number) {
    this.currentObjectiveIndex = objectiveIndex;
    this.showKeyResultModal = true;
  }

  closeKeyResultModal() {
    this.showKeyResultModal = false;
    this.keyResultForm.reset({ priority: 'medium' });
  }

  addKeyResult() {
    if (this.keyResultForm.valid && this.currentObjectiveIndex !== null) {
      const keyResult = this.keyResultForm.value;
      this.savedObjectives[this.currentObjectiveIndex].keyResults.push(keyResult);
      this.closeKeyResultModal();
    } else {
      Object.keys(this.keyResultForm.controls).forEach(key => {
        const control = this.keyResultForm.get(key);
        if (control) {
          control.markAsTouched();
          control.updateValueAndValidity();
        }
      });
    }
  }

  // Helper methods for template
  isFieldInvalid(fieldName: string): boolean {
    const control = this.currentObjective.get(fieldName);
    return !!control && control.invalid && control.touched;
  }

  getFieldError(fieldName: string): string {
    const control = this.currentObjective.get(fieldName);
    if (control?.errors?.['required']) {
      return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
    }
    return '';
  }
} 