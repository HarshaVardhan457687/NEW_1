import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormArray, AbstractControl } from '@angular/forms';
import { AddProjectGeneralSectionComponent } from '../add-project-general-section/add-project-general-section.component';
import { AddProjectTeamSectionComponent } from '../add-project-team-section/add-project-team-section.component';
import { AddProjectObjectivesSectionComponent } from '../add-project-objectives-section/add-project-objectives-section.component';

@Component({
  selector: 'app-add-project-card',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AddProjectGeneralSectionComponent,
    AddProjectTeamSectionComponent,
    AddProjectObjectivesSectionComponent
  ],
  templateUrl: './add-project-card.component.html',
  styleUrls: ['./add-project-card.component.scss']
})
export class AddProjectCardComponent implements OnInit {
  @Output() closeModal = new EventEmitter<void>();
  
  currentSection = 0;
  projectForm!: FormGroup;

  constructor(private fb: FormBuilder) {
    this.initializeForm();
  }

  ngOnInit() {
    // Subscribe to form value changes for debugging
    this.projectForm.get('projectInfo')?.valueChanges.subscribe(val => {
      console.log('Project Info Values:', val);
      console.log('Project Info Valid:', this.projectForm.get('projectInfo')?.valid);
    });
  }

  private initializeForm() {
    this.projectForm = this.fb.group({
      projectInfo: this.fb.group({
        name: ['', Validators.required],
        description: [''],
        dueDate: ['', Validators.required],
        priority: ['medium', Validators.required]
      }),
      team: this.fb.group({
        teams: [[]]  // Changed to match the actual data structure
      }),
      objectives: this.fb.array([])
    });
  }

  validateSection(section: number): boolean {
    if (section === 0) {
      const projectInfo = this.projectForm.get('projectInfo');
      console.log('Validating Project Info:', {
        value: projectInfo?.value,
        valid: projectInfo?.valid,
        errors: projectInfo?.errors,
        touched: projectInfo?.touched,
        dirty: projectInfo?.dirty
      });

      if (projectInfo && !projectInfo.valid) {
        Object.keys((projectInfo as FormGroup).controls).forEach(key => {
          const control = projectInfo.get(key);
          if (control) {
            control.markAsTouched();
            console.log(`Field ${key}:`, {
              value: control.value,
              valid: control.valid,
              errors: control.errors,
              touched: control.touched
            });
          }
        });
        return false;
      }
      return true;
    } else if (section === 1) {
      // For team section, we'll always return true since teams are optional
      return true;
    }
    return true;
  }

  nextSection() {
    console.log('Next button clicked. Current Section:', this.currentSection);
    if (this.validateSection(this.currentSection)) {
      if (this.currentSection < 2) {
        this.currentSection++;
        console.log('Moving to section:', this.currentSection);
      }
    }
  }

  previousSection() {
    if (this.currentSection > 0) {
      this.currentSection--;
      console.log('Moving back to section:', this.currentSection);
    }
  }

  onSubmit() {
    if (this.projectForm.valid) {
      console.log('Form submitted:', this.projectForm.value);
      this.close();
    }
  }

  close() {
    this.closeModal.emit();
  }
}

