import { Injectable } from '@angular/core';
import * as jwt_decode from 'jwt-decode';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Base64 } from 'js-base64';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { UsuarioLogado } from '../_models/usuario-logado';


@Injectable()
export class AuthenticationService {
    public accessToken: string;

    url: string;
    headers: Headers;
    options: RequestOptions;
    creds: String;
    updatedUser: string;
    user: UsuarioLogado;

    constructor(private http: Http, private router: Router) {
        // constructor(private http: DataService) {
        // set token if saved in local storage
        // const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        // this.token = currentUser && currentUser.token;
    }

    getToken(): string {
        return localStorage.getItem('currentUser');
    }

    authenticate(user: UsuarioLogado) {
        this.url = 'http://localhost:8080/auth/oauth/token';
        this.headers = new Headers({
          'Content-Type': 'application/x-www-form-urlencoded',
          'Authorization': 'Basic ' + Base64.encode(user.usuario + ':' + user.senha)
        });
        this.options = new RequestOptions({ headers: this.headers });
        this.creds = 'grant_type=client_credentials';
        return this.http.post(this.url, this.creds, this.options);
      }

    logout(): void {
        // clear token remove user from local storage to log user out
        try {
            this.accessToken = JSON.parse(localStorage.getItem('currentUser')).token;
            if (this.accessToken !== null) {
                location.reload();
                localStorage.removeItem('currentUser');
            }
        } catch (e) {
            console.log('Mensagem de Logout');
        }
    }

    getRule(): String {
       const decoded = jwt_decode(this.accessToken);
       let papel: String = '';
       // tslint:disable-next-line:prefer-const
       for (let aux of decoded.authorities) {
            if (aux === 'ROLE_ADMIN') {
                papel = 'Admin';
                break;
            }
            if (aux === 'ROLE_CLIENT') {
                papel = 'User';
                break;
            }
       }
       return papel;
    }

    getTokenExpirationDate(token: string): Date {
        const decoded = jwt_decode(token);
        if (decoded.exp === undefined) { return null; }
        const date = new Date(0);
        date.setUTCSeconds(decoded.exp);
        return date;

    }

    isTokenExpired(token?: string): boolean {
        if (!token) { token = this.getToken(); }
        if (!token) { return true; }

        const date = this.getTokenExpirationDate(token);
        if (date === undefined) { return false; }
        return !(date.valueOf() > new Date().valueOf());
    }


    public getUpdatedUser(user: UsuarioLogado): Observable<UsuarioLogado> {
        this.url = 'http://localhost:9090/usuario/getUpdatedUser';
        this.headers = new Headers({
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('currentUser')).token
        });
        this.options = new RequestOptions({ headers: this.headers });
        return this.http.post(this.url, user, this.options)
          .map(res => res.json());
    }
}
