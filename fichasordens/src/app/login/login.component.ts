import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as jwt_decode from 'jwt-decode';
import { FormsModule } from '@angular/forms';
import { User } from '../_models/index';


import { AuthenticationService } from '../_services/index';

@Component({
    moduleId: module.id,
    templateUrl: 'login.component.html'
})

export class LoginComponent implements OnInit {
    model: any = {};
    loading = false;
    error = '';
    user: User;

    constructor(
        private router: Router,
        private authenticationService: AuthenticationService) {
    }

    ngOnInit() {
        // reset login status
        this.authenticationService.logout();
    }

    login(): void {
        this.loading = true;
        this.user = new User();
        this.user.usuario = this.model.usuario;
        this.user.senha = this.model.senha;
        this.authenticationService.authenticate(this.user)
            .map(res => res.json()).subscribe(response => {
                this.authenticationService.accessToken = response.access_token;
                console.log(response);
                const papel = this.authenticationService.getRule();
                localStorage.setItem('currentUser', JSON.stringify({usuario: this.user.usuario,
                    token: response.access_token, role: papel
                 }));
                this.router.navigateByUrl('/home');
            }, (error) => {
                console.log('error in', error);
                this.error = 'Usuario ou senha incorretos';
                this.loading = false;
        });
    }
}
