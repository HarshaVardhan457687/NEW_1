import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-side-tab',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './side-tab.component.html',
  styleUrl: './side-tab.component.scss'
})
export class SideTabComponent {
  @Input() icon: string = '';
  @Input() label: string = '';
  @Input() isSelected: boolean = false;
  @Input() route: string = '';
}
