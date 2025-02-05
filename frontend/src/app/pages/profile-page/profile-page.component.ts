import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

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
  selector: 'app-profile-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
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

  constructor(private router: Router) {}

  changePassword() {
    this.router.navigate(['/change-password']);
  }

  viewHistory() {
    // Add view history logic here
  }

  toggleEmailNotifications() {
    this.profile.emailNotifications = !this.profile.emailNotifications;
  }
}
