import { Injectable } from '@angular/core';
import { UsuarioLogado } from '../_models/usuario-logado';

@Injectable()
export class PapelUserService {
    papel: string;
    user: UsuarioLogado;

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

  private newMethod(): string {
    return 'currentUser';
  }

  getUsuario() {
    const user = new UsuarioLogado();
    user.usuario = JSON.parse(localStorage.getItem('currentUser')).usuario;
    return user;
  }
}
