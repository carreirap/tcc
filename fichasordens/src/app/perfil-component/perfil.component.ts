import { Component, OnInit } from '@angular/core';
import { User } from '../_models';
import { PerfiService } from './perfil-service';

@Component({
  selector: 'app-perfil-component',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {

  formUsuario: User = new User();

  operacao: String;

  optionsSelect: Array<any>;

  constructor(private perfil: PerfiService) {
    this.operacao = 'inserir';
   }

  ngOnInit() {
    this.optionsSelect = [
      { value: 'Admin', label: 'Administrador' },
      { value: 'User', label: 'Usuário' }
  ];
  }

  isOperacaoInserir() {
    if (this.operacao === 'inserir') {
      return false;
    } else {
      return true;
    }

  }

  onSubmit() {
    console.log(this.formUsuario);

    if (this.operacao === 'inserir') {
      this.perfil.post(this.formUsuario).subscribe(response => {
        console.log(response);
        this.operacao = 'inserir';
      });
    } else {
      this.perfil.post(this.formUsuario).subscribe(response => {
        console.log(response);
        this.operacao = 'inserir';
      });
    }
  }

  loadUsuario(userLine: any) {
    console.log('no parent');
    console.log(userLine);
    this.formUsuario.usuario = userLine.usuario;
    this.formUsuario.nome = userLine.nome;
    this.formUsuario.id = userLine.id;
    this.formUsuario.situacao = userLine.situacao;
    this.formUsuario.papel = userLine.papel;
    this.operacao = 'alterar';
  }

}
