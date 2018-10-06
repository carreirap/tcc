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
    timer: any;
    dashboadFicha: Dashboard;
    dashboardOrdem: Dashboard;

    totalOrdemTrabalhando: number;
    totalOrdemFechado: number;
    totalOrdemFinalizado: number;
    totalOrdemFaturado: number;

    totalFichaTrabalhando: number;
    totalFichaFechado: number;
    totalFichaFaturado: number;
    totalFichaFinalizado: number;

    alertaOrdemAberto: Boolean = false;
    alertaOrdemTrabalhando: Boolean = false;
    alertaOrdemAguardando: Boolean = false;
    alertaOrdemFinalizado: Boolean = false;
    alertaOrdemFaturado: Boolean = false;

    alertaFichaAberto: Boolean = false;
    alertaFichaTrabalhando: Boolean = false;
    alertaFichaAguardando: Boolean = false;
    alertaFichaFinalizado: Boolean = false;
    alertaFichaFaturado: Boolean = false;

    user = new UsuarioLogado();
    // tslint:disable-next-line:no-input-rename
    @Input() loggedInUser: string;
    constructor(private service: DataService) {
        this.dashboadFicha = new Dashboard();
        this.dashboardOrdem = new Dashboard();
    }

    ngOnInit() {
        this.timer = 1;
        this.refreshData();
        setInterval(() => {
            this.refreshData();
        }, 30000);
    }

    // tslint:disable-next-line:use-life-cycle-interface
    ngOnDestroy() {
        this.timer = undefined;
    }
    refreshData() {
        if (this.timer !== undefined) {
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
        this.totalFichaTrabalhando = data.Trabalhando.valor + data.Aguardando.valor;
        this.totalFichaFinalizado = data.Finalizado.valor;
        this.totalFichaFaturado = data.Faturado.valor;

        if (data.Aberto.alerta === 'S') {
            this.alertaFichaAberto =  true;
        } else {
            this.alertaFichaAberto =  false;
        }
        if (data.Trabalhando.alerta === 'S') {    
            this.alertaFichaTrabalhando = true; 
        } else {
            this.alertaFichaTrabalhando = false; 
        }    
        if (data.Aguardando.alerta === 'S') {
            this.alertaFichaAguardando = true;
        } else {
            this.alertaFichaAguardando = false;
        } 
        if (data.Finalizado.alerta === 'S') {       
            this.alertaFichaFinalizado = true; 
        } else {
            this.alertaFichaFinalizado = false; 
        }
        if (data.Faturado.alerta === 'S') {
            this.alertaFichaFaturado = true;
        } else {
            this.alertaFichaFaturado = false;
        }
    }

    setValuesOrdem(data) {
        this.dashboardOrdem.qtqAberta = data.Aberto.qtd;
        this.dashboardOrdem.qtdFechada = data.Fechado.qtd;
        this.dashboardOrdem.qtdTrabalhando = data.Trabalhando.qtd;
        this.dashboardOrdem.qtdAguardando = data.Aguardando.qtd;
        this.dashboardOrdem.qtdCancelado = data.Cancelado.qtd;
        this.dashboardOrdem.qtdFinalizado = data.Finalizado.qtd;
        this.dashboardOrdem.qtdFaturado = data.Faturado.qtd;

        if (data.Aberto.alerta === 'S') {
            this.alertaOrdemAberto = true;
        } else {
            this.alertaOrdemAberto = false;
        }

        if (data.Trabalhando.alerta === 'S') {
            this.alertaOrdemTrabalhando = true; 
        } else {
            this.alertaOrdemTrabalhando = false;
        }

        if (data.Aguardando.alerta === 'S') {
            this.alertaOrdemAguardando = true; 
        } else {
            this.alertaOrdemAguardando = false;
        }
            
        if (data.Finalizado.alerta === 'S') {
            this.alertaOrdemFinalizado = true; 
        } else {
            this.alertaOrdemFinalizado = false;
        }
        
        if (data.Faturado.alerta === 'S') {
            this.alertaOrdemFaturado = true; 
        } else {
            this.alertaOrdemFaturado = false;
        }

        this.totalOrdemFechado = data.Fechado.valor;
        this.totalOrdemTrabalhando = data.Trabalhando.valor + data.Aguardando.valor;
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
