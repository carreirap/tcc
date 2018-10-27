import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from '../../../node_modules/rxjs';
import { DataService } from '../_services';
import { ToasterService } from 'angular5-toaster';
import { Listagem } from './lista';
import { Configuration } from '../app.constants';


@Component({
    selector: 'listagem-component',
    templateUrl: 'listagem.component.html'
    // styleUrls: ['./cliente-component.component.css']
  })

export class ListagemComponent implements OnInit {
    param: any;
    paramTipo: any;
    lista: any[];
    ngOnInit(): void {
        this.lista = new Array();
        this.route.params.subscribe(
            params => {
                this.param = params['situacao'];
                this.paramTipo = params['tipo'];
        });
        if (this.paramTipo === 'Ficha') {
            this.service.get('/ficha?situacao=' + this.param).subscribe(response => {
                this.loadData(response);
            }, (error) => {
                console.log('error in', error);
                // this.toasterService.pop('error', 'Empresa', error.mensagem);
            });
        } else {
            this.service.get('/ordem?situacao=' + this.param).subscribe(response => {
                this.loadData(response);
            }, (error) => {
                console.log('error in', error);
                // this.toasterService.pop('error', 'Empresa', error.mensagem);
            });
        }

    }

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private service: DataService, toasterService: ToasterService,
        private _configuration: Configuration
      ) {}

    loadData(data) {
        for(let i=0; i < data.length; i++) {
           let l:Listagem = new Listagem();
           l.numero = data[i].id;
           l.cliente = data[i].nomeCliente;
           l.dias = data[i].dias;
           l.responsavel = data[i].responsavel;
           l.situacao = data[i].situacao;
           l.tipo = data[i].tipoServico;
           if (data[i].situacao !== 'Fechado')
                l.alerta = data[i].alerta;
           this.lista.push(l) 
        }
    }  

    openServico(event: Event, ficha: Listagem) {
        console.log(ficha);
        this.router.navigate(['/ficha', ficha.numero]);
    }
}

