import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { SideTabComponent } from '../../../../shared/side-tab/side-tab.component';

@Component({
  selector: 'app-side-bar',
  standalone: true,
  imports: [CommonModule, SideTabComponent],
  templateUrl: './side-bar.component.html',
  styleUrl: './side-bar.component.scss'
})
export class SideBarComponent implements OnInit {
  @Input() selectedTab: string = 'overview';
  @Output() tabChange = new EventEmitter<string>();

  tabs = [
    { icon: 'home_icon.svg', label: 'Overview', route: './overview', id: 'overview' },
    { icon: 'team_icon.svg', label: 'Teams', route: './my-team', id: 'teams' },
    { icon: 'objectives_icon.svg', label: 'Objectives', route: './objectives', id: 'objectives' },
    { icon: 'my_tasks_icon.svg', label: 'My Tasks', route: './tasks', id: 'tasks' },
    { icon: 'calender_icon.svg', label: 'Calendar', route: './calendar', id: 'calendar' }
  ];

  constructor(private router: Router) {}

  ngOnInit() {
    // Set initial selected tab based on current URL
    this.updateSelectedTab(this.router.url);

    // Update on navigation
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.updateSelectedTab(event.url);
    });
  }

  private updateSelectedTab(url: string) {
    console.log('[SideBar] Current URL:', url);
    const currentTab = this.tabs.find(tab => url.includes(tab.id));
    if (currentTab) {
      this.selectedTab = currentTab.id;
    } else {
      // Default to overview if no match found
      this.selectedTab = 'overview';
    }
  }

  onTabClick(tab: { id: string; route: string }) {
    this.tabChange.emit(tab.id);
  }
}
