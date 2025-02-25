import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RoleSelectionService, UserRole } from '../../core/services/role-selection.service';

@Component({
  selector: 'app-role-selection-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './role-selection-card.component.html',
  styleUrl: './role-selection-card.component.scss'
})
export class RoleSelectionCardComponent {
  @Input() title: string = '';
  @Input() description: string = '';
  @Input() icon: string = '';
  @Input() route: string = '';
  @Input() roleType: UserRole = 'team_member';

  constructor(
    private router: Router,
    private roleSelectionService: RoleSelectionService
  ) {}

  onContinue() {
    this.roleSelectionService.clearRole();
    this.roleSelectionService.setRole(this.roleType);
    this.router.navigate([this.route]);
  }
}
