import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { User, Booking, UserStatus, Hotel, Room} from './app.model';
import { Observable } from 'rxjs';
import e from 'express';
@Injectable({ providedIn: 'root' })
export class ClienteApiRestService {
  private static readonly BASE_URI_auth = 'http://localhost:8000/api/auth/';
  private static readonly BASE_URI_users = 'http://localhost:8000/api/users/' ;
  private static readonly BASE_URI_bookings = 'http://localhost:8000/api/bookings/' ;
  private static readonly BASE_URI_hotels = 'http://localhost:8000/api/hotels/' ;
  private static readonly USERS = 'users/';
  private static readonly HOTELS = 'hotels/';
  private static readonly ROOMS = 'rooms/';
  private static readonly BOOKINGS = 'bookings/';
  private static readonly LOGIN = 'login/';
  private static readonly REGISTER = 'register/';


  constructor(private http: HttpClient) { } // inyectamos el servicio HttpClient


  // Usuarios
  getAllUsers_ConResponse(): Observable<HttpResponse<User[]>> {
    let url = ClienteApiRestService.BASE_URI_users + ClienteApiRestService.USERS;
    return this.http.get<User[]>(url, { observe: 'response' });
  }
  getUserbyId(id: String): Observable<HttpResponse<User>> {
    let url = ClienteApiRestService.BASE_URI_users + ClienteApiRestService.USERS + id;
    return this.http.get<User>(url, { observe: 'response' });
  }

  deleteUsers(id: String): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI_users + ClienteApiRestService.USERS + id;
    return this.http.delete(url, { observe: 'response', responseType: 'text' });
  }
  
  putUserbyId(id: String, user: User): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI_users + ClienteApiRestService.USERS + id;
    return this.http.put(url, user, { observe: 'response', responseType: 'text' });
  }

  putUserStatus(id: String, user: User): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI_users + ClienteApiRestService.USERS + id;
    return this.http.patch(url, user, { observe: 'response', responseType: 'text' });
  }

  // Hoteles y habitaciones
  getAllHotels_ConResponse(): Observable<HttpResponse<Hotel[]>> {
    let url = ClienteApiRestService.BASE_URI_hotels + ClienteApiRestService.HOTELS;
    return this.http.get<Hotel[]>(url, { observe: 'response' });
  }

  getAllRooms_ConResponse(id: String, params: String): Observable<HttpResponse<Room[]>> {
    let url = ClienteApiRestService.BASE_URI_hotels + ClienteApiRestService.HOTELS + id + "/" + ClienteApiRestService.ROOMS + params;
    return this.http.get<Room[]>(url, { observe: 'response' });
  }

  addHotel_ConResponse(hotel: Hotel): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI_hotels + ClienteApiRestService.HOTELS;
    return this.http.post(url, hotel, { observe: 'response', responseType: 'text' });
  }
  getHotelbyId(id: String): Observable<HttpResponse<Hotel>> {
    let url = ClienteApiRestService.BASE_URI_hotels + ClienteApiRestService.HOTELS + id;
    return this.http.get<Hotel>(url, { observe: 'response' });
  }
  deleteHotel(id: String): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI_hotels + ClienteApiRestService.HOTELS + id;
    return this.http.delete(url, { observe: 'response', responseType: 'text' });
  }
  updateRoomAvailability(idHotel: number, available: boolean, idRoom: number): Observable<HttpResponse<any>> {
    const url = `${ClienteApiRestService.BASE_URI_hotels}${ClienteApiRestService.HOTELS}${idHotel}/${ClienteApiRestService.ROOMS}${idRoom}`;
    return this.http.patch(url, available, { observe: 'response', responseType: 'text' });
  }

  //Bookings

  addBooking(booking: Booking): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI_bookings + ClienteApiRestService.BOOKINGS;
    return this.http.post(url, booking, { observe: 'response', responseType: 'text' });
  }

  getAllBookings_ConResponse(): Observable<HttpResponse<Booking[]>> {
    let url = ClienteApiRestService.BASE_URI_bookings + ClienteApiRestService.BOOKINGS;
    return this.http.get<Booking[]>(url, { observe: 'response' });
  }

  //Login
  loginUser(user: User): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI_auth + ClienteApiRestService.LOGIN;
    return this.http.post(url, user, { observe: 'response', responseType: 'text' });
  }
  addUser(user: User): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI_auth + ClienteApiRestService.REGISTER;
    return this.http.post(url, user, { observe: 'response', responseType: 'text' });
  }


}