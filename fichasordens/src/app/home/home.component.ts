import { Component, Input, OnInit } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { Headers, Http, RequestOptions } from '@angular/http';
import { ActivatedRoute } from '@angular/router';
import { AuthenticationService, DataService } from '../_services';

// import { UserService } from '../_services/index';
import { Router } from '@angular/router';
import { UsuarioLogado } from '../_models/usuario-logado';
import { Dashboard } from '../_models/dashboad';

@Component({
    moduleId: module.id,
    templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit {
    // users: User[] = [];
    dashboadFicha: Dashboard;
    dashboardOrdem: Dashboard;

    totalOrdemTrabalhando: number;
    totalOrdemFechado: number;
    totalOrdemFinalizado: number;
    totalOrdemFaturado: number;

    totalFichaTrabalhando: number;
    totalFichaFechado: number;
    totalFichaFaturado: number;
    totalFichaFinalizado

    user = new UsuarioLogado();
    // tslint:disable-next-line:no-input-rename
    @Input() loggedInUser: string;
    constructor(private service: DataService) {
        this.dashboadFicha = new Dashboard();
        this.dashboardOrdem = new Dashboard();
    }

    ngOnInit() {
        this.service.get('/dashboard/allFichas').subscribe(response => {
            // console.log(response);
            this.setValues(response);
        }, (error) => {
            console.log('error in', error.error.mensagem);
        });

        this.service.get('/dashboard/allOrdens').subscribe(response => {
            // console.log(response);
            this.setValuesOrdem(response);
        }, (error) => {
            console.log('error in', error.error.mensagem);
        });
    }


    setValues(data) {
        this.dashboadFicha.qtqAberta = data.Aberto.qtd;
        this.dashboadFicha.qtdFechada = data.Fechado.qtd
        this.dashboadFicha.qtdTrabalhando = data.Trabalhando.qtd;
        this.dashboadFicha.qtdAguardando = data.Aguardando.qtd;
        this.dashboadFicha.qtdCancelado = data.Cancelado.qtd;
        this.dashboadFicha.qtdFinalizado = data.Finalizado.qtd;
        this.dashboadFicha.qtdFaturado = data.Faturado.qtd;

        this.totalFichaFechado = data.Fechado.valor;
        this.totalFichaTrabalhando = data.Trabalhando.valor;
        this.totalFichaFinalizado = data.Finalizado.valor;
        this.totalFichaFaturado = data.Faturado.valor;
    }

    setValuesOrdem(data) {
        this.dashboardOrdem.qtqAberta = data.Aberto.qtd;
        this.dashboardOrdem.qtdFechada = data.Fechado.qtd;
        this.dashboardOrdem.qtdTrabalhando = data.Trabalhando.qtd;
        this.dashboardOrdem.qtdAguardando = data.Aguardando.qtd;
        this.dashboardOrdem.qtdCancelado = data.Cancelado.qtd;
        this.dashboardOrdem.qtdFinalizado = data.Finalizado.qtd;
        this.dashboardOrdem.qtdFaturado = data.Faturado.qtd;

        this.totalOrdemFechado = data.Fechado.valor;
        this.totalOrdemTrabalhando = data.Trabalhando.valor;
        this.totalOrdemFinalizado = data.Finalizado.valor;
        this.totalOrdemFaturado = data.Faturado.valor;
    }
    /*getUpdatedUser(): void {
        this.user.usuario = JSON.parse(localStorage.getItem('currentUser')).usuario;
        this.backEndService.getUpdatedUser(this.user).subscribe(response => {
          this.loggedInUser = response.usuario;
        }, (error) => {
          console.log('error in', error);
        });
    }*/
}
