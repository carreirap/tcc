import 'rxjs/add/operator/map';

import { HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Configuration } from '../app.constants';

@Injectable()
export class DataService {

    private actionUrl: string;

    constructor(private http: HttpClient, private _configuration: Configuration) {
        this.actionUrl = _configuration.ServerWithApiUrl + 'usuario/';
    }

    public post<T>(path: String, T) {
        return this.http.post(this._configuration.ServerService +
            path, T);
    }

    public get<T>(path: String) {
        return this.http.get(this._configuration.ServerService +
            path);
    }

    public getAll<T>(): Observable<T> {
        return this.http.get<T>(this.actionUrl);
    }

    public getSingle<T>(id: number): Observable<T> {
        return this.http.get<T>(this.actionUrl + id);
    }

    public add<T>(itemName: string): Observable<T> {
        const toAdd = JSON.stringify({ ItemName: itemName });

        return this.http.post<T>(this.actionUrl, toAdd);
    }

    public update<T>(id: number, itemToUpdate: any): Observable<T> {
        return this.http
            .put<T>(this.actionUrl + id, JSON.stringify(itemToUpdate));
    }

    public delete<T>(id: number): Observable<T> {
        return this.http.delete<T>(this.actionUrl + id);
    }
}


@Injectable()
export class CustomInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!req.headers.has('Content-Type')) {
            req = req.clone({ headers: req.headers.set('Content-Type', 'application/json')});
            req = req.clone({ headers: req.headers.set('Authorization',
                'Bearer ' + JSON.parse(localStorage.getItem('currentUser')).token)});
        }

        req = req.clone({ headers: req.headers.set('Accept', 'application/json') });
        console.log(JSON.stringify(req.headers));
        return next.handle(req);
    }
}
