import { Component, OnInit, ViewChild, ElementRef, Renderer } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UsuarioLogado } from '../_models/usuario-logado';

@Component({
  selector: 'app-headermenu-component',
  templateUrl: './headermenu.component.component.html'
})

export class HeadermenuComponentComponent implements OnInit {
  user: UsuarioLogado;
  papel: String;


  public isCollapsed = true;


  toggleMenu() {
    this.isCollapsed = !this.isCollapsed;
  }

  ngOnInit() {

  }

  isUserLogger() {
    try {
      this.user = JSON.parse(localStorage.getItem(this.newMethod())).usuario;
    } catch (e) {
    }
    this.isAdmin();
    return this.user != null;
  }

  isAdmin() {
    try {
      this.papel = JSON.parse(localStorage.getItem(this.newMethod())).role;
    } catch (e) {
    }
    if (this.papel === 'Admin') {
      return true;
    } else {
      return false;
    }
  }

  constructor(private el: ElementRef, private renderer: Renderer) {
  }

  private newMethod(): string {
    return 'currentUser';
  }

  onMenuClick() {
    this.el.nativeElement.querySelector('.navbar-toggler').click();
  }

}
