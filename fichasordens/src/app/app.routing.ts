import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './login';
import { HomeComponent } from './home';
import { AuthGuard } from './_guards';
import { HeadermenuComponentComponent } from './headermenu.component/headermenu.component.component';
import { PerfilComponent } from './perfil-component/perfil.component';
import { ClienteComponentComponent } from './cliente-component/cliente-component.component';
import { DadosempresaComponentComponent } from './dadosempresa-component/dadosempresa-component.component';
import { FichaAtendimentoComponentComponent } from './ficha-atendimento-component/ficha-atendimento-component.component';
import { ParametroComponent } from './parametro/parametro.component';
import { OrdemServicoComponent } from './ordem-servico/ordem-servico.component';
import { ListagemComponent } from './home/listagem.component';
import { HistoricoComponent } from './historico/historico.component';

const appRoutes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
    { path: 'about', component: HomeComponent, canActivate: [AuthGuard] },
    { path: 'perfil', component: PerfilComponent, canActivate: [AuthGuard] },
    { path: 'cliente', component: ClienteComponentComponent, canActivate: [AuthGuard] },
    { path: 'param', component: ParametroComponent, canActivate: [AuthGuard]},
    { path: 'dadosempresa', component: DadosempresaComponentComponent, canActivate: [AuthGuard]},
    { path: 'fichas/:id', component: FichaAtendimentoComponentComponent, canActivate: [AuthGuard]},
    { path: 'fichas', component: FichaAtendimentoComponentComponent, canActivate: [AuthGuard]},
    { path: 'historico', component: HistoricoComponent, canActivate: [AuthGuard]},
    { path: 'ordem', component: OrdemServicoComponent, canActivate: [AuthGuard]},
    { path: 'ordem/:id', component: OrdemServicoComponent, canActivate: [AuthGuard]},
    { path: 'listaDashboard/:situacao/:tipo', component: ListagemComponent, canActivate: [AuthGuard]},
    { path: '', component: HomeComponent, canActivate: [AuthGuard] },

    // otherwise redirect to homed
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);
