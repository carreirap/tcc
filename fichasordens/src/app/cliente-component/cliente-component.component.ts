import { Component, OnInit, ViewChild, ViewChildren } from '@angular/core';
import { DataService, CustomInterceptor } from '../_services/http.service';
import { ToasterService} from 'angular5-toaster';
import { Cliente } from '../_models/cliente';
import { Estados } from '../_models/TodosEstados';
import { NgModel } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PaginatorModule } from 'primeng/paginator';
import { TableModule } from 'primeng/table';
import { ModalClienteService } from '../modal-pesquisa-cliente/modal-cliente-service';



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

  page = 0;
  content: Array<any>;
  pages = 0;
  nomePesquisa: String;
  cnpjPesquisa: String;
  typePesquisa = '';

  @ViewChild('cpfcnpjvalidation') pwConfirmModel: NgModel;
  constructor(private service: DataService, toasterService: ToasterService, public modal: NgbModal,
              private modalClienteService: ModalClienteService) {
    this.toasterService = toasterService;
  }

  ngOnInit() {
    this.optionsSelect = this.estados.getTodosEstados();

    this.modalClienteService.carregarCliente.subscribe(
      result => this.loadForm(result)
    );
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

  mostrarModal(clienteModal) {
    this.modal.open(clienteModal);
    return false;
  }

  loadForm(line) {
    console.log(line);
    this.formCliente.id = line.id;
    this.formCliente.nome = line.nome;
    this.formCliente.celular = line.celular;
    this.formCliente.cidade = line.cidade;
    this.formCliente.bairro = line.bairro;
    this.formCliente.cnpj = line.cnpj;
    this.formCliente.complemento = line.complemento;
    this.formCliente.email = line.email;
    this.formCliente.estado = line.estado;
    this.formCliente.fone = line.fone;
    this.formCliente.idEndereco = line.idEndereco;
    this.formCliente.logradouro = line.logradouro;
    this.formCliente.numero = line.numero;
    this.formCliente.cep = line.cep;
  }

}
