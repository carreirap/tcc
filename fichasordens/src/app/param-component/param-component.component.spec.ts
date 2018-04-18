import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ParamComponentComponent } from './param-component.component';

describe('ParamComponentComponent', () => {
  let component: ParamComponentComponent;
  let fixture: ComponentFixture<ParamComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ParamComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParamComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
