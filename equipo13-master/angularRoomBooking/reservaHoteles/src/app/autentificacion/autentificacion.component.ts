import { Component } from '@angular/core';
import { User, UserStatus } from '../shared/app.model';
import { AuthService } from '../shared/auth.service';


@Component({
  selector: 'app-autentificacion',
  templateUrl: './autentificacion.component.html',
  styleUrl: './autentificacion.component.css'
})

export class AutentificacionComponent {

  mostrarMensajeInicio!: boolean;
  mostrarMensajeRegistro!: boolean;
  mensaje!: string;
  userPassword!:string;
  constructor(private authService: AuthService) { }

  activeForm: string = 'login';
  user: User = {
    id: 0, name: '', email: '', status: UserStatus.NOBOOKINGS, password: '',
  };

  registerData = {
    email: '',
    password: '',
    name: '',
    confirmPassword: ''
  };

  // Alternar entre "Iniciar Sesión" y "Registrarse"
  change(form: string) {
    this.activeForm = form;
  }


  login() {
    this.hashPassword(this.userPassword).then(hashedPassword => {
      this.user.password = hashedPassword;
      this.authService.login(this.user).subscribe(
        () => {
          this.mostrarMensajeInicio = false;
        },
        (error) => {
          this.mostrarMensajeInicio = true;
          this.mensaje = error.message;
        }
      );
    });

  }

  logout() {
    this.authService.logout();
  }


  register() {
    if (this.registerData.password == this.registerData.confirmPassword) {
      this.user.password = this.registerData.password
      this.user.email = this.registerData.email
      this.user.name = this.registerData.name
      this.mostrarMensajeRegistro=false;
      this.hashPassword(this.user.password).then(hashedPassword => {
        this.user.password = hashedPassword;
        this.authService.register(this.user);
      });

    }else{
      this.mensaje="Las contraseñas no coinciden"
      this.mostrarMensajeRegistro=true;
    }
  }

  async hashPassword(password: string): Promise<string> {
    const encoder = new TextEncoder();
    const data = encoder.encode(password);
    const hashBuffer = await crypto.subtle.digest('SHA-256', data);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    return hashArray.map(byte => byte.toString(16).padStart(2, '0')).join('');
  }


}
