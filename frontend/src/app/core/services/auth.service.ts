import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { tap, map, catchError, switchMap } from 'rxjs/operators';
import { Router } from '@angular/router';

export interface UserDTO {
  name: string;
  userName: string;
  emailId: string;
  password: string;
}

export interface UserServiceDTO {
  userName: string;
  userEmail: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly AUTH_API = 'http://localhost:8088/api/auth';
  private readonly USER_API = 'http://localhost:8060/api/users';  // Through API Gateway
  private readonly TOKEN_KEY = 'auth_token';
  private readonly ROLES_KEY = 'user_roles';

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.AUTH_API}/authenticate`, { username, password })
      .pipe(
        tap((response: any) => {
          localStorage.clear();
          sessionStorage.clear();
          localStorage.setItem(this.TOKEN_KEY, response.token);
          localStorage.setItem(this.ROLES_KEY, JSON.stringify(response.roles));
          localStorage.setItem('username', username);
          localStorage.setItem('name', response.name);
        })
      );
  }

  logout(): void {
    localStorage.clear();
    sessionStorage.clear();
    this.router.navigate(['/login']);
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

  validate(): Observable<boolean> {
    const token = this.getToken();
    if (!token) return of(false);

    return this.http.post<{ valid: boolean }>(`${this.AUTH_API}/validate`, { token })
      .pipe(map(response => response.valid), catchError(() => of(false)));
  }

  isValidToken(): Observable<boolean> {
    return this.validate();
  }

  register(userDTO: UserDTO): Observable<any> {
    let keycloakUserId: string;

    return this.http.post(`${this.AUTH_API}/register`, userDTO).pipe(
      // Store the Keycloak response
      tap((response: any) => {
        keycloakUserId = response[0]?.id; // Store Keycloak user ID for potential rollback
      }),
      // Attempt user service registration
      switchMap(() => {
        const userServiceDTO: UserServiceDTO = {
          userName: userDTO.name,
          userEmail: userDTO.emailId
        };
        return this.http.post(this.USER_API, userServiceDTO);
      }),
      // If user service registration fails, delete from Keycloak
      catchError(error => {
        if (keycloakUserId) {
          // Delete the user from Keycloak
          return this.http.delete(`${this.AUTH_API}/delete/${keycloakUserId}`).pipe(
            // Re-throw the original error after cleanup
            switchMap(() => throwError(() => new Error('Registration failed. Please try again.')))
          );
        }
        return throwError(() => error);
      })
    );
  }
}
