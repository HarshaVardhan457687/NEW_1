import { Component } from '@angular/core';
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
