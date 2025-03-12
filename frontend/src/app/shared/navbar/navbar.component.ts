import { Component, OnInit, Input } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ModalService } from '../../core/services/modal.service';
import { NavbarService } from '../../core/services/navbar.service';
import { NotificationService } from '../../core/services/notification.service';
import { NotificationComponentComponent } from '../notification-component/notification-component.component';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, NotificationComponentComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  host: { 'id': 'navbar-component' }
})
export class NavbarComponent implements OnInit {
  @Input() projectTitle?: string;
  @Input() dashboardType: 'team-leader' | 'team-member' | 'team-manager' = 'team-member';
  currentTab: 'dashboard' | 'projects' = 'dashboard';
  profilePicUrl: string = 'assets/default_profile.png';
  showNotifications: boolean = false;
  unreadNotificationCount: number = 0;

  constructor(
    private router: Router,
    private modalService: ModalService,
    private navbarService: NavbarService,
    private notificationService: NotificationService
  ) {}

  ngOnInit() {
    this.setInitialTab();
    this.loadProfilePicture();
    this.loadNotificationCount();
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

  private loadNotificationCount() {
    this.notificationService.getUnreadCountObservable()
      .subscribe(count => {
        this.unreadNotificationCount = count;
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

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }

  closeNotifications(): void {
    this.showNotifications = false;
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