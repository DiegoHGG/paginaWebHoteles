export enum UserStatus {
    NOBOOKINGS = "NOBOOKINGS",
    WITHINACTIVEBOOKINGS = "WITHINACTIVEBOOKINGS",
    WITHACTIVEBOOKINGS = "WITHACTIVEBOOKINGS"
}

export interface Booking {
    id: number;
    startDate: String;
    endDate: String;
    user?: String;
    nameUser?: String;
    room?: String;
    numberRoom?:String;
    nameHotel?:String;
}

export interface User {
    id: number;
    name: string;
    email: string;
    status: UserStatus;
    bookingList?: Booking[];
    password:string;
}

export interface Hotel{
    id:number;
    name:string;
    address: Address;
    roomList:Room[];    
}

export interface Room{
    id:number;
    roomNumber:string;
    roomType:RoomType;
    available:boolean;
    hotel?:Hotel;
    msgerror?:string;
}

export enum RoomType{
    SINGLE="SINGLE",
    DOUBLE="DOUBLE",
    SUITE="SUITE"
}

export interface Address{
    streetKind: String;
    streetName: String;
    number: number;
    postCode: String;
    otherInfo:String;
}