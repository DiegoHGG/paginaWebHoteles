import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingsListarComponent } from './bookings-listar.component';

describe('BookingsListarComponent', () => {
  let component: BookingsListarComponent;
  let fixture: ComponentFixture<BookingsListarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BookingsListarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookingsListarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
