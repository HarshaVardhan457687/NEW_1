import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddTeamCardComponent } from '../../../../../shared/add-team-card/add-team-card.component';

@Component({
  selector: 'app-project-team-card',
  standalone: true,
  imports: [CommonModule, AddTeamCardComponent],
  templateUrl: './project-team-card.component.html',
  styleUrl: './project-team-card.component.scss'
})
export class ProjectTeamCardComponent {
  @Input() projectId!: number;
  showAddTeamCard = false;

  onAddTeam() {
    this.showAddTeamCard = true;
  }
}
