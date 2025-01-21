import { Component } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar.component';

@Component({
  selector: 'app-team-member-dashboard',
  standalone: true,
  imports: [NavbarComponent],
  templateUrl: './team-member-dashboard.component.html',
  styleUrl: './team-member-dashboard.component.scss',
  host: { 'id': 'team-member-dashboard-component' }
})
export class TeamMemberDashboardComponent {
}