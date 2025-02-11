import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormBuilder, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';

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
export class AddProjectObjectivesSectionComponent {
  @Input() projectForm!: FormGroup;

  showObjectiveForm = false;
  showKeyResultModal = false;
  currentObjectiveIndex: number | null = null;
  savedObjectives: any[] = [];
  keyResultForm!: FormGroup;

  constructor(private fb: FormBuilder) {
    this.initializeKeyResultForm();
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
    return this.projectForm.get('objectives') as FormArray;
  }

  addObjective() {
    this.showObjectiveForm = true;
    const objective = this.fb.group({
      name: ['', Validators.required],
      dueDate: ['', Validators.required],
      priority: ['medium', Validators.required],
      keyResults: this.fb.array([])
    });
    this.objectives.push(objective);
  }

  saveObjective(index: number) {
    if (this.validateObjective(index)) {
      const objective = this.objectives.at(index);
      if (objective.valid) {
        this.savedObjectives.push({
          ...objective.value,
          keyResults: []
        });
        this.objectives.removeAt(index);
        this.showObjectiveForm = false;
      }
    }
  }

  validateObjective(index: number): boolean {
    const objective = this.objectives.at(index);
    let isValid = true;

    if (objective.get('name')?.invalid) {
      objective.get('name')?.markAsTouched();
      isValid = false;
    }
    if (objective.get('dueDate')?.invalid) {
      objective.get('dueDate')?.markAsTouched();
      isValid = false;
    }
    if (objective.get('priority')?.invalid) {
      objective.get('priority')?.markAsTouched();
      isValid = false;
    }

    return isValid;
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
    }
  }
} 