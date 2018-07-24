import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthenticationService } from '../_services';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router,
        private authenticationService: AuthenticationService) {
    }

    canActivate() {
        if (this.authenticationService.isTokenExpired()) {
            this.router.navigate(['/login']);
            return false;
        } else {
            return true;
        }
    }
}
