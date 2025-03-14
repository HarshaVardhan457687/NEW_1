import { Component, OnInit, Input, Output, EventEmitter, HostBinding } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MyTasksService, TaskPriority, TaskStatus, TaskDetailsDTO } from '../../core/services/my-tasks.service';

interface CalendarDay {
  date: Date;
  dayNumber: number;
  isCurrentMonth: boolean;
  isToday: boolean;
  tasks: TaskDetailsDTO[];
}

type CalendarView = 'month' | 'week';
type FilterType = 'status' | 'priority';

@Component({
  selector: 'app-calender',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './calender.component.html',
  styleUrl: './calender.component.scss'
})
export class CalenderComponent implements OnInit {
  @HostBinding('style.height') height = '100%';
  @HostBinding('style.display') display = 'block';
  
  // Calendar data
  currentDate: Date = new Date();
  selectedDate: Date = new Date();
  calendarDays: CalendarDay[] = [];
  weekDays: string[] = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  months: string[] = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  // View controls
  currentView: CalendarView = 'month';
  showMonthYearSelector: boolean = false;
  selectorMode: 'month' | 'year' = 'month';
  yearsList: number[] = [];
  
  // Task filters
  statusFilters: { value: TaskStatus, label: string, checked: boolean }[] = [
    { value: TaskStatus.PENDING, label: 'Pending', checked: true },
    { value: TaskStatus.COMPLETED, label: 'Completed', checked: true },
    { value: TaskStatus.WAITING_FOR_APPROVAL, label: 'Waiting for Approval', checked: true }
  ];
  
  priorityFilters: { value: TaskPriority, label: string, checked: boolean }[] = [
    { value: TaskPriority.HIGH, label: 'High', checked: true },
    { value: TaskPriority.MEDIUM, label: 'Medium', checked: true },
    { value: TaskPriority.LOW, label: 'Low', checked: true }
  ];

  // Selected day tasks
  selectedDayTasks: TaskDetailsDTO[] = [];
  showTaskDetails: boolean = false;

