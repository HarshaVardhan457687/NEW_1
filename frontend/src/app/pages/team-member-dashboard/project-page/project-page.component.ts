import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { NavbarComponent } from '../../../shared/navbar/navbar.component';
import { SideBarComponent } from '../../../shared/side-bar/side-bar.component';
import { ProjectService } from '../../../core/services/projects.service';
import { ProjectWithManager } from '../../../core/services/projects.service';

@Component({
  selector: 'app-project-page',
  standalone: true,
  imports: [CommonModule, NavbarComponent, SideBarComponent, RouterModule],
  templateUrl: './project-page.component.html',
  styleUrl: './project-page.component.scss'
})
export class ProjectPageComponent implements OnInit {
  projectTitle: string = '';
  project?: ProjectWithManager;
  isLoading: boolean = true;
  error?: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private projectService: ProjectService
  ) {}

  ngOnInit() {
    const projectId = Number(this.route.snapshot.paramMap.get('id'));
    console.log('Loading project ID:', projectId); // Debug log
    
    if (isNaN(projectId)) {
      this.error = 'Invalid project ID';
      this.isLoading = false;
      return;
    }

    this.projectService.getProjectById(projectId).subscribe({
      next: (project) => {
        console.log('Loaded project:', project); // Debug log
        this.project = project;
        this.projectTitle = project.title;
        
        // Check if we're not already on the overview route
        if (!this.route.firstChild) {
          this.router.navigate(['overview'], {
            relativeTo: this.route
          });
        }
      },
      error: (err) => {
        console.error('Error loading project:', err);
        this.error = 'Failed to load project';
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}
