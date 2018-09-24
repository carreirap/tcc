import { Component, OnInit } from '@angular/core';
import { Parametro } from '../_models/parametro';
import { DataService } from '../_services';
import { ToasterService} from 'angular5-toaster';
import { FormGroup, AbstractControl } from '@angular/forms';
import { FormValue } from '../_models/paramForm';

@Component({
  selector: 'app-parametro',
  templateUrl: './parametro.component.html',
  styleUrls: ['./parametro.component.css']
})



export class ParametroComponent implements OnInit {
  formValue: FormValue = new FormValue();

  toasterService: ToasterService;

  constructor(private service: DataService, toasterService: ToasterService) {
    this.toasterService = toasterService;
  }

  ngOnInit() {

    this.service.get('/parametro').subscribe(response => {
      console.log(response);
      this.loadData(response);
    }, (error) => {
      console.log('error in', error);
      this.toasterService.pop('error', 'Empresa', error.mensagem);
    });
  }

  onSubmit() {
    const array = new Array();
    const par1: Parametro = new Parametro();
    par1.id = this.formValue.idVisita;
    par1.valor = this.formValue.valorVisita;
    array.push(par1);
    const par2: Parametro = new Parametro();
    par2.id = this.formValue.idHora;
    par2.valor = this.formValue.valorHora;
    array.push(par2);
    const par3: Parametro = new Parametro();
    par2.id = this.formValue.idDiasAlerta;
    par2.valor = this.formValue.diasAlerta;

    this.service.post('/parametro', array).subscribe(response => {
        console.log(response);
        this.toasterService.pop('success', 'Parametros', 'Parâmetros alterados com sucesso!');
      }, (error) => {
        console.log('error in', error);
        this.toasterService.pop('error', 'Parametros', JSON.parse(error._body).mensagem);
    });

  }

  loadData(data: any) {
    this.formValue.idVisita = data[0].id;
    this.formValue.valorVisita = data[0].valor;
    this.formValue.idHora = data[1].id;
    this.formValue.valorHora = data[1].valor;
    this.formValue.idDiasAlerta = data[2].id;
    this.formValue.diasAlerta = data[2].valor;
  }

}
