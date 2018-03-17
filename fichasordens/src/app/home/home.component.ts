import { Component, Input, OnInit } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { Headers, Http, RequestOptions } from '@angular/http';
import {ActivatedRoute} from '@angular/router';
import {AuthenticationService} from '../_services/index';

import { User } from '../_models/index';
// import { UserService } from '../_services/index';
import { Router } from '@angular/router';

@Component({
    moduleId: module.id,
    templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit {
    // users: User[] = [];
    user = new User();
    @Input('loggedInUser') loggedInUser: string;
    constructor(private router: Router, private backEndService: AuthenticationService) { }

    ngOnInit() {
        // get users from secure api end point
        /*this.userService.getUsers()
            .subscribe(users => {
                this.users = users;
            }); */
    }

    // tslint:disable-next-line:member-ordering

    getUpdatedUser(): void {
        this.user.usuario = JSON.parse(localStorage.getItem('currentUser')).usuario;
        this.backEndService.getUpdatedUser(this.user).subscribe(response => {
          this.loggedInUser = response.usuario;
        }, (error) => {
          console.log('error in', error);
        });
      }
}
