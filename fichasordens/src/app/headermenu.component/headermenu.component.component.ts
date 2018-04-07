import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-headermenu-component',
  templateUrl: './headermenu.component.component.html'
})

export class HeadermenuComponentComponent implements OnInit {

  public isCollapsed = true;


  toggleMenu() {
    this.isCollapsed = !this.isCollapsed;
  }

  constructor() { }

  ngOnInit() {
  }

}
