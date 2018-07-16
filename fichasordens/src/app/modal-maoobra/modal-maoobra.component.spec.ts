import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalMaoobraComponent } from './modal-maoobra.component';

describe('ModalMaoobraComponent', () => {
  let component: ModalMaoobraComponent;
  let fixture: ComponentFixture<ModalMaoobraComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalMaoobraComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalMaoobraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
