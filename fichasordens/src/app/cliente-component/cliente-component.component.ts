import { Component, OnInit, ViewChild, ViewChildren } from '@angular/core';
import { DataService, CustomInterceptor } from '../_services/http.service';
import { ToasterService} from 'angular5-toaster';
import { Cliente } from '../_models/cliente';
import { Estados } from '../_models/TodosEstados';
import { NgModel } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PaginatorModule } from 'primeng/paginator';
import { TableModule } from 'primeng/table';



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
  constructor(private service: DataService, toasterService: ToasterService, public modal: NgbModal) {
    this.toasterService = toasterService;
  }

  ngOnInit() {
    this.optionsSelect = this.estados.getTodosEstados();
    /* this.service.get('/cliente?cnpjcpf=' + '273.784.108-96').subscribe(response => {
      console.log(response);
    }, (error) => {
      console.log('error in', error.error.mensagem);
    }); */
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

  setPages(i, event: any) {
    event.preventDefault();
    this.page = i;
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

  loadClientes(response: any) {
    console.log(response);
    this.content = response.content;
    console.log(this.content);
    this.pages = response['totalPages'];
  }

  public pesquisarPage(type) {
    this.typePesquisa = type;
    if (this.typePesquisa === 'cnpjcpf') {
        this.service.get('/cliente?cnpjcpf=' + this.cnpjPesquisa + '&page=' +
        this.page + '&size=1&sort=nome,DESC').subscribe(response => {
            console.log(response);
            this.loadClientes(response);
        }, (error) => {
            console.log('error in', error.error.mensagem);
        });
    } else {
        if (this.nomePesquisa.length > 3 ) {
            this.service.get('/cliente?nome=' + this.nomePesquisa + '&page=' +
            this.page + '&size=1&sort=nome,DESC').subscribe(response => {
                console.log(response);
                this.loadClientes(response);
            }, (error) => {
                console.log('error in', error.error.mensagem);
            });
        }
    }
  }

  paginate(event) {
   console.log(event);
   this.page = event.page;
   this.pesquisarPage(this.typePesquisa);
  }
}
