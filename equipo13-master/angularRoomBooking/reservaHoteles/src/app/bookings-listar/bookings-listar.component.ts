import { Component } from '@angular/core';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { User, UserStatus, Booking, Hotel, Room, RoomType } from '../shared/app.model';
import { DataService } from '../shared/data.service';
import { HttpResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-bookings-listar',
  templateUrl: './bookings-listar.component.html',
  styleUrl: './bookings-listar.component.css'
})
export class BookingsListarComponent {

  Users!: User[];
  Bookings!: Booking[];
  Hotels!: Hotel[];
  Rooms!: Room[];
  mostrarMensaje!: boolean;
  mensaje!: string;
  // Inyectamos los servicios
  constructor(private clienteApiRest: ClienteApiRestService, private datos: DataService) { }
  //método ejecutado tras la construcción del componente. Es el lugar adecuado para cargardatos
  userSeleccionado: any = null;
  hotelSeleccionado: any = null;
  roomSeleccionada: any = null;
  booking: Booking = {
    id: 0, startDate: "", endDate: ""
  };

  selectUser(user: any) {
    this.userSeleccionado = user;
    this.userSeleccionado.bookingList = []
    this.booking.user = user.id
    this.booking.nameUser = user.name
  }
  selectHotel(hotel: any) {
    this.hotelSeleccionado = hotel;
    this.booking.nameHotel = hotel.name;
    if (this.booking.startDate !== "" || this.booking.endDate !== "") this.getRooms_AccesoResponse();
  }
  selectRoom(room: any) {
    this.roomSeleccionada = room;
    this.roomSeleccionada.hotel = null;
    this.booking.numberRoom = room.roomNumber
    this.booking.room = room.id
  }

  ngOnInit() {
    this.getBookings_AccesoResponse();
    this.getUsers_AccesoResponse();
    this.getHotels_AccesoResponse();
  }

  getBookings_AccesoResponse() {
    this.clienteApiRest.getAllBookings_ConResponse().subscribe(
      resp => {
        if (resp.status < 400) { 
          this.Bookings = resp.body!;
        } else {
          this.mensaje = 'Error al acceder a los datos';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al traer la lista: " + err.message);
        throw err;
      }
    )
  }

  getUsers_AccesoResponse() {
    this.clienteApiRest.getAllUsers_ConResponse().subscribe(
      resp => {
        if (resp.status < 400) {
          this.Users = resp.body!;
        } else {
          this.mensaje = 'Error al acceder a los datos';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al traer la lista: " + err.message);
        throw err;
      }
    )
  }

  getHotels_AccesoResponse() {
    this.clienteApiRest.getAllHotels_ConResponse().subscribe(
      resp => {
        if (resp.status < 400) { 
          this.Hotels = resp.body!;
        } else {
          this.mensaje = 'Error al acceder a los datos';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al traer la lista: " + err.message);
        throw err;
      }
    )
  }

  getRooms_AccesoResponse() {
    let params: String = `?startDate=${this.booking.startDate}&endDate=${this.booking.endDate}`;

    this.clienteApiRest.getAllRooms_ConResponse(this.hotelSeleccionado.id, params).subscribe(
      resp => {
        if (resp.status < 400) { 
          this.Rooms = resp.body!; 
        } else {
          this.mensaje = 'Error al acceder a los datos';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al traer la lista: " + err.message);
        throw err;
      }
    )
  }

  addBooking_AccesoResponse() {
    this.clienteApiRest.addBooking(this.booking).subscribe(
      resp => {
        if (resp.status < 400) { 
          this.actualizarStatus()
          this.booking = {
            id: 0, startDate: "", endDate: ""
          };
          this.userSeleccionado = null;
          this.hotelSeleccionado = null;
          this.roomSeleccionada = null;
          this.mensaje = 'Reserva añadida exitosamente!';
          this.Rooms = [];
          this.mostrarMensaje = true;
          this.getBookings_AccesoResponse();
        } else {
          this.mensaje = 'Error al acceder a los datos';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al traer la lista: " + err.message);
        throw err;
      }
    )
  }

  putStatus_AccesoResponse() {
    this.clienteApiRest.putUserStatus(this.userSeleccionado.id, this.userSeleccionado).subscribe(
      resp => {
        if (resp.status < 400) {
          this.booking = {
            id: 0, startDate: "", endDate: ""
          };

        } else {
          this.mensaje = 'Error al acceder a los datos';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al traer la lista: " + err.message);
        throw err;
      }
    )
  }

  getCurrentDate(): string {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
    const day = currentDate.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  onSubmit(form: any) {
    this.addBooking_AccesoResponse();
  }

  actualizarStatus() {
    this.userSeleccionado.status = "WITHACTIVEBOOKINGS";
    this.putStatus_AccesoResponse()
    this.limpiarformulario()
  }

  limpiarformulario() {
    this.userSeleccionado = null;
    this.hotelSeleccionado = null;
    this.roomSeleccionada = null;
    this.mensaje = 'Reserva añadida exitosamente!';
    this.Rooms = [];
    this.mostrarMensaje = true;
  }
}

