import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export type UserRole = 'project_manager' | 'team_leader' | 'team_member';

@Injectable({
  providedIn: 'root'
})
export class RoleSelectionService {
  private readonly ROLE_KEY = 'role_selected';
  private selectedRoleSubject = new BehaviorSubject<UserRole | null>(this.getStoredRole());

  constructor() {}

  setRole(role: UserRole): void {
    sessionStorage.setItem(this.ROLE_KEY, role);
    this.selectedRoleSubject.next(role);
  }

  getRole(): UserRole | null {
    return this.getStoredRole();
  }

  getRole$(): Observable<UserRole | null> {
    return this.selectedRoleSubject.asObservable();
  }

  private getStoredRole(): UserRole | null {
    const role = sessionStorage.getItem(this.ROLE_KEY);
    return role ? (role as UserRole) : null;  // Only cast if role is valid
  }
  

  clearRole(): void {
    sessionStorage.removeItem(this.ROLE_KEY);
    this.selectedRoleSubject.next(null);
  }
} 