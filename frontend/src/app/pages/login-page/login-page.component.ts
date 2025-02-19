import { Component, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {
  isRightPanelActive = false;
  loginEmail = '';
  loginPassword = '';
  errorMessage = '';

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}
  
  @ViewChild('passwordInput') passwordInput!: ElementRef;

  focusPassword(event: Event) {
    const keyboardEvent = event as KeyboardEvent;
    keyboardEvent.preventDefault();
    this.passwordInput.nativeElement.focus();
  }

  onSignUpClick() {
    this.isRightPanelActive = true;
  }

  onSignInClick() {
    this.isRightPanelActive = false;
  }

  onLogin() {
    this.errorMessage = '';
    this.authService.login(this.loginEmail, this.loginPassword)
      .subscribe({
        next: () => {
          this.router.navigate(['/role-selection']);
        },
        error: () => {
          this.errorMessage = 'Invalid credentials';
        }
      });
  }
}
