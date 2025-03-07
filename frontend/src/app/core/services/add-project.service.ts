import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface ProjectRequest {
  projectName: string;
  projectDescription: string;
  projectPriority: string;
  projectDueDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class AddProjectService {
  private readonly API_URL = 'http://localhost:8060/api/projects';

  constructor(private http: HttpClient) {}

  createProject(project: ProjectRequest): Observable<any> {
    const projectManagerEmail = localStorage.getItem('username');
    return this.http.post(`${this.API_URL}/new`, {
      projectName: project.projectName,
      projectManagerEmail: projectManagerEmail,
      projectDescription: project.projectDescription,
      projectPriority: project.projectPriority.toUpperCase(),
      projectDueDate: project.projectDueDate
    });
  }
}
