import { Component, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

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

  constructor(private router: Router) {}
  @ViewChild('passwordInput') passwordInput!: ElementRef;



  focusPassword(event: Event) {
    const keyboardEvent = event as KeyboardEvent;
    keyboardEvent.preventDefault(); // Prevent form submission
    this.passwordInput.nativeElement.focus();
  }

  onSignUpClick() {
    this.isRightPanelActive = true;
  }

  onSignInClick() {
    this.isRightPanelActive = false;
  }

  onLogin() {
    if (this.loginEmail === 'admin' && this.loginPassword === 'password') {
      this.router.navigate(['/role-selection']);
    } else {
      this.errorMessage = 'Invalid credentials';
    }
  }
}
