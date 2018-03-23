import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Headermenu.ComponentComponent } from './headermenu.component.component';

describe('Headermenu.ComponentComponent', () => {
  let component: Headermenu.ComponentComponent;
  let fixture: ComponentFixture<Headermenu.ComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Headermenu.ComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Headermenu.ComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
