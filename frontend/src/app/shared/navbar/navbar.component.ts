import { Component } from '@angular/core';
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
export class NavbarComponent {
  private currentTab: 'dashboard' | 'projects' = 'dashboard';

  constructor(private router: Router) {}

  isHighlighted(tab: 'dashboard' | 'projects'): boolean {
    return this.currentTab === tab;
  }

  setActiveTab(tab: 'dashboard' | 'projects'): void {
    this.currentTab = tab;
  }

  navigateToUnderConstruction(): void {
    this.router.navigate(['/under-construction']);
  }
}