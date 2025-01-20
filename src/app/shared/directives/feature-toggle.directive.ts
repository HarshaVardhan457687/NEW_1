import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { FeatureToggleService } from '@frontend/core/services/feature-toggle.service';

@Directive({
  selector: '[appFeatureToggle]'
})
export class FeatureToggleDirective {
  @Input('appFeatureToggle') featureName: string;

  constructor(
    private readonly templateRef: TemplateRef<any>,
    private readonly viewContainer: ViewContainerRef,
    private readonly featureToggleService: FeatureToggleService
  ) {}

  ngOnInit(): void {
    if (this.featureToggleService.isFeatureEnabled(this.featureName)) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
} 