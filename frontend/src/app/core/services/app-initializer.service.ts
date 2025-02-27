import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AppInitializerService {
  private readonly REFRESH_KEY = 'app_load_time';

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  initializeApp(): void {
    if (isPlatformBrowser(this.platformId)) {
      const lastLoadTime = sessionStorage.getItem(this.REFRESH_KEY);
      const currentTime = Date.now().toString();

      if (lastLoadTime) {
        // If REFRESH_KEY exists, it's a refresh
        sessionStorage.clear();
      }
      
      // Set new timestamp
      sessionStorage.setItem(this.REFRESH_KEY, currentTime);
    }
  }
} 