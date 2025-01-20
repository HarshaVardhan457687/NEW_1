import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FeatureToggleService {
  private enabledFeatures: Set<string> = new Set([
    // Add your default enabled features here
    'dashboard',
    'metrics'
  ]);

  isFeatureEnabled(featureName: string): boolean {
    return this.enabledFeatures.has(featureName);
  }

  enableFeature(featureName: string): void {
    this.enabledFeatures.add(featureName);
  }

  disableFeature(featureName: string): void {
    this.enabledFeatures.delete(featureName);
  }
} 