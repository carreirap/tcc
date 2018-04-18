import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Base64 } from 'js-base64';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { DataService } from './http.service';
import { User } from '../_models/index';
import { Subscription } from 'rxjs/Subscription';

@Injectable()
export class AuthenticationService {
    public accessToken: string;

    url: string;
    headers: Headers;
    options: RequestOptions;
    creds: String;
    updatedUser: string;
    user: User;

    constructor(private http: Http, private router: Router) {
        // constructor(private http: DataService) {
        // set token if saved in local storage
        // const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        // this.token = currentUser && currentUser.token;
    }

    authenticate(user: User) {
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
        this.accessToken = JSON.parse(localStorage.getItem('currentUser')).token;
        if (this.accessToken !== null) {
            location.reload();
            localStorage.removeItem('currentUser');
        }
    }


    getUpdatedUser(user: User): Observable<User> {
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
