import { Component, Output, EventEmitter, ElementRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-nav-options',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './nav-options.component.html',
  styleUrl: './nav-options.component.scss'
})
export class NavOptionsComponent {
  @Output() close = new EventEmitter<void>();
  @Output() profileClick = new EventEmitter<void>();
  private isInitialized = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private elementRef: ElementRef
  ) {}

  ngAfterViewInit() {
    // Delay setting the initialized flag to prevent immediate closing
    setTimeout(() => {
      this.isInitialized = true;
    }, 100);
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: Event) {
    // Only process click outside events after initialization
    if (this.isInitialized && !this.elementRef.nativeElement.contains(event.target)) {
      this.close.emit();
    }
  }

  @HostListener('click', ['$event'])
  onClick(event: Event) {
    // Prevent clicks within the component from bubbling up
    event.stopPropagation();
  }

  viewProfile(): void {
    this.profileClick.emit();
    this.close.emit();
  }

  logout(): void {
    this.authService.logout();
    // Refresh the browser after logout
    window.location.reload();
  }

  closeOptions(): void {
    this.close.emit();
  }
}
