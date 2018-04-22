import { Component, OnInit } from '@angular/core';
import { PerfiService } from './perfil-service';

@Component({
  selector: 'app-perfil-table-component',
  templateUrl: './perfil-table.component.html',
  styleUrls: ['./perfil-table.component.css']
})
export class PerfilTableComponent implements OnInit {
  user: any[];
  constructor(private perfil: PerfiService) { }

  ngOnInit() {
    this.perfil.get().subscribe(response => {
        this.user = response;
    });
  }

}
