import { Injectable } from '@angular/core';

@Injectable()
export class Configuration {
    public Server = 'http://localhost:8080/';
    public ServerService = 'http://localhost:9090';
    public ApiUrl = 'fichasordens-web';
    public ServerWithApiUrl = this.Server + this.ApiUrl;
    public ServerServiceWithApiUrl = this.ServerService + this.ApiUrl;
}
