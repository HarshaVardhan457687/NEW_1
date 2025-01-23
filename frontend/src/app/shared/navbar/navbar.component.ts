import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  host: { 'id': 'navbar-component' }
})
export class NavbarComponent implements OnInit {
  currentTab: 'dashboard' | 'projects' = 'dashboard';

  constructor(private router: Router) {}

  ngOnInit() {
    // Set initial active tab based on current route
    const currentRoute = this.router.url;
    if (currentRoute.includes('projects')) {
      this.currentTab = 'projects';
    } else if (currentRoute.includes('team-member-dashboard')) {
      this.currentTab = 'dashboard';
    }
  }

  isHighlighted(tab: 'dashboard' | 'projects'): boolean {
    return this.currentTab === tab;
  }

  setActiveTab(tab: 'dashboard' | 'projects'): void {
    this.currentTab = tab;
    if (tab === 'projects') {
      this.router.navigate(['/projects']);
    } else {
      this.router.navigate(['/team-member-dashboard']);
    }
  }

  navigateToUnderConstruction(): void {
    this.router.navigate(['/construction']);
  }
}