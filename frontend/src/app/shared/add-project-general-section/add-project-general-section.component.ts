import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, ReactiveFormsModule, AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-add-project-general-section',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './add-project-general-section.component.html',
  styleUrls: ['./add-project-general-section.component.scss']
})
export class AddProjectGeneralSectionComponent {
  @Input() projectForm!: FormGroup;

  get projectInfo(): FormGroup {
    return this.projectForm.get('projectInfo') as FormGroup;
  }

  getControl(name: string): AbstractControl | null {
    return this.projectInfo.get(name);
  }

  isFieldInvalid(name: string): boolean {
    const control = this.getControl(name);
    return !!control && control.invalid && control.touched;
  }
} 