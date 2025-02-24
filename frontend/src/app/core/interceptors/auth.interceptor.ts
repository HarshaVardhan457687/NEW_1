import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('auth_token');

  const authUrls = ['/api/auth/authenticate', '/api/auth/register'];
  const isAuthRequest = authUrls.some(url => req.url.includes(url));
  
  if (token && !isAuthRequest) {
    const clonedRequest = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(clonedRequest);
  }
  return next(req);
};
