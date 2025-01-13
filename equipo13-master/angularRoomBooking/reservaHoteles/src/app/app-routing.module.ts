import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsersListarComponent } from './users-listar/users-listar.component';
import { HotelsListarComponent } from './hotels-listar/hotels-listar.component';
import { BookingsListarComponent } from './bookings-listar/bookings-listar.component';
import { AutentificacionComponent } from './autentificacion/autentificacion.component';
import { authGuard } from './autentificacion/auth.guard';

const routes: Routes = [
  { path: 'users', component: UsersListarComponent, canActivate: [authGuard] }, // Ruta para el componente listar usuarios
  { path: 'hotels', component: HotelsListarComponent, canActivate: [authGuard] }, // Ruta para el componente listar hoteles
  { path: 'bookings', component: BookingsListarComponent, canActivate: [authGuard] }, // Ruta para el componente listar reservas
  { path: 'login', component: AutentificacionComponent }, // Ruta para el componente autentificacion

  { path: '**', redirectTo: 'login', pathMatch: 'full' } // Redirección para rutas no encontradas
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
