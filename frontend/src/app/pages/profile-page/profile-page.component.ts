import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ModalService } from '../../core/services/modal.service';
import { ProfilePageService, ProfileData } from '../../core/services/profile-page.service';
import { EditProfileComponent } from '../../shared/edit-profile/edit-profile.component';

@Component({
  selector: 'app-profile-modal',
  standalone: true,
  imports: [CommonModule, EditProfileComponent],
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.scss']
})
export class ProfilePageComponent implements OnInit {
  profile: ProfileData = {
    name: 'loading...',
    title: 'loading...',
    email: 'loading...',
    phone: 'loading...',
    location: 'loading...',
    timezone: 'loading...',
    imageUrl: null,
    joiningDate: 'loading...',
    emailNotifications: false,
    projectsCount: 0
  };

  showEditProfile = false;

  constructor(
    private router: Router,
    private modalService: ModalService,
    private profileService: ProfilePageService
  ) {}

  ngOnInit(): void {
    this.loadProfileData();
  }

  private loadProfileData(): void {
    this.profileService.getProfileData().subscribe({
      next: (data) => {
        this.profile = {
          name: data.name || 'Info not found',
          title: data.title || 'Info not found',
          email: data.email || 'Info not found',
          phone: data.phone || 'Info not found',
          location: data.location || 'Info not found',
          timezone: data.timezone || 'Info not found',
          imageUrl: data.imageUrl,
          joiningDate: data.joiningDate || 'Info not found',
          emailNotifications: data.emailNotifications,
          projectsCount: data.projectsCount
        };
      },
      error: (error) => {
        console.error('Error loading profile data:', error);
        // Profile will retain default "Info not found" values on error
      }
    });
  }

  goBack(): void {
    this.modalService.closeProfileModal();
  }

  onOverlayClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.goBack();
    }
  }

  changePassword(): void {
    this.router.navigate(['/construction']);
    this.modalService.closeProfileModal();
  }

  viewHistory(): void {
    // Add view history logic here
  }

  toggleEmailNotifications(): void {
    // Simply toggle the local state without making an API call
    this.profile.emailNotifications = !this.profile.emailNotifications;
  }

  calculateExperience(joiningDate: string | null): string {
    if(joiningDate !== null && joiningDate !== 'Info not found'){
      const start = new Date(joiningDate);
      const now = new Date();
      const years = now.getFullYear() - start.getFullYear();
      const months = now.getMonth() - start.getMonth();
      
      if (months < 0) {
        return `${years - 1} years, ${12 + months} months`;
      }
      return `${years} years, ${months} months`;
    }
    return 'Info not found';
  }

  openEditProfile(): void {
    this.showEditProfile = true;
  }

  closeEditProfile(): void {
    this.showEditProfile = false;
  }

  handleProfileUpdate(formData: any): void {
    this.profileService.updateProfile(formData).subscribe({
      next: () => {
        this.closeEditProfile();
        // Reload profile data to show updated values
        this.loadProfileData();
      },
      error: (error) => {
        console.error('Error updating profile:', error);
        // Handle error appropriately (show error message, etc.)
      }
    });
  }
}
