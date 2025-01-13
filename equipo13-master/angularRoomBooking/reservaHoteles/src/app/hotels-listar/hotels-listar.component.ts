import { Component } from '@angular/core';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { Hotel, UserStatus, Booking, Room, RoomType } from '../shared/app.model';
import { DataService } from '../shared/data.service';
import { HttpResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-hotels-listar',
  templateUrl: './hotels-listar.component.html',
  styleUrl: './hotels-listar.component.css'
})

export class HotelsListarComponent {
  Hotels!: Hotel[];
  hotel =
    {
      id: 0,
      name: '',
      address: {
        streetKind: '',
        streetName: '',
        number: 0,
        postCode: '',
        otherInfo: ''
      },
      roomList: [],
    } as Hotel;
  mostrarMensaje!: boolean;
  mensaje!: string;
  Rooms!: Room[];

  selectedHotel: any;
  idGlobal: number = -1;
  newRoom = { id: -1, roomNumber: '', roomType: RoomType.SINGLE, available: false } as Room;
  constructor(private clienteApiRest: ClienteApiRestService, private datos: DataService) { }



  ngOnInit() {
    this.getHotels_AccesoResponse();
  }

  seleccionarHotel(hotel: Hotel) {
    this.selectedHotel = hotel;
    this.getRooms_AccesoResponse();
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

  ngAddHotel() {
    this.clienteApiRest.addHotel_ConResponse(this.hotel).subscribe(
      resp => {
        if (resp.status < 400) { 
          this.mensaje = 'Usuario creado exitosamente';
          this.mostrarMensaje = true;
          this.hotel =
            {
              id: 0,
              name: '',
              address: {
                streetKind: '',
                streetName: '',
                number: 0,
                postCode: '',
                otherInfo: ''
              },
              roomList: [],
            } as Hotel; // Reiniciamos el objeto `hotel`
          this.getHotels_AccesoResponse(); // Actualizamos la lista de hotels
        } else {
          this.mensaje = 'Error al crear hotel';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.error("Error al crear hotel: " + err.message);
        this.mensaje = 'Error al crear hotel';
        this.mostrarMensaje = true;
      }
    );
  }

  borrar(id: Number) {
    this.clienteApiRest.deleteHotel(String(id)).subscribe(
      resp => {
        if (resp.status < 400) {
          this.mostrarMensaje = true;
          this.mensaje = resp.body;
          this.getHotels_AccesoResponse();
        } else {
          this.mostrarMensaje = true;
          this.mensaje = "Error al eliminar registro";
        }
      },
      err => {
        console.log("Error al borrar: " + err.message);
        throw err;
      }
    )
  }
  getRooms_AccesoResponse() {

    this.clienteApiRest.getHotelbyId(this.selectedHotel.id).subscribe(
      resp => {

        if (resp.status < 400) { 
          this.selectedHotel = resp.body!;
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

  async onCheckboxChange(event: any, room: Room) {
    const originalAvailable = room.available;

    room.available = event.target.checked;

    try {
      // Llamamos a la función que gestiona el cambio de disponibilidad
      const success = await this.cambiarDisponibilidad(room);

      if (!success) {
        // Si la llamada falla, revertimos el estado
        room.available = originalAvailable;
        event.target.checked = originalAvailable;
        this.mostrarMensaje = true;
        room.msgerror = 'Habitación reservada actualmente';
      }
    } catch (error) {
      // Si ocurre un error inesperado, revertimos también el estado
      room.available = originalAvailable;
      event.target.checked = originalAvailable;
      this.mostrarMensaje = true;
      room.msgerror = 'Habitación reservada actualmente';
    }
  }

  cambiarDisponibilidad(room: Room): Promise<boolean> {
    return new Promise((resolve, reject) => {
      const newAvailability = room.available; 

      this.clienteApiRest.updateRoomAvailability(this.selectedHotel.id, newAvailability, room.id).subscribe(
        resp => {
          if (resp.status < 400) {
            resolve(true); 
            this.getRooms_AccesoResponse(); 
          } else {
            resolve(false);
          }
        },
        err => {
          reject(false); 
        }
      );
    });
  }


  // Añadir nueva habitación al roomList del hotel
  addRoom() {
    this.hotel.roomList.push({ ...this.newRoom }); // Añadir una copia de newRoom
    this.idGlobal--;//usamos un id negativo para que no coincida con habitaciones de verdad
    this.newRoom = { id: this.idGlobal, roomNumber: '', roomType: RoomType.SINGLE, available: false}; // Reiniciar el objeto newRoom
  }

}

