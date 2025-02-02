import { Component, Inject, Renderer2 } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { DOCUMENT } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `<router-outlet></router-outlet>`,
  host: { 'id': 'app-root-component' }
})
export class AppComponent {
  constructor(
    @Inject(DOCUMENT) private document: Document,
    protected renderer: Renderer2
  ) {
    this.renderer.setStyle(document.body, 'overflow', 'hidden');
  }
}
