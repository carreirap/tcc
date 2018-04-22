import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dadosempresa-component',
  templateUrl: './dadosempresa-component.component.html',
  styleUrls: ['./dadosempresa-component.component.css']
})
export class DadosempresaComponentComponent implements OnInit {

  constructor() { }

  optionsSelect: Array<any>;

    ngOnInit() {
        this.optionsSelect = [
            { value: '1', label: 'Paraná' },
            { value: '2', label: 'São Paulo' },
            { value: '3', label: 'Rio de Janeiro' },
            { value: '3', label: 'Pernanbuco' },
            { value: '3', label: 'Bahia' }
        ];
    }

}
