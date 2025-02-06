import { Component, Inject, Renderer2 } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { DOCUMENT, AsyncPipe, CommonModule } from '@angular/common';
import { ModalService } from './core/services/modal.service';
import { ProfilePageComponent } from './pages/profile-page/profile-page.component';
import { AssignTaskCardComponent } from './shared/assign-task-card/assign-task-card.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet, 
    ProfilePageComponent, 
    AssignTaskCardComponent,
    AsyncPipe, 
    CommonModule
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  isProfileModalOpen$;
  modalConfig$;

  constructor(
    @Inject(DOCUMENT) private document: Document,
    protected renderer: Renderer2,
    private modalService: ModalService
  ) {
    this.renderer.setStyle(document.body, 'overflow', 'hidden');
    this.isProfileModalOpen$ = this.modalService.isProfileModalOpen$;
    this.modalConfig$ = this.modalService.modalConfig$;
  }
}
