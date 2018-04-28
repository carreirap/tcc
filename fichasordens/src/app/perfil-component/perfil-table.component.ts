import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { PerfiService } from './perfil-service';


@Component({
  selector: 'app-perfil-table-component',
  templateUrl: './perfil-table.component.html',
  styleUrls: ['./perfil-table.component.css']
})
export class PerfilTableComponent implements OnInit {
  user: any[];

  @Output() valueChange = new EventEmitter();

  constructor(private perfil: PerfiService) { }

  ngOnInit() {
    this.perfil.get().subscribe(response => {
      this.user = response;
    });
  }

  getRow(user: any) {
    console.log(user);
    this.valueChanged(user);
  }

  valueChanged(user: any) { // You can give any function name
    this.valueChange.emit(user);
  }

}
