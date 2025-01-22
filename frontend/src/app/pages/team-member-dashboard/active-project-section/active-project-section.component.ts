import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActiveProjectsCardComponent } from '../active-projects-card/active-projects-card.component';

@Component({
  selector: 'app-active-project-section',
  standalone: true,
  imports: [CommonModule, ActiveProjectsCardComponent],
  templateUrl: './active-project-section.component.html',
  styleUrl: './active-project-section.component.scss'
})
export class ActiveProjectSectionComponent {
  projects = [
    { name: 'Project Alpha', objectives: 4, progress: 75 },
    { name: 'Project Beta', objectives: 6, progress: 45 },
    { name: 'Project Gamma', objectives: 3, progress: 15 },
    { name: 'Project Delta', objectives: 5, progress: 85 },
    { name: 'Project Epsilon', objectives: 8, progress: 25 },
    { name: 'Project Zeta', objectives: 4, progress: 65 },
    { name: 'Project Eta', objectives: 7, progress: 10 }
  ];
}
