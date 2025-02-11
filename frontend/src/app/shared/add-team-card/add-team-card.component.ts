import { Component, Input, Output, EventEmitter, OnInit, HostListener, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService, User } from '../../core/services/user.service';

@Component({
  selector: 'app-add-team-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-team-card.component.html',
  styleUrl: './add-team-card.component.scss'
})
export class AddTeamCardComponent implements OnInit {
  @Input() show = false;
  @Output() showChange = new EventEmitter<boolean>();

  teamName = '';
  selectedLeader: User | null = null;
  selectedMembers: User[] = [];
  
  teamLeaders: User[] = [];
  teamMembers: User[] = [];
  
  showLeaderDropdown = false;
  showMemberDropdown = false;
  searchLeader = '';
  searchMember = '';

  // Add validation properties
  isTeamNameTouched = false;
  isLeaderTouched = false;
  isMembersTouched = false;

  constructor(
    private userService: UserService,
    private elementRef: ElementRef
  ) {}

  ngOnInit() {
    this.userService.getUsers().subscribe(users => {
      this.teamLeaders = users.filter(user => user.role === 'team_leader');
      this.teamMembers = users.filter(user => user.role === 'team_member');
    });
  }

  close() {
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

  selectLeader(leader: User) {
    this.selectedLeader = leader;
    this.showLeaderDropdown = false;
    this.searchLeader = '';
    this.isLeaderTouched = true;
  }

  removeLeader() {
    this.selectedLeader = null;
    this.isLeaderTouched = true;
  }

  selectMember(member: User) {
    if (!this.selectedMembers.find(m => m.id === member.id)) {
      this.selectedMembers.push(member);
      this.isMembersTouched = true;
    }
    this.searchMember = '';
  }

  removeMember(member: User) {
    this.selectedMembers = this.selectedMembers.filter(m => m.id !== member.id);
    this.isMembersTouched = true;
  }

  onSave() {
    // TODO: Implement save logic
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
      leader.name.toLowerCase().includes(this.searchLeader.toLowerCase())
    );
  }

  get filteredMembers() {
    return this.teamMembers.filter(member => 
      member.name.toLowerCase().includes(this.searchMember.toLowerCase()) &&
      !this.selectedMembers.find(m => m.id === member.id)
    );
  }

  getRandomAvatar(): string {
    const avatars = ['pic1.png', 'pic2.png', 'pic3.png', 'pic4.png', 'pic5.png'];
    return avatars[Math.floor(Math.random() * avatars.length)];
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
