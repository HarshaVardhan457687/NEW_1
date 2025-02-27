import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/operators';

export interface ProfileData {
  name: string | null;
  title: string | null;
  email: string | null;
  phone: string | null;
  location: string | null;
  timezone: string | null;
  imageUrl: string | null;
  joiningDate: string | null;
  emailNotifications: boolean;
  projectsCount: number;
}

interface UpdateProfileRequest {
  userName: string;
  userEmail: string;
  userPhoneNo: number;
  userAddress: string;
  userDesignation: string;
  userTimeZone: string;
  userJoiningDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProfilePageService {
  private readonly API_URL = 'http://localhost:8060/api/users';
  private readonly PROFILE_STORAGE_KEY = 'user_profile';

  constructor(private http: HttpClient) {}

  private getUserEmail(): string {
    return localStorage.getItem('username') || '';
  }

  private transformUserData(userData: any): ProfileData {
    const totalProjects = (
      (userData.userManagerProjectId?.length || 0) +
      (userData.userTeamLeaderProjectId?.length || 0) +
      (userData.userTeamMemberProjectId?.length || 0)
    );

    return {
      name: userData.userName || null,
      title: userData.userDesignation || null,
      email: userData.userEmail || null,
      phone: userData.userPhoneNo ? `+${userData.userPhoneNo}` : null,
      location: userData.userAddress || null,
      timezone: userData.userTimeZone || null,
      imageUrl: userData.userProfilePhoto || null,
      joiningDate: userData.userJoiningDate || null,
      emailNotifications: false,
      projectsCount: totalProjects
    };
  }

  getProfileData(): Observable<ProfileData> {
    // Check session storage first
    const cachedProfile = sessionStorage.getItem(this.PROFILE_STORAGE_KEY);
    if (cachedProfile) {
      return of(JSON.parse(cachedProfile));
    }

    // If not in session storage, fetch from API
    const userEmail = this.getUserEmail();
    return this.http.get(`${this.API_URL}/by-email/${userEmail}`).pipe(
      map(userData => this.transformUserData(userData)),
      tap(profileData => {
        // Cache the transformed data in session storage
        sessionStorage.setItem(this.PROFILE_STORAGE_KEY, JSON.stringify(profileData));
      })
    );
  }

  clearCache(): void {
    sessionStorage.removeItem(this.PROFILE_STORAGE_KEY);
  }

  updateProfile(formData: any): Observable<void> {
    const userEmail = this.getUserEmail();
    
    // Transform form data to match backend structure
    const updateRequest: UpdateProfileRequest = {
      userName: formData.name,
      userEmail: formData.email,
      userPhoneNo: Number(formData.countryCode.substring(1) + formData.phoneNumber),
      userAddress: formData.address,
      userDesignation: formData.designation,
      userTimeZone: formData.timezone,
      userJoiningDate: formData.joiningDate
    };

    return this.http.patch<void>(`${this.API_URL}/${userEmail}`, updateRequest).pipe(
      tap(() => {
        // Clear cache to force refresh of profile data
        this.clearCache();
      })
    );
  }

  uploadProfilePicture(formData: FormData): Observable<string> {
    const userEmail = this.getUserEmail();
    return this.http.post(`${this.API_URL}/${userEmail}/upload-profile`, formData, {
      responseType: 'text'  // Tell HttpClient to expect a text response
    });
  }
} 