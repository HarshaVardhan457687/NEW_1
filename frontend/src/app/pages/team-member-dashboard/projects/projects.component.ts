import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectCardComponent } from './project-card/project-card.component';
import { NavbarComponent } from '../../../shared/navbar/navbar.component';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, ProjectCardComponent, NavbarComponent],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent {
  projects = [
    {
      title: 'Website Redesign',
      dueDate: 'Mar 15, 2025',
      teamSize: 8,
      role: 'UI/UX Lead',
      progress: 75,
      projectManager: 'Sarah Johnson',
      status: 'In Progress',
      priority: 'High'
    },
    {
      title: 'Mobile App Development',
      dueDate: 'Apr 20, 2025',
      teamSize: 12,
      role: 'Frontend Developer',
      progress: 45,
      projectManager: 'Michael Chen',
      status: 'In Progress',
      priority: 'Medium'
    },
    {
      title: 'Database Migration',
      dueDate: 'May 10, 2025',
      teamSize: 6,
      role: 'Developer',
      progress: 100,
      projectManager: 'Emily Brown',
      status: 'Completed',
      priority: 'Low'
    },
    {
      title: 'E-commerce Platform',
      dueDate: 'Jun 5, 2025',
      teamSize: 10,
      role: 'Frontend Lead',
      progress: 60,
      projectManager: 'David Wilson',
      status: 'In Progress',
      priority: 'High'
    },
    {
      title: 'Analytics Dashboard',
      dueDate: 'Jul 1, 2025',
      teamSize: 5,
      role: 'UI Developer',
      progress: 30,
      projectManager: 'Lisa Anderson',
      status: 'In Progress',
      priority: 'Medium'
    },
    {
      title: 'CRM Integration',
      dueDate: 'Aug 15, 2025',
      teamSize: 7,
      role: 'Backend Developer',
      progress: 90,
      projectManager: 'James Taylor',
      status: 'Completed',
      priority: 'High'
    },
    {
      title: 'Security Audit',
      dueDate: 'Sep 1, 2025',
      teamSize: 4,
      role: 'Security Analyst',
      progress: 20,
      projectManager: 'Robert Martin',
      status: 'In Progress',
      priority: 'High'
    },
    {
      title: 'Payment Gateway',
      dueDate: 'Oct 10, 2025',
      teamSize: 6,
      role: 'Full Stack Developer',
      progress: 55,
      projectManager: 'Emma Davis',
      status: 'In Progress',
      priority: 'Medium'
    },
    {
      title: 'Mobile App Redesign',
      dueDate: 'Nov 20, 2025',
      teamSize: 9,
      role: 'UI/UX Designer',
      progress: 40,
      projectManager: 'Sophie Wilson',
      status: 'In Progress',
      priority: 'Low'
    },
    {
      title: 'Cloud Migration',
      dueDate: 'Dec 5, 2025',
      teamSize: 11,
      role: 'Cloud Engineer',
      progress: 85,
      projectManager: 'Alex Thompson',
      status: 'Completed',
      priority: 'High'
    },
    {
      title: 'AI Integration',
      dueDate: 'Jan 15, 2026',
      teamSize: 8,
      role: 'ML Engineer',
      progress: 25,
      projectManager: 'Ryan Cooper',
      status: 'In Progress',
      priority: 'Medium'
    },
    {
      title: 'Performance Optimization',
      dueDate: 'Feb 1, 2026',
      teamSize: 5,
      role: 'Backend Developer',
      progress: 70,
      projectManager: 'Oliver White',
      status: 'In Progress',
      priority: 'High'
    },
    {
      title: 'User Authentication',
      dueDate: 'Mar 10, 2026',
      teamSize: 4,
      role: 'Security Engineer',
      progress: 95,
      projectManager: 'Isabella Gray',
      status: 'Completed',
      priority: 'High'
    },
    {
      title: 'Reporting System',
      dueDate: 'Apr 5, 2026',
      teamSize: 7,
      role: 'Full Stack Developer',
      progress: 35,
      projectManager: 'William Clark',
      status: 'In Progress',
      priority: 'Medium'
    },
    {
      title: 'API Gateway',
      dueDate: 'May 20, 2026',
      teamSize: 6,
      role: 'Backend Lead',
      progress: 50,
      projectManager: 'Sophia Lee',
      status: 'In Progress',
      priority: 'High'
    },
    {
      title: 'Content Management',
      dueDate: 'Jun 15, 2026',
      teamSize: 8,
      role: 'Frontend Developer',
      progress: 80,
      projectManager: 'Daniel Brown',
      status: 'Completed',
      priority: 'Low'
    },
    {
      title: 'DevOps Pipeline',
      dueDate: 'Jul 1, 2026',
      teamSize: 5,
      role: 'DevOps Engineer',
      progress: 65,
      projectManager: 'Lucas Martinez',
      status: 'In Progress',
      priority: 'High'
    },
    {
      title: 'Data Analytics',
      dueDate: 'Aug 10, 2026',
      teamSize: 9,
      role: 'Data Scientist',
      progress: 30,
      projectManager: 'Ava Wilson',
      status: 'In Progress',
      priority: 'Medium'
    },
    {
      title: 'Microservices',
      dueDate: 'Sep 5, 2026',
      teamSize: 12,
      role: 'System Architect',
      progress: 40,
      projectManager: 'Ethan Anderson',
      status: 'In Progress',
      priority: 'High'
    },
    {
      title: 'Testing Framework',
      dueDate: 'Oct 15, 2026',
      teamSize: 6,
      role: 'QA Lead',
      progress: 90,
      projectManager: 'Mia Taylor',
      status: 'Completed',
      priority: 'Medium'
    }
  ] as const;

}
