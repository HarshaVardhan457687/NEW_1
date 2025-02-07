import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SideTabComponent } from '../../../../shared/side-tab/side-tab.component';

@Component({
  selector: 'app-side-bar',
  standalone: true,
  imports: [CommonModule, SideTabComponent],
  templateUrl: './side-bar.component.html',
  styleUrl: './side-bar.component.scss'
})
export class SideBarComponent {
  @Input() selectedTab: string = 'overview';
  @Output() tabChange = new EventEmitter<string>();

  tabs = [
    { icon: 'home_icon.svg', label: 'Overview', route: './overview', id: 'overview' },
    { icon: 'team_icon.svg', label: 'Teams', route: './teams', id: 'teams' },
    { icon: 'objectives_icon.svg', label: 'Objectives', route: './objectives', id: 'objectives' },
    { icon: 'my_tasks_icon.svg', label: 'My Tasks', route: './tasks', id: 'tasks' },
    { icon: 'approvals_icon.svg', label: 'Approvals', route: './approvals', id: 'approvals' },
    { icon: 'analytics_icon.svg', label: 'Analytics', route: './analytics', id: 'analytics' },
    { icon: 'calender_icon.svg', label: 'Calendar', route: './calendar', id: 'calendar' }
  ];

  onTabClick(tab: { id: string; route: string }) {
    this.tabChange.emit(tab.id);
  }
}
