import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}
   canActivate(): Observable<boolean> {
      return this.authService.isValidToken().pipe(
        map((isValid : boolean)  => {
          console.log("loggedIn: ",this.authService.isLoggedIn());
          console.log("validToken :" ,isValid);
          if (this.authService.isLoggedIn() && isValid) {
            return true;
          } else {
            this.router.navigate(['/login']);
            return false;
          }
        })
      );
   }
}
