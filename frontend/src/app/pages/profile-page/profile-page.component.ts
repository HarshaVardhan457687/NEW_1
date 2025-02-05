import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ModalService } from '../../core/services/modal.service';

interface ProfileData {
  name: string;
  title: string;
  email: string;
  phone: string;
  location: string;
  timezone: string;
  projects: number;
  experience: string;
  emailNotifications: boolean;
  imageUrl?: string;  // Optional profile image URL
}

@Component({
  selector: 'app-profile-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.scss']
})
export class ProfilePageComponent {
  profile: ProfileData = {
    name: 'Sarah Anderson',
    title: 'Team Manager',
    email: 'sarah.anderson@company.com',
    phone: '+1 (555) 123-4567',
    location: 'San Francisco, CA',
    timezone: 'PST (UTC-8)',
    projects: 3,
    experience: '5 years at company',
    emailNotifications: true,
    imageUrl: '',  // Will be empty by default, showing the default avatar
  };

  constructor(
    private router: Router,
    private modalService: ModalService
  ) {}

  goBack(): void {
    this.modalService.closeProfileModal();
  }

  onOverlayClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.goBack();
    }
  }

  changePassword() : void {
    this.router.navigate(['/construction']);
    this.modalService.closeProfileModal();
  }

  viewHistory() {
    // Add view history logic here
  }

  toggleEmailNotifications() : void {
    this.profile.emailNotifications = !this.profile.emailNotifications;
  }
}
