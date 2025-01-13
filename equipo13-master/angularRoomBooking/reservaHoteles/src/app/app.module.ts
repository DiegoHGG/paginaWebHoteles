import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UsersListarComponent } from './users-listar/users-listar.component';
import { ClienteApiRestService } from './shared/cliente-api-rest.service';
import { DataService } from './shared/data.service';
import { FormsModule } from '@angular/forms';
import { HotelsListarComponent } from './hotels-listar/hotels-listar.component';
import { BookingsListarComponent } from './bookings-listar/bookings-listar.component';
import { AutentificacionComponent } from './autentificacion/autentificacion.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './autentificacion/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    UsersListarComponent,
    HotelsListarComponent,
    BookingsListarComponent,
    AutentificacionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,  
    FormsModule        
  ],
  providers: [
    ClienteApiRestService,
    DataService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },  
  ],
  bootstrap: [AppComponent]  
})
export class AppModule { }
