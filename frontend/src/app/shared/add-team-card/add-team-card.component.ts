import { Component, Input, Output, EventEmitter, OnInit, HostListener, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AddTeamService, UserSummary } from '../../core/services/add-team.service';

@Component({
  selector: 'app-add-team-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-team-card.component.html',
  styleUrl: './add-team-card.component.scss'
})
export class AddTeamCardComponent implements OnInit {
  // Make console available to template
  console = console;

  @Input() projectId!: number;

  @Input() set show(value: boolean) {
    console.log('Show value changed:', value);
    this._show = value;
    if (value && this.editingTeam) {
      this.prefillForm(this.editingTeam);
    }
    if (value) {
      this.loadUsers();
    }
    if (!value) {
      this.resetForm();
    }
    this.showChange.emit(value);
  }
  get show(): boolean {
    return this._show;
  }
  private _show = false;

  @Input() set editingTeam(team: any) {
    if (team && this.show) {
      this.prefillForm(team);
    }
  }

  @Output() showChange = new EventEmitter<boolean>();
  @Output() teamSaved = new EventEmitter<any>();

  teamName = '';
  selectedLeader: UserSummary | null = null;
  selectedMembers: UserSummary[] = [];
  
  teamLeaders: UserSummary[] = [];
  teamMembers: UserSummary[] = [];
  
  showLeaderDropdown = false;
  showMemberDropdown = false;
  searchLeader = '';
  searchMember = '';

  // Add validation properties
  isTeamNameTouched = false;
  isLeaderTouched = false;
  isMembersTouched = false;

  constructor(
    private addTeamService: AddTeamService,
    private elementRef: ElementRef
  ) {}

  ngOnInit() {
    console.log('AddTeamCardComponent initialized, show:', this.show);
    if (this.show) {
      this.loadUsers();
    }
  }

  private loadUsers() {
    this.addTeamService.getAllUsersSummary().subscribe({
      next: (users) => {
        // We'll need to add role filtering once it's added to UserSummary
        this.teamLeaders = users;
        this.teamMembers = users;
      },
      error: (err) => {
        console.error('Error loading users:', err);
      }
    });
  }

  private prefillForm(team: any) {
    this.teamName = team.name;
    this.selectedLeader = team.leader;
    this.selectedMembers = [...team.members];
    this.isTeamNameTouched = true;
    this.isLeaderTouched = true;
    this.isMembersTouched = true;
  }

  close() {
    console.log('Closing card');
    this.show = false;
    this.showChange.emit(false);
    this.resetForm();
  }

  resetForm() {
    this.teamName = '';
    this.selectedLeader = null;
    this.selectedMembers = [];
    this.searchLeader = '';
    this.searchMember = '';
    // Reset validation states
    this.isTeamNameTouched = false;
    this.isLeaderTouched = false;
    this.isMembersTouched = false;
  }

  selectLeader(leader: UserSummary) {
    this.selectedLeader = leader;
    this.showLeaderDropdown = false;
    this.searchLeader = '';
    this.isLeaderTouched = true;
  }

  removeLeader() {
    this.selectedLeader = null;
    this.isLeaderTouched = true;
  }

  selectMember(member: UserSummary) {
    if (!this.selectedMembers.find(m => m.userId === member.userId)) {
      this.selectedMembers.push(member);
      this.isMembersTouched = true;
    }
    this.searchMember = '';
  }

  removeMember(member: UserSummary) {
    this.selectedMembers = this.selectedMembers.filter(m => m.userId !== member.userId);
    this.isMembersTouched = true;
  }

  onSave() {
    const team = {
      name: this.teamName,
      leader: this.selectedLeader,
      members: this.selectedMembers
    };
    this.teamSaved.emit(team);
    this.close();
  }

  validateAndSave() {
    // Mark all fields as touched to trigger validation
    this.isTeamNameTouched = true;
    this.isLeaderTouched = true;
    this.isMembersTouched = true;

    if (this.isTeamNameInvalid || this.isLeaderInvalid || this.isMembersInvalid) {
      // Trigger jiggle animation
      const card = this.elementRef.nativeElement.querySelector('.add-team-card');
      if (card) {
        card.classList.remove('jiggle');
        void card.offsetWidth; // Force reflow
        card.classList.add('jiggle');
      }
      return;
    }

    // If validation passes, save the team
    this.onSave();
  }

  get isTeamNameInvalid(): boolean {
    return !this.teamName || this.teamName.trim().length === 0;
  }

  get isLeaderInvalid(): boolean {
    return !this.selectedLeader;
  }

  get isMembersInvalid(): boolean {
    return this.selectedMembers.length === 0;
  }

  get filteredLeaders() {
    return this.teamLeaders.filter(leader => 
      leader.userName.toLowerCase().includes(this.searchLeader.toLowerCase())
    );
  }

  get filteredMembers() {
    return this.teamMembers.filter(member => 
      member.userName.toLowerCase().includes(this.searchMember.toLowerCase()) &&
      !this.selectedMembers.find(m => m.userId === member.userId)
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
