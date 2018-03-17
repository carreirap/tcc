import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Base64 } from 'js-base64';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { DataService } from './http.service';
import { User } from '../_models/index';

@Injectable()
export class AuthenticationService {
    public accessToken: string;

    url: string;
    headers: Headers;
    options: RequestOptions;
    creds: String;
    updatedUser: string;

    constructor(private http: Http, private router: Router) {
        // constructor(private http: DataService) {
        // set token if saved in local storage
        // const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        // this.token = currentUser && currentUser.token;
    }

    /*login(usuario: string, senha: string): Observable<boolean> {
        return this.http.add(JSON.stringify({ usuario: usuario, senha: senha }))
            .map((response: Response) => {
                // login successful if there's a jwt token in the response
                alert(response);
                const token = response.json() && response.json().token;
                if (token) {
                    // set token property
                    this.token = token;

                    // store username and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify({ usuario: usuario, token: token }));

                    // return true to indicate successful login
                    return true;
                } else {
                    // return false to indicate failed login
                    return false;
                }
            });
    }*/

    authenticate(user: User) {
        this.url = 'http://localhost:8080/auth/oauth/token';
        this.headers = new Headers({
          'Content-Type': 'application/x-www-form-urlencoded',
          'Authorization': 'Basic ' + Base64.encode(user.usuario + ':' + user.senha)
        });
        this.options = new RequestOptions({ headers: this.headers });
        this.creds = 'grant_type=client_credentials';
        this.http.post(this.url, this.creds, this.options)
          .map(res => res.json()).subscribe(response => {
            this.accessToken = response.access_token;
            localStorage.setItem('currentUser', JSON.stringify({usuario: user.usuario, token: response.access_token }));
            this.router.navigateByUrl('/home');
          }, (error) => {
            console.log('error in', error);
            // this.error = 'Usuario ou senha incorretos';
            // this.loading = false;
          });
      }

    logout(): void {
        // clear token remove user from local storage to log user out
        this.accessToken = null;
        localStorage.removeItem('currentUser');
    }


    getUpdatedUser(user: User): Observable<User> {
        alert(user.usuario);
        this.url = 'http://localhost:9090/getUpdatedUser';
        this.headers = new Headers({
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('currentUser')).token
        });
        this.options = new RequestOptions({ headers: this.headers });
        return this.http.post(this.url, user, this.options)
          .map(res => res.json());
      }
}
