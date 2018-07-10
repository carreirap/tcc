import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalPesquisaClienteComponent } from './modal-pesquisa-cliente.component';

describe('ModalPesquisaClienteComponent', () => {
  let component: ModalPesquisaClienteComponent;
  let fixture: ComponentFixture<ModalPesquisaClienteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalPesquisaClienteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalPesquisaClienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
