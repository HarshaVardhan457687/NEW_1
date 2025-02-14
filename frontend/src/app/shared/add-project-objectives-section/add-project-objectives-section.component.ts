import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormBuilder, FormArray, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { AddObjectiveCardComponent } from '../add-objective-card/add-objective-card.component';
import { AddKeyResultCardComponent } from '../add-key-result-card/add-key-result-card.component';

@Component({
  selector: 'app-add-project-objectives-section',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AddObjectiveCardComponent,
    AddKeyResultCardComponent
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
  savedTeams: any[] = [];

  constructor(private fb: FormBuilder) {
    this.objectiveForm = this.fb.group({
      objectives: this.fb.array([])
    });
  }

  ngOnInit() {
    // Subscribe to changes in the teams array
    this.projectForm.get('team')?.valueChanges.subscribe(teamData => {
      if (teamData && teamData.teams) {
        this.savedTeams = teamData.teams;
      }
    });

    // Initialize savedTeams with current value
    const teamData = this.projectForm.get('team')?.value;
    if (teamData && teamData.teams) {
      this.savedTeams = teamData.teams;
    }
  }

  get objectives() {
    return this.objectiveForm.get('objectives') as FormArray;
  }

  get currentObjective(): FormGroup {
    return this.objectives.at(this.objectives.length - 1) as FormGroup;
  }

  addObjective() {
    this.showObjectiveForm = true;
  }

  onObjectiveClose() {
    this.showObjectiveForm = false;
  }

  onObjectiveSave(objective: any) {
    this.savedObjectives.push({
      ...objective,
      keyResults: []
    });
    
    // Update the project form's objectives
    const objectives = this.projectForm.get('objectives')?.value || [];
    objectives.push(objective);
    this.projectForm.patchValue({ objectives });
  }

  openKeyResultModal(objectiveIndex: number) {
    this.currentObjectiveIndex = objectiveIndex;
    this.showKeyResultModal = true;
  }

  onKeyResultClose() {
    this.showKeyResultModal = false;
  }

  onKeyResultSave(keyResult: any) {
    if (this.currentObjectiveIndex !== null) {
      this.savedObjectives[this.currentObjectiveIndex].keyResults.push(keyResult);
      this.showKeyResultModal = false;
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