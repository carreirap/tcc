import { Injectable } from '@angular/core';
import { User } from '../_models';

@Injectable()
export class PapelUserService {
    papel: string;
    user: User;

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
    const user = new User();
    user.usuario = JSON.parse(localStorage.getItem('currentUser')).usuario;
    return user;
  }
}
