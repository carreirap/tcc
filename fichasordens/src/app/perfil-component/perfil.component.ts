import { Component, OnInit } from '@angular/core';
import { User } from '../_models';
import { PerfiService } from './perfil-service';
import { ToasterService} from 'angular5-toaster';
import * as CryptoJS from 'crypto-js';
import { FormsModule } from '@angular/forms';
import { PapelUserService } from '../_services/papel-service';

@Component({
  selector: 'app-perfil-component',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css'
  ]
})
export class PerfilComponent implements OnInit {
  private toasterService: ToasterService;
  formUsuario: User = new User();

  operacao: String;
  alteraSenha: Boolean;
  mostrarLink: Boolean;
  iReadOnly: Boolean;

  optionsSelect: Array<any>;

  constructor(private perfil: PerfiService, toasterService: ToasterService, private papelUserService: PapelUserService) {
    this.operacao = 'inserir';
    this.alteraSenha = false;
    this.mostrarLink = false;
    this.toasterService = toasterService;
    this.iReadOnly = false;
  }

  ngOnInit() {
    this.optionsSelect = [
      { value: 'Admin', label: 'Administrador' },
      { value: 'User', label: 'Usuário' }
  ];
  }

  isAtivo() {
    if (this.papelUserService.isAdmin() === true) {
      return true;
    } else {
      return this.mostrarLink;
    }
  }

  isOperacaoInserir() {
    if (this.operacao === 'inserir') {
      return false;
    } else if (this.operacao === 'alterar' && this.alteraSenha === true) {
      return true;
    } else {
      return false;
    }

  }

  isAlterarSenha() {
    if (this.operacao === 'inserir') {
      return true;
    } else {
      return this.alteraSenha;
    }
  }
  mostrarCampos() {
    this.alteraSenha = true;
  }

  mostrarLinkSenha() {
    return this.mostrarLink;
  }

  onSubmit() {
    console.log(this.formUsuario);

    if (this.operacao === 'inserir') {
      // console.log('cripto: ' + CryptoJS.enc.Base64.parse(this.formUsuario.novaSenha).toString());
      this.formUsuario.novaSenha = CryptoJS.enc.Base64.parse(this.formUsuario.novaSenha);
      this.perfil.post(this.formUsuario).subscribe(response => {
        console.log(response);
        this.operacao = 'inserir';
        this.toasterService.pop('success', 'Usuário', 'Usuário cadastrado com sucesso!');
      }, (error) => {
        console.log('error in', error);
        this.toasterService.pop('error', 'Usuário', JSON.parse(error._body).mensagem);
      });
    } else {
      // this.formUsuario.novaSenha = CryptoJS.enc.Base64.parse(this.formUsuario.novaSenha).toString();
      console.log('cripto: ' + this.formUsuario.novaSenha);
      this.perfil.put(this.formUsuario).subscribe(response => {
        console.log(response);
        this.toasterService.pop('success', 'Usuário', 'Usuário alterado com sucesso!');
      }, (error) => {
        console.log('error in', error);
        this.toasterService.pop('error', 'Usuário', JSON.parse(error._body).mensagem);
      });
    }
  }

  loadUsuario(userLine: any) {
    console.log(userLine);
    this.formUsuario.usuario = userLine.usuario;
    this.formUsuario.nome = userLine.nome;
    this.formUsuario.id = userLine.id;
    this.formUsuario.situacao = userLine.situacao;
    this.formUsuario.papel = userLine.papel;
    this.operacao = 'alterar';
    this.alteraSenha = false;
    this.mostrarLink = true;
    if (this.papelUserService.isAdmin() === true) {
      this.iReadOnly = false;
    } else {
      this.iReadOnly = true;
    }
  }

}
