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
        this.dashboadFicha.qtqAberta = data.Aberto;
        this.dashboadFicha.qtdFechada = data.Fechado
        this.dashboadFicha.qtdTrabalhando = data.Trabalhando;
        this.dashboadFicha.qtdAguardando = data.Aguardando;
        this.dashboadFicha.qtdCancelado = data.Cancelado;
        this.dashboadFicha.qtdFinalizado = data.Finalizado;
        this.dashboadFicha.qtdFaturado = data.Faturado;
    }

    setValuesOrdem(data) {
        this.dashboardOrdem.qtqAberta = data.Aberto;
        this.dashboardOrdem.qtdFechada = data.Fechado
        this.dashboardOrdem.qtdTrabalhando = data.Trabalhando;
        this.dashboardOrdem.qtdAguardando = data.Aguardando;
        this.dashboardOrdem.qtdCancelado = data.Cancelado;
        this.dashboardOrdem.qtdFinalizado = data.Finalizado;
        this.dashboardOrdem.qtdFaturado = data.Faturado;
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
