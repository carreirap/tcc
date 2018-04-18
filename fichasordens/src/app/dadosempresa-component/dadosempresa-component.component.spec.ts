import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DadosempresaComponentComponent } from './dadosempresa-component.component';

describe('DadosempresaComponentComponent', () => {
  let component: DadosempresaComponentComponent;
  let fixture: ComponentFixture<DadosempresaComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DadosempresaComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DadosempresaComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
