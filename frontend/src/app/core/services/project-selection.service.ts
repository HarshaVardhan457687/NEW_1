import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectSelectionService {
  private selectedProjectId = new BehaviorSubject<number | null>(this.getStoredProjectId());
  selectedProject$ = this.selectedProjectId.asObservable();

  constructor() {
    // Initialize with stored value if exists
    const storedId = this.getStoredProjectId();
    if (storedId !== null) {
      this.selectedProjectId.next(storedId);
    }
  }

  setSelectedProject(projectId: number) {
    // Store in session storage
    sessionStorage.setItem('selected_project_id', projectId.toString());
    this.selectedProjectId.next(projectId);
  }

  getSelectedProject(): number | null {
    return this.selectedProjectId.value;
  }

  private getStoredProjectId(): number | null {
    const storedId = sessionStorage.getItem('selected_project_id');
    return storedId ? parseInt(storedId, 10) : null;
  }

  clearSelectedProject() {
    sessionStorage.removeItem('selected_project_id');
    this.selectedProjectId.next(null);
  }
} 