  // Dummy tasks data (will be replaced with service data later)
  dummyTasks: TaskDetailsDTO[] = [
    {
      taskId: 1,
      taskName: 'Complete Project Proposal',
      dueDate: new Date(new Date().getFullYear(), new Date().getMonth(), 10),
      taskTag: 'Planning',
      taskPriority: TaskPriority.HIGH,
      taskStatus: TaskStatus.PENDING,
      taskDescription: 'Finalize the project proposal document with all requirements and submit for review.'
    },
    {
      taskId: 2,
      taskName: 'Review Code Changes',
      dueDate: new Date(new Date().getFullYear(), new Date().getMonth(), 15),
      taskTag: 'Development',
      taskPriority: TaskPriority.MEDIUM,
      taskStatus: TaskStatus.WAITING_FOR_APPROVAL,
      taskDescription: 'Review the latest code changes and provide feedback to the development team.'
    },
    {
      taskId: 3,
      taskName: 'Update Documentation',
      dueDate: new Date(new Date().getFullYear(), new Date().getMonth(), 20),
      taskTag: 'Documentation',
      taskPriority: TaskPriority.LOW,
      taskStatus: TaskStatus.COMPLETED,
      taskDescription: 'Update the project documentation with the latest changes and improvements.'
    },
    {
      taskId: 4,
      taskName: 'Team Meeting',
      dueDate: new Date(new Date().getFullYear(), new Date().getMonth(), 5),
      taskTag: 'Meeting',
      taskPriority: TaskPriority.MEDIUM,
      taskStatus: TaskStatus.COMPLETED,
      taskDescription: 'Weekly team meeting to discuss project progress and upcoming tasks.'
    },
    {
      taskId: 5,
      taskName: 'Client Presentation',
      dueDate: new Date(new Date().getFullYear(), new Date().getMonth(), 25),
      taskTag: 'Presentation',
      taskPriority: TaskPriority.HIGH,
      taskStatus: TaskStatus.PENDING,
      taskDescription: 'Prepare and deliver a presentation to the client on project progress.'
    },
    {
      taskId: 6,
      taskName: 'Bug Fixing Session',
      dueDate: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate() + 2),
      taskTag: 'Development',
      taskPriority: TaskPriority.HIGH,
      taskStatus: TaskStatus.PENDING,
      taskDescription: 'Address critical bugs reported in the latest release.'
    },
    {
      taskId: 7,
      taskName: 'Performance Testing',
      dueDate: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate() + 3),
      taskTag: 'QA',
      taskPriority: TaskPriority.MEDIUM,
      taskStatus: TaskStatus.PENDING,
      taskDescription: 'Conduct performance testing on the application under various load conditions.'
    },
    {
      taskId: 8,
      taskName: 'Sprint Planning',
      dueDate: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate() + 1),
      taskTag: 'Planning',
      taskPriority: TaskPriority.MEDIUM,
      taskStatus: TaskStatus.PENDING,
      taskDescription: 'Plan the next sprint and assign tasks to team members.'
    }
  ];

  constructor(private myTasksService: MyTasksService) {}

  ngOnInit(): void {
    this.generateYearsList();
    this.generateCalendarDays();
    
    // In the future, we'll replace the dummy data with actual service data
    // this.loadTasksFromService();
  }

  // Future method to load tasks from the service
  // loadTasksFromService(): void {
  //   const userEmail = localStorage.getItem('username');
  //   const projectId = parseInt(localStorage.getItem('selectedProjectId') || '0', 10);
  //   
  //   if (userEmail && projectId) {
  //     this.myTasksService.getTasksForUser(userEmail, projectId).subscribe({
  //       next: (tasks) => {
  //         // Replace dummy tasks with actual tasks
  //         this.dummyTasks = tasks;
  //         this.generateCalendarDays();
  //       },
  //       error: (error) => {
  //         console.error('Error loading tasks:', error);
  //       }
  //     });
  //   }
  // }

  // Generate a list of years for the year selector
  generateYearsList(): void {
    const currentYear = new Date().getFullYear();
    this.yearsList = [];
    
    // Generate a list of years (current year -5 to current year +5)
    for (let i = currentYear - 5; i <= currentYear + 5; i++) {
      this.yearsList.push(i);
    }
  }

  // Toggle the month/year selector
  toggleMonthYearSelector(): void {
    this.showMonthYearSelector = !this.showMonthYearSelector;
    this.selectorMode = 'month';
  }

  // Select a month from the month selector
  selectMonth(monthIndex: number): void {
    this.selectedDate = new Date(
      this.selectedDate.getFullYear(),
      monthIndex,
      1
    );
    this.generateCalendarDays();
    this.selectorMode = 'year';
  }

  // Select a year from the year selector
  selectYear(year: number): void {
    this.selectedDate = new Date(
      year,
      this.selectedDate.getMonth(),
      1
    );
    this.generateCalendarDays();
    this.showMonthYearSelector = false;
  }

  // Generate calendar days based on the selected date and view
  generateCalendarDays(): void {
    this.calendarDays = [];
    
    if (this.currentView === 'month') {
      this.generateMonthView();
    } else if (this.currentView === 'week') {
      this.generateWeekView();
    }
  }

  // Generate days for month view
  generateMonthView(): void {
    this.calendarDays = [];
    
    // Get the first day of the month
    const firstDay = new Date(
      this.selectedDate.getFullYear(),
      this.selectedDate.getMonth(),
      1
    );
    
    // Get the last day of the month
    const lastDay = new Date(
      this.selectedDate.getFullYear(),
      this.selectedDate.getMonth() + 1,
      0
    );
    
    // Get the day of the week the first day falls on (0-6, where 0 is Sunday)
    const firstDayIndex = firstDay.getDay();
    
    // Get the last day of the previous month
    const prevMonthLastDay = new Date(
      this.selectedDate.getFullYear(),
      this.selectedDate.getMonth(),
      0
    ).getDate();
    
    // Add days from previous month to fill the first row
    for (let i = 0; i < firstDayIndex; i++) {
      const date = new Date(
        this.selectedDate.getFullYear(),
        this.selectedDate.getMonth() - 1,
        prevMonthLastDay - (firstDayIndex - i - 1)
      );
      
      this.calendarDays.push({
        date,
        dayNumber: prevMonthLastDay - (firstDayIndex - i - 1),
        isCurrentMonth: false,
        isToday: this.isToday(date),
        tasks: this.getTasksForDate(date)
      });
    }
    
    // Add all days of current month
    for (let i = 1; i <= lastDay.getDate(); i++) {
      const date = new Date(
        this.selectedDate.getFullYear(),
        this.selectedDate.getMonth(),
        i
      );
      
      this.calendarDays.push({
        date,
        dayNumber: i,
        isCurrentMonth: true,
        isToday: this.isToday(date),
        tasks: this.getTasksForDate(date)
      });
    }
    
    // Calculate how many days from next month to show to complete the grid
    // We want to show exactly 6 rows (42 days) for consistency
    const daysToAdd = 42 - this.calendarDays.length;
    
    // Add days from next month
    for (let i = 1; i <= daysToAdd; i++) {
      const date = new Date(
        this.selectedDate.getFullYear(),
        this.selectedDate.getMonth() + 1,
        i
      );
      
      this.calendarDays.push({
        date,
        dayNumber: i,
        isCurrentMonth: false,
        isToday: this.isToday(date),
        tasks: this.getTasksForDate(date)
      });
    }

    // Make sure we have exactly 42 days (6 weeks)
    if (this.calendarDays.length !== 42) {
      console.warn('Calendar days not equal to 42:', this.calendarDays.length);
    }
  }

  // Generate days for week view
  generateWeekView(): void {
    // Get the first day of the week (Sunday)
    const firstDayOfWeek = new Date(this.selectedDate);
    const day = this.selectedDate.getDay();
    firstDayOfWeek.setDate(this.selectedDate.getDate() - day);
    
    // Generate 7 days starting from the first day of the week
    for (let i = 0; i < 7; i++) {
      const date = new Date(firstDayOfWeek);
      date.setDate(firstDayOfWeek.getDate() + i);
      
      this.calendarDays.push({
        date,
        dayNumber: date.getDate(),
        isCurrentMonth: date.getMonth() === this.selectedDate.getMonth(),
        isToday: this.isToday(date),
        tasks: this.getTasksForDate(date)
      });
    }
  }

  // Check if a date is today
  isToday(date: Date): boolean {
    const today = new Date();
    return date.getDate() === today.getDate() &&
           date.getMonth() === today.getMonth() &&
           date.getFullYear() === today.getFullYear();
  }

  // Get tasks for a specific date
  getTasksForDate(date: Date): TaskDetailsDTO[] {
    // Filter tasks based on the selected filters
    return this.dummyTasks.filter(task => {
      const taskDate = new Date(task.dueDate);
      const isMatchingDate = taskDate.getDate() === date.getDate() &&
                             taskDate.getMonth() === date.getMonth() &&
                             taskDate.getFullYear() === date.getFullYear();
      
      const isStatusIncluded = this.statusFilters.find(f => f.value === task.taskStatus)?.checked;
      const isPriorityIncluded = this.priorityFilters.find(f => f.value === task.taskPriority)?.checked;
      
      return isMatchingDate && isStatusIncluded && isPriorityIncluded;
    });
  }

  // Get upcoming tasks (next 7 days)
  getUpcomingTasks(): TaskDetailsDTO[] {
    const today = new Date();
    const nextWeek = new Date();
    nextWeek.setDate(today.getDate() + 7);
    
    return this.dummyTasks
      .filter(task => {
        const taskDate = new Date(task.dueDate);
        return taskDate >= today && taskDate <= nextWeek &&
               this.statusFilters.find(f => f.value === task.taskStatus)?.checked &&
               this.priorityFilters.find(f => f.value === task.taskPriority)?.checked;
      })
      .sort((a, b) => a.dueDate.getTime() - b.dueDate.getTime());
  }

  // Change the current view (month, week)
  changeView(view: CalendarView): void {
    this.currentView = view;
    this.generateCalendarDays();
  }

  // Navigate to previous period (month or week)
  prev(): void {
    if (this.currentView === 'month') {
      this.selectedDate = new Date(
        this.selectedDate.getFullYear(),
        this.selectedDate.getMonth() - 1,
        1
      );
    } else if (this.currentView === 'week') {
      const newDate = new Date(this.selectedDate);
      newDate.setDate(this.selectedDate.getDate() - 7);
      this.selectedDate = newDate;
    }
    this.generateCalendarDays();
  }

  // Navigate to next period (month or week)
  next(): void {
    if (this.currentView === 'month') {
      this.selectedDate = new Date(
        this.selectedDate.getFullYear(),
        this.selectedDate.getMonth() + 1,
        1
      );
    } else if (this.currentView === 'week') {
      const newDate = new Date(this.selectedDate);
      newDate.setDate(this.selectedDate.getDate() + 7);
      this.selectedDate = newDate;
    }
    this.generateCalendarDays();
  }

  // Go to today
  goToToday(): void {
    this.selectedDate = new Date();
    this.generateCalendarDays();
  }

  // Select a specific date
  selectDate(day: CalendarDay): void {
    this.selectedDate = new Date(day.date);
    this.selectedDayTasks = day.tasks;
    this.showTaskDetails = true;
    this.generateCalendarDays();
  }

  // Toggle a filter
  toggleFilter(type: FilterType, index: number): void {
    if (type === 'status') {
      this.statusFilters[index].checked = !this.statusFilters[index].checked;
    } else {
      this.priorityFilters[index].checked = !this.priorityFilters[index].checked;
    }
    this.generateCalendarDays();
  }

  // Get color for task priority
  getPriorityColor(priority: TaskPriority): string {
    switch (priority) {
      case TaskPriority.HIGH:
        return '#ff4d4f';
      case TaskPriority.MEDIUM:
        return '#faad14';
      case TaskPriority.LOW:
        return '#52c41a';
      default:
        return '#1890ff';
    }
  }

  // Get color for task status
  getStatusColor(status: TaskStatus): string {
    switch (status) {
      case TaskStatus.PENDING:
        return '#1890ff';
      case TaskStatus.COMPLETED:
        return '#52c41a';
      case TaskStatus.WAITING_FOR_APPROVAL:
        return '#faad14';
      default:
        return '#d9d9d9';
    }
  }

  // Format date for display
  formatDate(date: Date): string {
    return `${this.weekDays[date.getDay()]}, ${this.months[date.getMonth()]} ${date.getDate()}, ${date.getFullYear()}`;
  }
}
