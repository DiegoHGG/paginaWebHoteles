import { Component } from '@angular/core';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { User, UserStatus, Booking } from '../shared/app.model';
import { DataService } from '../shared/data.service';
import { HttpResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-users-listar',
  templateUrl: './users-listar.component.html',
  styleUrl: './users-listar.component.css'

})
export class UsersListarComponent {

  filter: string = 'all';
  confirmPassword: string = '';
  Users!: User[];
  user: User = {
    id: 0, name: '', email: '', status: UserStatus.NOBOOKINGS, password: '',
    bookingList: []
  }; mostrarMensaje!: boolean;
  mensaje!: string;
  // Inyectamos los servicios
  constructor(private clienteApiRest: ClienteApiRestService, private datos: DataService) { }
  //método ejecutado tras la construcción del componente. Es el lugar adecuado para cargardatos




  ngOnInit() {
    this.getUsers_AccesoResponse();
  }

  getUsers_AccesoResponse() {
    this.clienteApiRest.getAllUsers_ConResponse().subscribe(
      resp => {

        if (resp.status < 400) { 
          this.Users = resp.body!;
        } else {
          this.mensaje = 'Error al acceder a los datos';
          this.mostrarMensaje = false;
        }
      },
      err => {
        console.log("Error al traer la lista: " + err.message);
        throw err;
      }
    )
  }

  borrar(id: Number) {
    this.clienteApiRest.deleteUsers(String(id)).subscribe(
      resp => {
        if (resp.status < 400) {
          // actualizamos variable compartida
          this.mostrarMensaje = false;
          // actualizamos variable compartida
          this.mensaje = resp.body; // mostramos el mensaje retornado por el API
          //Actualizamos la lista de users en la vista
          this.getUsers_AccesoResponse();
        } else {
          this.mostrarMensaje = false;
          this.mensaje = "Error al eliminar registro";
        }
      },
      err => {
        console.log("Error al borrar: " + err.message);
        throw err;
      }
    )
  }
  abrirModalEditar(user: User) {
    this.user = { ...user }; // Copiamos los datos del usuario seleccionado
  }

  samePassword(): boolean {
    if(this.user.password === this.confirmPassword){
      this.mostrarMensaje=false
      this.mensaje=""
      return true
    }else{
      this.mostrarMensaje=true
      this.mensaje="Las contraseñas no coinciden"
      return false
    }
  }

  guardarCambios() {
      this.hashPassword(this.user.password).then(hashedPassword => {
        this.user.password = hashedPassword;
        this.clienteApiRest.putUserbyId(String(this.user.id), this.user).subscribe(
          resp => {
            if (resp.status < 400) {
              this.mensaje = 'Usuario actualizado correctamente';
              this.mostrarMensaje = false;
              this.getUsers_AccesoResponse(); // Actualizamos la lista
              this.confirmPassword=""
            } else {
              this.mensaje = 'Error al actualizar el usuario';
              this.mostrarMensaje = true;
            }
          },
          err => {
            console.log("Error al actualizar: " + err.message);
          }
        );
      });
  }

  async hashPassword(password: string): Promise<string> {
    const encoder = new TextEncoder();
    const data = encoder.encode(password);
    const hashBuffer = await crypto.subtle.digest('SHA-256', data);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    return hashArray.map(byte => byte.toString(16).padStart(2, '0')).join('');
  }

}
