import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, FormGroup } from '@angular/forms';
import { UserService, User } from '../../core/services/user.service';
import { AddTeamCardComponent } from '../add-team-card/add-team-card.component';

@Component({
  selector: 'app-add-project-team-section',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    AddTeamCardComponent
  ],
  templateUrl: './add-project-team-section.component.html',
  styleUrls: ['./add-project-team-section.component.scss']
})
export class AddProjectTeamSectionComponent {
  @Input() projectForm!: FormGroup;
  showTeamCard = false;
  savedTeams: any[] = [];
  editingTeamIndex: number | null = null;

  constructor(private userService: UserService) {
    console.log('AddProjectTeamSectionComponent initialized');
  }

  addTeam() {
    this.editingTeamIndex = null;
    console.log('addTeam called, current showTeamCard:', this.showTeamCard);
    this.showTeamCard = true;
    console.log('showTeamCard set to:', this.showTeamCard);
    setTimeout(() => {
      console.log('After timeout, showTeamCard:', this.showTeamCard);
    }, 0);
  }

  editTeam(index: number) {
    this.editingTeamIndex = index;
    this.showTeamCard = true;
  }

  onShowTeamCardChange(value: boolean) {
    console.log('onShowTeamCardChange:', value);
    this.showTeamCard = value;
    if (!value) {
      this.editingTeamIndex = null;
    }
  }

  deleteTeam(index: number) {
    this.savedTeams.splice(index, 1);
    this.updateFormValue();
  }

  onTeamSaved(team: any) {
    console.log('Team saved:', team);
    if (this.editingTeamIndex !== null) {
      this.savedTeams[this.editingTeamIndex] = team;
      this.editingTeamIndex = null;
    } else {
      this.savedTeams.push(team);
    }
    this.updateFormValue();
    this.showTeamCard = false;
  }

  private updateFormValue() {
    if (this.projectForm) {
      this.projectForm.get('team')?.patchValue({
        teams: this.savedTeams
      });
    }
  }
} 