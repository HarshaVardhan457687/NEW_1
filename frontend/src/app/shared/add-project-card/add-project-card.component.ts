import { Component, EventEmitter, Output, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormArray } from '@angular/forms';
import { AddTeamCardComponent } from '../add-team-card/add-team-card.component';
import { TeamService, Team } from '../../core/services/team.service';
import { UserService, User } from '../../core/services/user.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-project-card',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AddTeamCardComponent,
    FormsModule
  ],
  templateUrl: './add-project-card.component.html',
  styleUrls: ['./add-project-card.component.scss']
})
export class AddProjectCardComponent implements OnInit {
  @Output() closeModal = new EventEmitter<void>();
  
  currentSection = 0;
  projectForm!: FormGroup;
  teams: Team[] = [];
  objectives!: FormArray;
  showObjectiveForm = false;
  showKeyResultModal = false;
  currentObjectiveIndex: number | null = null;
  savedObjectives: any[] = [];
  showTeamCard = false;

  // Team selection properties
  selectedLeader: User | null = null;
  selectedMembers: User[] = [];
  teamLeaders: User[] = [];
  teamMembers: User[] = [];
  showLeaderDropdown = false;
  showMemberDropdown = false;
  searchLeader = '';
  searchMember = '';

  constructor(
    private fb: FormBuilder,
    private teamService: TeamService,
    private userService: UserService
  ) {
    this.initializeForm();
  }

  ngOnInit() {
    this.teamService.getTeams().subscribe(teams => {
      this.teams = teams;
    });
    
    this.userService.getUsers().subscribe(users => {
      this.teamLeaders = users.filter(user => user.role === 'team_leader');
      this.teamMembers = users.filter(user => user.role === 'team_member');
    });
  }

  private initializeForm() {
    this.projectForm = this.fb.group({
      projectInfo: this.fb.group({
        name: ['', Validators.required],
        description: ['', Validators.required],
        dueDate: ['', Validators.required],
        priority: ['medium', Validators.required]
      }),
      team: this.fb.group({
        teamName: ['', Validators.required],
        selectedMembers: [[]]
      }),
      objectives: this.fb.array([])
    });

    this.objectives = this.projectForm.get('objectives') as FormArray;
  }

  nextSection() {
    if (this.currentSection < 2) {
      this.currentSection++;
      if (this.currentSection === 1) {
        this.showTeamCard = true;
      }
    }
  }

  previousSection() {
    if (this.currentSection > 0) {
      this.currentSection--;
      if (this.currentSection !== 1) {
        this.showTeamCard = false;
      }
    }
  }

  onTeamCardVisibilityChange(visible: boolean) {
    this.showTeamCard = visible;
    if (!visible && this.currentSection === 1) {
      // If team card is closed in team section, move to next section
      this.nextSection();
    }
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

  openKeyResultModal(objectiveIndex: number) {
    this.currentObjectiveIndex = objectiveIndex;
    this.showKeyResultModal = true;
  }

  addKeyResult() {
    if (this.currentObjectiveIndex !== null) {
      const keyResult = this.fb.group({
        name: ['', Validators.required],
        targetValue: ['', Validators.required],
        unit: ['', Validators.required],
        dueDate: ['', Validators.required],
        priority: ['medium', Validators.required],
        assignedTeam: ['', Validators.required]
      });
      this.savedObjectives[this.currentObjectiveIndex].keyResults.push(keyResult.value);
      this.showKeyResultModal = false;
      this.currentObjectiveIndex = null;
    }
  }

  onSubmit() {
    if (this.projectForm.valid) {
      const formValue = this.projectForm.value;
      formValue.objectives = this.savedObjectives;
      // TODO: Submit form data
      this.closeModal.emit();
    }
  }

  close() {
    this.closeModal.emit();
  }

  // Team selection methods
  selectLeader(leader: User) {
    this.selectedLeader = leader;
    this.showLeaderDropdown = false;
    this.searchLeader = '';
  }

  removeLeader() {
    this.selectedLeader = null;
  }

  selectMember(member: User) {
    if (!this.selectedMembers.find(m => m.id === member.id)) {
      this.selectedMembers.push(member);
    }
    this.searchMember = '';
  }

  removeMember(member: User) {
    this.selectedMembers = this.selectedMembers.filter(m => m.id !== member.id);
  }

  get filteredLeaders() {
    return this.teamLeaders.filter(leader => 
      leader.name.toLowerCase().includes(this.searchLeader.toLowerCase())
    );
  }

  get filteredMembers() {
    return this.teamMembers.filter(member => 
      member.name.toLowerCase().includes(this.searchMember.toLowerCase()) &&
      !this.selectedMembers.find(m => m.id === member.id)
    );
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    // Close leader dropdown if click is outside
    if (this.showLeaderDropdown) {
      const leaderContainer = document.querySelector('.leader-dropdown-container');
      if (leaderContainer && !leaderContainer.contains(event.target as Node)) {
        this.showLeaderDropdown = false;
      }
    }

    // Close member dropdown if click is outside
    if (this.showMemberDropdown) {
      const memberContainer = document.querySelector('.member-dropdown-container');
      if (memberContainer && !memberContainer.contains(event.target as Node)) {
        this.showMemberDropdown = false;
      }
    }
  }
}
