import { Component, OnInit, ViewChild } from '@angular/core';
import { DataService, CustomInterceptor } from '../_services/http.service';
import { ToasterService} from 'angular5-toaster';
import { Cliente } from '../_models/cliente';
import { Estados } from '../_models/TodosEstados';
import { NgModel } from '@angular/forms';

@Component({
  selector: 'app-cliente-component',
  templateUrl: './cliente-component.component.html',
  styleUrls: ['./cliente-component.component.css']
})
export class ClienteComponentComponent implements OnInit {
  toasterService: ToasterService;
  formCliente = new Cliente();
  optionsSelect: Array<any>;
  estados: Estados = new Estados();


  @ViewChild('cpfcnpjvalidation') pwConfirmModel: NgModel;
  constructor(private service: DataService, toasterService: ToasterService) {
    this.toasterService = toasterService;
  }

  ngOnInit() {
    this.optionsSelect = this.estados.getTodosEstados();
  }

  onSubmit() {
    console.log(this.formCliente);
    this.service.post('/cliente', this.formCliente).subscribe(response => {
        console.log(response);
        this.toasterService.pop('success', 'Cliente', 'Cliente cadastrado com sucesso!');
      }, (error) => {
        console.log('error in', error.error.mensagem);
        this.toasterService.pop('error', 'Cliente', error.error.mensagem);
      });
  }

  setValue(value: string) {
    this.formCliente.cnpj = value;
  }

  setCnpjCpfInvalido(mensagem: String) {
    console.log(mensagem);
    if (mensagem !== 'OK') {
      this.pwConfirmModel.control.setErrors({'cnpj-cpf': true});
    }
  }
}
