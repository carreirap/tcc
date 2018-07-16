import { Injectable } from '@angular/core';
import * as jwt_decode from 'jwt-decode';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Base64 } from 'js-base64';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { User } from '../_models/index';
import { Subscription } from 'rxjs/Subscription';

import { Configuration } from '../app.constants';
import { PapelUserService } from '../_services/papel-service';

@Injectable()
export class PerfiService {
    headers: Headers;
    options: RequestOptions;

    constructor(private http: Http, private _configuration: Configuration, private papelUserService: PapelUserService) {
    }

    public get() {
        console.log(JSON.parse(localStorage.getItem('currentUser')).token);
        this.headers = new Headers({
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('currentUser')).token
        });
        console.log(this.headers);
        this.options = new RequestOptions({ headers: this.headers });
        let path = '/usuario';
        if (this.papelUserService.isAdmin() === false) {
            const user = this.papelUserService.getUsuario();
            console.log(user);
            path = path + '?user=' + user.usuario;
        }
        return this.http.get(this._configuration.ServerService +
            path, this.options)
            .map((response: Response) => response.json());
    }

    post(user: User) {
        this.headers = new Headers({
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('currentUser')).token
        });
        return this.http.post(this._configuration.ServerService +
            '/usuario', user, this.options)
            .map((response: Response) => response.json());
    }

    put(user: User) {
        this.headers = new Headers({
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('currentUser')).token
        });
        return this.http.put(this._configuration.ServerService +
            '/usuario', user, this.options)
            .map((response: Response) => response.json());
    }
}
