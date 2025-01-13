

import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from './app.model';
import { Router } from '@angular/router';
import { ClienteApiRestService } from './cliente-api-rest.service';
import { isPlatformBrowser } from '@angular/common';
import { Observable } from 'rxjs/internal/Observable';
import { catchError, map } from 'rxjs/operators';
import { throwError } from 'rxjs/internal/observable/throwError';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
   // Clave para almacenar el token 
  private tokenKey = 'token';
  //servicio que hemos implementado como buena practica
  // para separar la logica de gestion del token de la logica de logear al usuario
  constructor(private http: HttpClient, private router: Router, private clienteApi: ClienteApiRestService, @Inject(PLATFORM_ID) private platformId: Object) { }

  login(user: User): Observable<void> {
    return this.clienteApi.loginUser(user).pipe(
      map((resp) => {
        if (resp.body && !this.isError(resp.body)) {
          // Si el body empieza con 'Bearer', es un token
          if (isPlatformBrowser(this.platformId)) {
            sessionStorage.setItem(this.tokenKey, resp.body);
          }
          this.router.navigate(['/users/']);
        } else {
          // Si no es un token, retornamos el mensaje de error
          throw new Error(resp.body);
        }
      }),
      catchError((error) => {
        console.error('Error al iniciar sesión', error.message);
        return throwError(() => new Error(error.message));
      })
    );
  }
  
  isError(input: string): boolean {
    return input === 'Contraseña incorrecta' || input === 'Usuario no encontrado';
  }

  register(user:User){
    this.clienteApi.addUser(user).subscribe(
      resp => {
        if (isPlatformBrowser(this.platformId)) {
          sessionStorage.setItem(this.tokenKey, resp.body);
        }
        this.router.navigate(['/users/']);
      },

      err => {
        console.error("Error al crear usuario: " + err.message);
      }
    );
}

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return sessionStorage.getItem(this.tokenKey);
    } return null;
  }

  isAuthenticated(): boolean {
    return !!this.getToken(); // Devuelve true si hay un token
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      sessionStorage.removeItem(this.tokenKey);
    }
    this.router.navigate(['/login']); // Redirige al login
  }
}