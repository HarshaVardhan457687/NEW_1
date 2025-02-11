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
  keyResultForm!: FormGroup;

  // Team selection properties
  selectedLeader: User | null = null;
  selectedMembers: User[] = [];
  teamLeaders: User[] = [];
  teamMembers: User[] = [];
  showLeaderDropdown = false;
  showMemberDropdown = false;
  searchLeader = '';
  searchMember = '';

  // Team management
  teamForm!: FormGroup;
  savedTeams: any[] = [];
  showTeamModal = false;
  editingTeamIndex: number | null = null;

  constructor(
    private fb: FormBuilder,
    private teamService: TeamService,
    private userService: UserService
  ) {
    this.initializeForm();
    this.initializeKeyResultForm();
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

  validateSection(section: number): boolean {
    let isValid = true;
    const form = this.projectForm;

    if (section === 0) {
      const projectInfo = form.get('projectInfo');
      if (projectInfo?.get('name')?.invalid) {
        projectInfo.get('name')?.markAsTouched();
        isValid = false;
      }
      if (projectInfo?.get('dueDate')?.invalid) {
        projectInfo.get('dueDate')?.markAsTouched();
        isValid = false;
      }
      if (projectInfo?.get('priority')?.invalid) {
        projectInfo.get('priority')?.markAsTouched();
        isValid = false;
      }
    } else if (section === 1) {
      const team = form.get('team');
      if (team?.get('teamName')?.invalid) {
        team.get('teamName')?.markAsTouched();
        isValid = false;
      }
      if (!this.selectedLeader) {
        team?.get('leader')?.markAsTouched();
        isValid = false;
      }
    }

    if (!isValid) {
      const card = document.querySelector('.add-project-card') as HTMLElement;
      card?.classList.remove('jiggle');
      void card?.offsetWidth;
      card?.classList.add('jiggle');
    }

    return isValid;
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

    if (!isValid) {
      const card = document.querySelector('.objective') as HTMLElement;
      card?.classList.remove('jiggle');
      void card?.offsetWidth;
      card?.classList.add('jiggle');
    }

    return isValid;
  }

  validateKeyResult(): boolean {
    const keyResult = this.projectForm.get('keyResults.' + this.currentObjectiveIndex);
    let isValid = true;

    if (keyResult?.get('name')?.invalid) {
      keyResult.get('name')?.markAsTouched();
      isValid = false;
    }
    if (keyResult?.get('targetValue')?.invalid) {
      keyResult.get('targetValue')?.markAsTouched();
      isValid = false;
    }
    if (keyResult?.get('unit')?.invalid) {
      keyResult.get('unit')?.markAsTouched();
      isValid = false;
    }
    if (keyResult?.get('dueDate')?.invalid) {
      keyResult.get('dueDate')?.markAsTouched();
      isValid = false;
    }
    if (keyResult?.get('priority')?.invalid) {
      keyResult.get('priority')?.markAsTouched();
      isValid = false;
    }

    if (!isValid) {
      const modal = document.querySelector('.modal-content') as HTMLElement;
      modal?.classList.remove('jiggle');
      void modal?.offsetWidth;
      modal?.classList.add('jiggle');
    }

    return isValid;
  }

  nextSection() {
    if (this.validateSection(this.currentSection)) {
      if (this.currentSection < 2) {
        this.currentSection++;
        if (this.currentSection === 1) {
          this.showTeamCard = true;
        }
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

  openKeyResultModal(objectiveIndex: number) {
    this.currentObjectiveIndex = objectiveIndex;
    this.showKeyResultModal = true;
    this.keyResultForm.reset({
      priority: 'medium'
    });
  }

  closeKeyResultModal() {
    this.showKeyResultModal = false;
    this.currentObjectiveIndex = null;
    this.keyResultForm.reset();
  }

  addKeyResult() {
    if (this.keyResultForm.valid && this.currentObjectiveIndex !== null) {
      this.savedObjectives[this.currentObjectiveIndex].keyResults.push(this.keyResultForm.value);
      this.closeKeyResultModal();
    } else {
      // Mark all fields as touched to trigger validation display
      Object.keys(this.keyResultForm.controls).forEach(key => {
        const control = this.keyResultForm.get(key);
        control?.markAsTouched();
      });

      // Trigger jiggle animation
      const modal = document.querySelector('.modal-content') as HTMLElement;
      modal?.classList.remove('jiggle');
      void modal?.offsetWidth;
      modal?.classList.add('jiggle');
    }
  }

  onSubmit() {
    if (this.validateSection(2) && this.projectForm.valid) {
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
