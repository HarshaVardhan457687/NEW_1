import { Component, OnInit, Input } from '@angular/core';
import { RouterLink, RouterLinkActive, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ModalService } from '../../core/services/modal.service';
import { NavbarService } from '../../core/services/navbar.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  host: { 'id': 'navbar-component' }
})
export class NavbarComponent implements OnInit {
  @Input() projectTitle?: string;
  @Input() dashboardType: 'team-leader' | 'team-member' | 'team-manager' = 'team-member';
  currentTab: 'dashboard' | 'projects' = 'dashboard';
  profilePicUrl: string = 'assets/default_profile.png';

  constructor(
    private router: Router,
    private modalService: ModalService,
    private navbarService: NavbarService
  ) {}

  ngOnInit() {
    this.setInitialTab();
    this.loadProfilePicture();
  }

  private setInitialTab() {
    const currentRoute = this.router.url;
    if (currentRoute.includes('projects')) {
      this.currentTab = 'projects';
    } else if (currentRoute.includes('team-member-dashboard') || currentRoute.includes('team-leader-dashboard')) {
      this.currentTab = 'dashboard';
    }
  }

  private loadProfilePicture() {
    this.navbarService.getProfilePicture()
      .subscribe({
        next: (url) => {
          this.profilePicUrl = url;
        },
        error: () => {
          // Keep default profile picture on error
          console.error('Failed to load profile picture');
        }
      });
  }

  isHighlighted(tab: 'dashboard' | 'projects'): boolean {
    return this.currentTab === tab;
  }

  setActiveTab(tab: 'dashboard' | 'projects'): void {
    this.currentTab = tab;
    if (tab === 'projects') {
      const projectsPath = this.dashboardType === 'team-leader'
        ? '/team-leader-dashboard/projects'
        : this.dashboardType === 'team-manager'
        ? '/team-manager-dashboard/projects'
        : '/team-member-dashboard/projects';
      this.router.navigate([projectsPath]);

    } else {
      const dashboardPath = this.dashboardType === 'team-leader'
        ? '/team-leader-dashboard'
        : this.dashboardType === 'team-manager'
        ? '/team-manager-dashboard'
        : '/team-member-dashboard';
      this.router.navigate([dashboardPath]);
    }
  }

  navigateToProfile(): void {
    this.modalService.openProfileModal();
  }


  navigateToUnderConstruction(): void {
    this.router.navigate(['/construction']);
  }

  getWelcomeText(): string {
    if (this.projectTitle) {
      return this.projectTitle;
    }
    return `Welcome back, ${this.navbarService.getUserName()}`;
  }
}