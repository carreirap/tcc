import { Component, OnInit, ViewChild } from '@angular/core';
import { ToasterService} from 'angular5-toaster';
import { DataService, AuthenticationService } from '../_services';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Ordem } from '../_models/ordem';
import { DatePipe } from '@angular/common';
import { NgModel } from '@angular/forms';
import { ModalMaoobraComponent } from '../modal-maoobra/modal-maoobra.component';
import { ModalService } from '../modal-maoobra/modal-service';
import { PecaServicoOrdemService } from './ordem-servico-service';
import { PecaServicoOrdem } from '../_models/peca-servico-ordem';
import { ModalClienteService } from '../modal-pesquisa-cliente/modal-cliente-service';
import { UserService } from '../_services/user.service';
import { Lancamento } from '../_models/lancamentosTecnicos';
import { UsuarioLogado } from '../_models/usuario-logado';
// import { lancamento } from '../_models/lancamentosTecnicos';

@Component({
  selector: 'app-ordem-servico',
  templateUrl: './ordem-servico.component.html',
  styleUrls: ['./ordem-servico.component.css']
})

export class OrdemServicoComponent implements OnInit {
  toasterService: ToasterService;
  formOrdem = new Ordem();
  modalReference: any;
  item: PecaServicoOrdem;
  
  situacaoTecnica: Array<any> = [
    { value: 'Aberto', label: 'Aberto' },
    { value: 'Fechado', label: 'Fechado' },
    { value: 'Trabalhando', label: 'Trabalhando' },
    { value: 'Aguardando', label: 'Aguardando' },
    { value: 'Finalizado', label: 'Finalizado' },
    { value: 'Faturado', label: 'Faturado' }
  ];

  page = 0;
  content: Array<any>;
  pages = 0;
  nomePesquisa: String;
  cnpjPesquisa: String;
  typePesquisa = '';

  @ViewChild('cpfcnpjvalidation') pwConfirmModel: NgModel;
  constructor(private service: DataService, toasterService: ToasterService, public modal: NgbModal,
              private datePipe: DatePipe, private userService: UserService,
              private modalService: ModalService, private pecaServicoOrdemService: PecaServicoOrdemService,
              private modalClienteService: ModalClienteService,
              private authenticationService: AuthenticationService) {
    this.toasterService = toasterService;
  }

  ngOnInit() {
    this.formOrdem.lancamento = new Lancamento();
    this.getNomeUsario();
    this.modalService.carregarLinha.subscribe(
      result => this.addLinha(result)
    );

    this.modalClienteService.carregarCliente.subscribe(
      result => this.loadForm(result)
    );
    this.formOrdem.lancamento.situacao = 'Aberto';
    // this.formOrdem.dataAbertura = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
    this.formOrdem.lancamento.data = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
    
    // console.log(this.formOrdem.itemTables.length);
  }

  private getNomeUsario() {
      let userLog = new UsuarioLogado();
      userLog.usuario = JSON.parse(localStorage.getItem('currentUser')).usuario;
      this.authenticationService.getUpdatedUser(userLog).subscribe(response => {
          this.formOrdem.lancamento.nomeUsuario = response.nome;
          this.formOrdem.lancamento.idUsuario = response.id;
      }, (error) => {
        console.log('error in', error);
      });
  }


  onSubmit() {
    if (this.formOrdem.lancamento.situacao === 'Aberto') {
      this.formOrdem.lancamento.sequencia = 0;
    }
    this.service.post('/ordem', this.formOrdem).subscribe(response => {
      console.log(response);
      this.setNumeroOrdem(response);
      this.formOrdem.lancamentoLst.push(this.formOrdem.lancamento);
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

  mostrarModalMaoObra(modalPecas) {
    this.modalReference = this.modal.open(modalPecas);
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

  loadForm(line) {
    console.log(line);
    this.formOrdem.cliente.id = line.id;
    this.formOrdem.cliente.nome = line.nome;
    this.formOrdem.cliente.celular = line.celular;
    this.formOrdem.cliente.fone = line.fone;
    this.formOrdem.cliente.cnpj = line.cnpj;
  }

  addLinha(event) {
    console.log(event.descricao);
    event.sequencia = this.formOrdem.itemTables.length + 1;
    event.idOrdem = this.formOrdem.numeroOrdem;

    this.service.post('/ordem/pecaServico', event).subscribe(response => {
      console.log(response);
      this.pecaServicoOrdemService.emitirResultado.emit('gravou');
      this.formOrdem.itemTables.push(event);
      // this.toasterService.pop('success', 'Ordem de Serviço', 'Ordem de serviço cadastrado com sucesso!');
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.pecaServicoOrdemService.emitirResultado.emit('falhou');
      // this.toasterService.pop('error', 'Ordem de Serviço', error.error.mensagem);
    });
  }

  /* onSubmitLancamento() {
    this.formLancamento.idOrdem = this.formOrdem.numeroOrdem;
    this.service.post('/ordem/lancamento', this.formLancamento).subscribe(response => {
      console.log(response);
      this.toasterService.pop('success', 'Ordem de Serviço', 'Lançamento!');
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.toasterService.pop('error', 'Ordem de Serviço', error.error.mensagem);
    });
  } */

}
