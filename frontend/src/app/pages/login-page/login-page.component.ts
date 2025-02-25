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
  
  // Sign up form fields
  signupName = '';
  signupEmail = '';
  signupPassword = '';
  confirmPassword = '';
  signupErrorMessage = '';
  
  // Field validation states
  invalidFields: { [key: string]: boolean } = {};
  shake = false;

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
    this.resetErrors();
  }

  onSignInClick() {
    this.isRightPanelActive = false;
    this.resetErrors();
  }

  resetErrors() {
    this.errorMessage = '';
    this.signupErrorMessage = '';
    this.invalidFields = {};
    this.shake = false;
  }

  validateEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  validatePassword(password: string): boolean {
    const passwordRegex = /^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9]).{8,}$/;
    return passwordRegex.test(password);
  }

  onLogin() {
    this.resetErrors();

    // Check for empty fields
    if (!this.loginEmail || !this.loginPassword) {
      this.errorMessage = 'Fields cannot be empty';
      this.invalidFields = {
        username: !this.loginEmail,
        password: !this.loginPassword
      };
      return;
    }

    this.authService.login(this.loginEmail, this.loginPassword)
      .subscribe({
        next: () => {
          this.router.navigate(['/role-selection']);
        },
        error: () => {
          this.errorMessage = 'Invalid credentials';
          this.invalidFields = {
            username: true,
            password: true
          };
          this.shake = true;
          setTimeout(() => this.shake = false, 500);
        }
      });
  }

  onSignUp() {
    this.resetErrors();
    
    // Check for empty fields
    if (!this.signupName || !this.signupEmail || !this.signupPassword || !this.confirmPassword) {
      this.signupErrorMessage = 'Fields cannot be empty';
      this.invalidFields = {
        name: !this.signupName,
        email: !this.signupEmail,
        password: !this.signupPassword,
        confirmPassword: !this.confirmPassword
      };
      return;
    }

    // Validate email
    if (!this.validateEmail(this.signupEmail)) {
      this.signupErrorMessage = 'Enter valid email id';
      this.invalidFields = { email: true };
      return;
    }

    // Validate password
    if (!this.validatePassword(this.signupPassword)) {
      this.signupErrorMessage = 'Password must contain at least 8 characters, one capital letter, and one special character';
      this.invalidFields = { password: true };
      return;
    }

    // Check password match
    if (this.signupPassword !== this.confirmPassword) {
      this.signupErrorMessage = 'Passwords do not match';
      this.invalidFields = {
        password: true,
        confirmPassword: true
      };
      return;
    }


    const userDTO = {
      name: this.signupName,
      userName: this.signupEmail,
      emailId: this.signupEmail,
      password: this.signupPassword
    };

    this.authService.register(userDTO).subscribe({
      next: () => {
        // Reset form and show success message
        this.signupErrorMessage = '';
        this.signupName = '';
        this.signupEmail = '';
        this.signupPassword = '';
        this.confirmPassword = '';
        // Switch to login panel
        this.isRightPanelActive = false;
      },
      error: (error) => {
        this.signupErrorMessage = error.error?.message || 'Registration failed. Please try again.';
      }
    });
  }
}
