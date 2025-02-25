import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoleSelectionCardComponent } from '../../shared/role-selection-card/role-selection-card.component';
import { UserRole } from '../../core/services/role-selection.service';

interface RoleOption {
  title: string;
  description: string;
  icon: string;
  route: string;
  roleType: UserRole;
}

@Component({
  selector: 'app-role-selection-page',
  standalone: true,
  imports: [CommonModule, RoleSelectionCardComponent],
  templateUrl: './role-selection-page.component.html',
  styleUrl: './role-selection-page.component.scss'
})
export class RoleSelectionPageComponent {
  roles: RoleOption[] = [
    {
      title: 'Project Manager',
      description: 'Lead and oversee your project\'s activities',
      icon: 'project_manager_role.png',
      route: '/team-manager-dashboard',
      roleType: 'project_manager'
    },
    {
      title: 'Team Leader',
      description: 'Lead and oversee your team\'s activities',
      icon: 'team_leader_role.png',
      route: '/team-leader-dashboard',
      roleType: 'team_leader'
    },
    {
      title: 'Project Member',
      description: 'Collaborate and work with your team',
      icon: 'member_role.png',
      route: '/team-member-dashboard',
      roleType: 'team_member'
    }
  ];
}
