import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SideTabComponent } from './side-tab/side-tab.component';

@Component({
  selector: 'app-side-bar',
  standalone: true,
  imports: [CommonModule, SideTabComponent],
  templateUrl: './side-bar.component.html',
  styleUrl: './side-bar.component.scss'
})
export class SideBarComponent {
  @Input() selectedTab: string = 'overview';

  tabs = [
    { icon: 'home_icon.svg', label: 'Overview', route: './overview', id: 'overview' },
    { icon: 'team_icon.svg', label: 'My Team', route: '/construction', id: 'team' },
    { icon: 'objectives_icon.svg', label: 'Objectives', route: '/construction', id: 'objectives' },
    { icon: 'my_tasks_icon.svg', label: 'My Tasks', route: '/construction', id: 'tasks' },
    { icon: 'calender_icon.svg', label: 'Calendar', route: '/construction', id: 'calendar' }
  ];
}
