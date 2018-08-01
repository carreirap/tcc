/*import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';
import { HttpModule } from '@angular/http';

// used to create fake backend
import { fakeBackendProvider } from './_helpers/index';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { BaseRequestOptions } from '@angular/http';

import { AppComponent }  from './app.component';


import { AuthGuard } from './_guards/index';
import { AuthenticationService, UserService } from './_services/index';
import { LoginComponent } from './login/index';
import { HomeComponent } from './home/index';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        routing
    ],
    declarations: [
        AppComponent,
        LoginComponent,
        HomeComponent
    ],
    providers: [
        AuthGuard,
        AuthenticationService,
        UserService,

        // providers used to create fake backend
        fakeBackendProvider,
        MockBackend,
        BaseRequestOptions
    ],
    bootstrap: [AppComponent]
})*/


import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { HttpModule } from '@angular/http';
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { routing } from './app.routing';

import { AppComponent } from './app.component';
import { AuthGuard } from './_guards';
import { AuthenticationService } from './_services';
// import { fakeBackendProvider } from './_helpers';
// import { MockBackend, MockConnection } from '@angular/http/testing';
import { BaseRequestOptions } from '@angular/http';
import { LoginComponent } from './login';
import { HomeComponent } from './home';
import { DataService, CustomInterceptor } from './_services/http.service';
import { Configuration } from './app.constants';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeadermenuComponentComponent } from './headermenu.component/headermenu.component.component';
import { DropdownDirective } from './headermenu.component/appDropdown';
import { PerfilComponent } from './perfil-component/perfil.component';
import { PerfilTableComponent } from './perfil-component/perfil-table.component';
import { ClienteComponentComponent } from './cliente-component/cliente-component.component';
import { DadosempresaComponentComponent } from './dadosempresa-component/dadosempresa-component.component';
import { FichaAtendimentoComponentComponent } from './ficha-atendimento-component/ficha-atendimento-component.component';
import { PerfiService } from './perfil-component/perfil-service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToasterModule, ToasterService} from 'angular5-toaster';
import { ParametroComponent } from './parametro/parametro.component';
import { CpfCnpjDirective } from './util/cnpj-cpf-validator';
import { PesquisarDirective } from './util/cliente-search';
import {TableModule} from 'primeng/table';
import {PaginatorModule} from 'primeng/paginator';
import {AccordionModule} from 'primeng/accordion';     // accordion and accordion tab
import {DataViewModule} from 'primeng/dataview';
// import {MenuItem} from 'primeng/api';
import { OrdemServicoComponent } from './ordem-servico/ordem-servico.component';                 // api
import { DatePipe } from '@angular/common';
import { ModalPesquisaClienteComponent } from './modal-pesquisa-cliente/modal-pesquisa-cliente.component';
import { ModalMaoobraComponent } from './modal-maoobra/modal-maoobra.component';
import { ModalService } from './modal-maoobra/modal-service';
import { PecaServicoOrdemService } from './ordem-servico/ordem-servico-service';
import { ModalClienteService } from './modal-pesquisa-cliente/modal-cliente-service';
import { PapelUserService } from './_services/papel-service';
import { UserService } from './_services/user.service';
import {RadioButtonModule} from 'primeng/radiobutton';
import { ModalAtendimentoComponent } from './modal-atendimento/modal-atendimento.component';
import { ModalAtendimentoService } from './modal-atendimento/modal-atendimento-service';
import { PanelModule } from 'primeng/panel';
import { ListagemComponent } from './home/listagem.component';
import { FichaAtendimentoService } from './ficha-atendimento-component/ficha-atendimento-service';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    HeadermenuComponentComponent,
    PerfilComponent,
    PerfilTableComponent,
    ClienteComponentComponent,
    DadosempresaComponentComponent,
    FichaAtendimentoComponentComponent,
    ParametroComponent,
    CpfCnpjDirective,
    PesquisarDirective,
    OrdemServicoComponent,
    ModalPesquisaClienteComponent,
    ModalMaoobraComponent,
    ModalAtendimentoComponent,
    ListagemComponent
   ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    HttpClientModule,
    NgbModule.forRoot(),
    TableModule,
    PaginatorModule,
    AccordionModule,
    PanelModule,
    DataViewModule,
    RadioButtonModule,
    routing,
    BrowserAnimationsModule, ToasterModule

  ],
  providers: [
        AuthGuard,
        AuthenticationService,
        PerfiService,
        ToasterService,
        DataService,
        FormBuilder,
        // providers used to create fake backend
        // fakeBackendProvider,
        // MockBackend,
        BaseRequestOptions,
        HttpClientModule,
        Configuration,
        DatePipe,
        { provide: HTTP_INTERCEPTORS, useClass: CustomInterceptor, multi: true },
        ModalService,
        ModalClienteService,
        ModalAtendimentoService,
        PecaServicoOrdemService,
        FichaAtendimentoService,
        PapelUserService,
        UserService
  ],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
  bootstrap: [AppComponent]
})

export class AppModule { }
