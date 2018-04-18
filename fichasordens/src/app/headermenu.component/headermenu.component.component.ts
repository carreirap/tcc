import { Component, OnInit, ViewChild, ElementRef, Renderer } from '@angular/core';
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

  ngOnInit() {
  }

  isUserLogger() {
    this.user = JSON.parse(localStorage.getItem('currentUser')).usuario;
    return this.user != null;
  }

  constructor(private el: ElementRef, private renderer: Renderer) {
  }

  onMenuClick() {
    this.el.nativeElement.querySelector('.navbar-toggler').click();
  }

}
