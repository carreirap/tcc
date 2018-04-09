import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { User } from '../_models/index';

@Component({
  selector: 'app-headermenu-component',
  templateUrl: './headermenu.component.component.html'
})

export class HeadermenuComponentComponent implements OnInit {
  user: User;

  public isCollapsed = true;


  toggleMenu() {
    this.isCollapsed = !this.isCollapsed;
  }

  constructor() { }

  ngOnInit() {
  }

  isUserLogger() {
    this.user = JSON.parse(localStorage.getItem('currentUser')).usuario;
    //alert(this.user);
    return this.user != null;
}

}
