﻿import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './login/index';
import { HomeComponent } from './home/index';
import { AuthGuard } from './_guards/index';
import { HeadermenuComponentComponent } from './headermenu.component/headermenu.component.component';

const appRoutes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomeComponent },
    { path: 'about', component: HomeComponent },
    { path: 'courses', component: HomeComponent },
    { path: 'menu', component: HeadermenuComponentComponent },

    { path: '', component: HomeComponent, canActivate: [AuthGuard] },

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);
