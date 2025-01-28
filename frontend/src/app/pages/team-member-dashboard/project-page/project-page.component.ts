import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { NavbarComponent } from '../../../shared/navbar/navbar.component';
import { SideBarComponent } from '../../../shared/side-bar/side-bar.component';
import { ProjectService } from '../../../core/services/projects.service';

@Component({
  selector: 'app-project-page',
  standalone: true,
  imports: [CommonModule, NavbarComponent, SideBarComponent, RouterModule],
  templateUrl: './project-page.component.html',
  styleUrl: './project-page.component.scss'
})
export class ProjectPageComponent implements OnInit {
  projectTitle: string = '';

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService
  ) {}

  ngOnInit() {
    const projectId = Number(this.route.snapshot.paramMap.get('id'));
    this.projectService.getProjects().subscribe(projects => {
      const project = projects.find(p => p.id === projectId);
      if (project) {
        this.projectTitle = project.title;
      }
    });
  }
}
