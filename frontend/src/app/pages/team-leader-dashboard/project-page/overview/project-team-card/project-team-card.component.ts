import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-project-team-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './project-team-card.component.html',
  styleUrl: './project-team-card.component.scss'
})
export class ProjectTeamCardComponent {
  @Input() projectId!: number;
}
