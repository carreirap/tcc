import { Component, OnInit } from '@angular/core';
import { DataService, CustomInterceptor } from '../_services/http.service';
import { ToasterService} from 'angular5-toaster';
import { Cliente } from '../_models/cliente';

@Component({
  selector: 'app-cliente-component',
  templateUrl: './cliente-component.component.html',
  styleUrls: ['./cliente-component.component.css']
})
export class ClienteComponentComponent implements OnInit {
  toasterService: ToasterService;
  formCliente = new Cliente();
  optionsSelect: Array<any>;

  constructor(private service: DataService, toasterService: ToasterService) {
    this.toasterService = toasterService;
  }

  ngOnInit() {
    this.optionsSelect = [
      { value: 'PR', label: 'Paraná' },
      { value: 'SP', label: 'São Paulo' },
      { value: 'AC', label: 'Acre' },
      { value: 'AL', label: 'Alagoas' },
      { value: 'AP', label: 'Amapá' },
      { value: 'AM', label: 'Amazonas' },
      { value: 'BH', label: 'Bahia' },
      { value: 'CE', label: 'Ceará' },
      { value: 'DF', label: 'Distrito Federal' },
      { value: 'ES', label: 'Espirito Santo' },
      { value: 'GO', label: 'Goias' },
      { value: 'MA', label: 'Maranhão' },
      { value: 'MT', label: 'Mato Grosso' },
      { value: 'MS', label: 'Mato Grosso do Sul' },
      { value: 'MG', label: 'Minas Gerais' },
      { value: 'PA', label: 'Pará' },
      { value: 'PB', label: 'Paraíba' },
      { value: 'PE', label: 'Pernambuco' },
      { value: 'PI', label: 'Piauí' },
      { value: 'RR', label: 'Roraima' },
      { value: 'RO', label: 'Rondônia' },
      { value: 'RJ', label: 'Rio de Janeiro' },
      { value: 'RN', label: 'Rio Grande do Norte' },
      { value: 'RS', label: 'Rio Grande do Sul' },
      { value: 'SC', label: 'Santa Catarina' },
      { value: 'SE', label: 'Sergipe' },
      { value: 'TO', label: 'Tocantis' }
  ];
  }

  onSubmit() {
    console.log(this.formCliente);
    this.service.post('/cliente', this.formCliente).subscribe(response => {
        console.log(response);
        this.toasterService.pop('success', 'Cliente', 'Cliente cadastrado com sucesso!');
      }, (error) => {
        console.log('error in', error);
        this.toasterService.pop('error', 'Cliente', JSON.parse(error._body).mensagem);
      });
  }

}
