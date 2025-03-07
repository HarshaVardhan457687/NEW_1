import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AddProjectService } from '../../core/services/add-project.service';
import { AddProjectGeneralSectionComponent } from '../add-project-general-section/add-project-general-section.component';

@Component({
  selector: 'app-add-project-card',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule,
    AddProjectGeneralSectionComponent
  ],
  templateUrl: './add-project-card.component.html',
  styleUrls: ['./add-project-card.component.scss']
})
export class AddProjectCardComponent {
  @Output() closeModal = new EventEmitter<void>();
  projectForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private addProjectService: AddProjectService
  ) {
    this.projectForm = this.fb.group({
      projectInfo: this.fb.group({
        projectName: ['', Validators.required],
        projectDescription: [''],
        projectDueDate: ['', Validators.required],
        projectPriority: ['MEDIUM', Validators.required]
      })
    });
  }

  onSubmit() {
    const projectInfo = this.projectForm.get('projectInfo') as FormGroup;
    if (projectInfo) {
      // Mark all fields as touched to trigger validation styling
      Object.keys(projectInfo.controls).forEach(key => {
        const control = projectInfo.get(key);
        control?.markAsTouched();
      });

      if (this.projectForm.valid) {
        this.addProjectService.createProject(projectInfo.value).subscribe({
          next: () => {
            this.close();
          },
          error: (error) => {
            console.error('Error creating project:', error);
          }
        });
      }
    }
  }

  close() {
    this.closeModal.emit();
  }
}

