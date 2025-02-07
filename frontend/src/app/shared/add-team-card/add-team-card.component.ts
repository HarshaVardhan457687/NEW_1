import { Component, Input, Output, EventEmitter, OnInit, HostListener } from '@angular/core';
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

  constructor(private userService: UserService) {}

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
  }

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

  onSave() {
    // TODO: Implement save logic
    this.close();
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
