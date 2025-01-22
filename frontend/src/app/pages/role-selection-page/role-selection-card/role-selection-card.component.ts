import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

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

  constructor(private router: Router) {}

  onContinue() {
    this.router.navigate([this.route]);
  }
}
