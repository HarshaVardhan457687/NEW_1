import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NavbarService {
  private readonly API_URL = 'http://localhost:8060/api/users';
  private readonly PROFILE_PIC_KEY = 'user_profile_pic';

  constructor(private http: HttpClient) {}

  getUserName(): string {
    return localStorage.getItem('name') || 'User';
  }

  getProfilePicture(): Observable<string> {
    // Check session storage first
    const cachedPic = sessionStorage.getItem(this.PROFILE_PIC_KEY);
    if (cachedPic) {
      return of(cachedPic);
    }

    // If not in session storage, fetch from API
    const userEmail = localStorage.getItem('username') || '';
    const params = new HttpParams().set('userEmail', userEmail);

    return this.http.get(`${this.API_URL}/get/profile-pic`, { 
      params,
      responseType: 'text'  // Specify text response type
    }).pipe(
      tap(picUrl => {
        sessionStorage.setItem(this.PROFILE_PIC_KEY, picUrl);
      })
    );
  }
} 