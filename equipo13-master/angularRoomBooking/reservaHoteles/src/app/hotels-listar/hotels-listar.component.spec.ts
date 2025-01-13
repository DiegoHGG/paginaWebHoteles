import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HotelsListarComponent } from './hotels-listar.component';

describe('HotelsListarComponent', () => {
  let component: HotelsListarComponent;
  let fixture: ComponentFixture<HotelsListarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HotelsListarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HotelsListarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
