import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfilComponentComponent } from './perfil-component.component';

describe('PerfilComponentComponent', () => {
  let component: PerfilComponentComponent;
  let fixture: ComponentFixture<PerfilComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PerfilComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PerfilComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
