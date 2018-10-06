import { Injectable } from '@angular/core';

@Injectable()
export class Configuration {
    public Server = 'http://192.168.100.33:8080/';
    public ServerService = 'http://192.168.100.33:9090';
    public ApiUrl = 'fichasordens-web';
    public ServerWithApiUrl = this.Server + this.ApiUrl;
    public ServerServiceWithApiUrl = this.ServerService + this.ApiUrl;
}
