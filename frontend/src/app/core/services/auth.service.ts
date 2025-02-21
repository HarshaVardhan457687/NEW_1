import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly AUTH_API = 'http://localhost:8088/api/auth';
  private readonly TOKEN_KEY = 'auth_token';
  private readonly ROLES_KEY = 'user_roles';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.AUTH_API}/authenticate`, { username, password })
      .pipe(
        tap((response: any) => {
          localStorage.setItem(this.TOKEN_KEY, response.token);
          localStorage.setItem(this.ROLES_KEY, JSON.stringify(response.roles));
        })
      );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.ROLES_KEY);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getRoles(): string[] {
    const roles = localStorage.getItem(this.ROLES_KEY);
    return roles ? JSON.parse(roles) : [];
  }
}
