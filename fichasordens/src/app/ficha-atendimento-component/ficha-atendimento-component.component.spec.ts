import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FichaAtendimentoComponentComponent } from './ficha-atendimento-component.component';

describe('FichaAtendimentoComponentComponent', () => {
  let component: FichaAtendimentoComponentComponent;
  let fixture: ComponentFixture<FichaAtendimentoComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FichaAtendimentoComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FichaAtendimentoComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
