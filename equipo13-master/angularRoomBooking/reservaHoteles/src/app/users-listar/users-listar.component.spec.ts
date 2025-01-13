import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersListarComponent } from './users-listar.component';

describe('UsersListarComponent', () => {
  let component: UsersListarComponent;
  let fixture: ComponentFixture<UsersListarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UsersListarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsersListarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
