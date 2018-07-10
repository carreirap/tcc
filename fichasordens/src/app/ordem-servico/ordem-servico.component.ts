import { Component, OnInit, ViewChild } from '@angular/core';
import { ToasterService} from 'angular5-toaster';
import { DataService, AuthenticationService } from '../_services';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Ordem } from '../_models/ordem';
import { ItemTable } from '../_models/item-table';
import { DatePipe } from '@angular/common';
import { User } from '../_models';
import { NgModel } from '@angular/forms';

@Component({
  selector: 'app-ordem-servico',
  templateUrl: './ordem-servico.component.html',
  styleUrls: ['./ordem-servico.component.css']
})

export class OrdemServicoComponent implements OnInit {
  toasterService: ToasterService;
  formOrdem = new Ordem();
  item: ItemTable;
  situacaoSelect: Array<any> = [
    { value: 'Aberto', label: 'Aberto' },
    { value: 'Trabalhando', label: 'Trabalhando' },
    { value: 'Aguardando', label: 'Aguardando' }
  ];

  page = 0;
  content: Array<any>;
  pages = 0;
  nomePesquisa: String;
  cnpjPesquisa: String;
  typePesquisa = '';

  @ViewChild('cpfcnpjvalidation') pwConfirmModel: NgModel;
  constructor(private service: DataService, toasterService: ToasterService, public modal: NgbModal,
              private datePipe: DatePipe, private authenticationService: AuthenticationService) {
    this.toasterService = toasterService;
  }

  ngOnInit() {
    this.formOrdem.situacao = 'Aberto';
    this.formOrdem.dataAbertura = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
    this.getNomeUsario();
    this.item = new ItemTable();
    this.item.qtde = 1;
    this.item.descricao = 'Descricao';
    this.item.valor = 5680;
    this.item.garantia = 3;
    this.item.nf = 69;
    this.formOrdem.itemTables.push(this.item);
  }

  private getNomeUsario() {
    const user = new User();
    user.usuario = JSON.parse(localStorage.getItem('currentUser')).usuario;
    this.authenticationService.getUpdatedUser(user).subscribe(response => {
      this.formOrdem.responsavel = response.nome;
    }, (error) => {
      console.log('error in', error);
    });
  }

  onSubmit() {
    this.service.post('/ordem', this.formOrdem).subscribe(response => {
      console.log(response);
      this.setNumeroOrdem(response);
      this.toasterService.pop('success', 'Ordem de Serviço', 'Ordem de serviço cadastrado com sucesso!');
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.toasterService.pop('error', 'Ordem de Serviço', error.error.mensagem);
    });
  }

  private setNumeroOrdem(response) {
    this.formOrdem.numeroOrdem = response.numeroOrdem;
  }

  mostrarModal(clienteModal) {
    this.modal.open(clienteModal);
    return false;
  }

  setCnpjCpfInvalido(mensagem: String) {
    console.log(mensagem);
    if (mensagem !== 'OK') {
        this.pwConfirmModel.control.setErrors({ 'cnpj-cpf': true });
    }
  }

  setValue(value: string) {
    this.formOrdem.cliente.cnpj = value;
  }

  setPages(i, event: any) {
    event.preventDefault();
    this.page = i;
  }

  loadForm(line) {
    console.log(line);
    this.formOrdem.cliente.id = line.id;
    this.formOrdem.cliente.nome = line.nome;
    this.formOrdem.cliente.celular = line.celular;
    this.formOrdem.cliente.fone = line.fone;
    this.formOrdem.cliente.cnpj = line.cnpj;
  }

  loadClientes(response: any) {
    console.log(response);
    this.content = response.content;
    console.log(this.content);
    this.pages = response['totalPages'];
  }

  pesquisarPage(type) {
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
