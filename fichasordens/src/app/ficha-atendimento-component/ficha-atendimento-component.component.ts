import { Component, OnInit, ViewChild } from '@angular/core';
import { ToasterService} from 'angular5-toaster';
import { DataService, AuthenticationService } from '../_services';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Ficha } from '../_models/ficha';
import { DatePipe } from '@angular/common';
import { UsuarioLogado } from '../_models/usuario-logado';
import { SituacaoTecnica } from '../_models/situacao-tecnica';
import { ModalClienteService } from '../modal-pesquisa-cliente/modal-cliente-service';
import { ModalService } from '../modal-maoobra/modal-service';
import { PecaServicoOrdemService } from '../ordem-servico/ordem-servico-service';
import { ModalAtendimentoService } from '../modal-atendimento/modal-atendimento-service';


@Component({
  selector: 'app-ficha-atendimento-component',
  templateUrl: './ficha-atendimento-component.component.html',
  styleUrls: ['./ficha-atendimento-component.component.css']
})
export class FichaAtendimentoComponentComponent implements OnInit {
  formFicha: Ficha;
  situacaoTecnica: any;
  toasterService: ToasterService;


  constructor(private service: DataService, toasterService: ToasterService, public modal: NgbModal,
          private datePipe: DatePipe, private authenticationService: AuthenticationService,
          private modalClienteService: ModalClienteService,
          private modalService: ModalService, private pecaServicoOrdemService: PecaServicoOrdemService,
        private modalAtendimento: ModalAtendimentoService) { 
    this.formFicha = new Ficha();
    this.situacaoTecnica = new SituacaoTecnica();
    this.toasterService = toasterService;
  }

  ngOnInit() {
    this.modalService.carregarLinha.subscribe(
      result => this.addLinha(result)
    );
    this.modalClienteService.carregarCliente.subscribe(
      result => this.loadForm(result)
    );
    this.modalAtendimento.carregarLinha.subscribe(
      result => this.addLinhaAtendimento(result)  
    );
    this.formFicha.lancamento.data = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
    this.formFicha.lancamento.situacao = 'Aberto';
    this.formFicha.tipoServico = 'Assitencia';
    this.getNomeUsario();
  }

  onSubmit() {
    if (this.formFicha.lancamento.situacao === 'Aberto') {
      this.formFicha.lancamento.sequencia = 0;
      this.formFicha.lancamento.observacao = 'Abertura'
    }
    this.service.post('/ficha', this.formFicha).subscribe(response => {
      console.log(response);
      this.setNumeroFicha(response);
      this.formFicha.lancamentoLst.push(this.formFicha.lancamento);
      this.toasterService.pop('success', 'Ordem de Serviço', 'Ordem de serviço cadastrado com sucesso!');
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.toasterService.pop('error', 'Ordem de Serviço', error.error.mensagem);
    });
  }

  setNumeroFicha(data) {
    this.formFicha.numeroFicha = data.numeroFicha;
  }
  

  mostrarModal(clienteModal) {
    this.modal.open(clienteModal);
    return false;
  }

  mostrarModalMaoObra(modalPecas) {
    this.modal.open(modalPecas);
    return false;
  }

  mostrarModalAtendimento(atendimento) {
    this.modal.open(atendimento);
    return false;
  }

  private getNomeUsario() {
    let userLog = new UsuarioLogado();
    userLog.usuario = JSON.parse(localStorage.getItem('currentUser')).usuario;
    this.authenticationService.getUpdatedUser(userLog).subscribe(response => {
        this.formFicha.lancamento.usuario = response.nome;
        this.formFicha.lancamento.idUsuario = response.id;
    }, (error) => {
      console.log('error in', error);
    });
  }

  addLinha(event) {
    console.log(event.descricao);
    event.sequencia = this.formFicha.itemTables.length + 1;
    event.idOrdem = this.formFicha.numeroFicha;

    this.service.post('/ficha/pecaServico', event).subscribe(response => {
      console.log(response);
      this.pecaServicoOrdemService.emitirResultado.emit('gravou');
      this.formFicha.itemTables.push(event);
      // this.toasterService.pop('success', 'Ordem de Serviço', 'Ordem de serviço cadastrado com sucesso!');
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.pecaServicoOrdemService.emitirResultado.emit('falhou');
      // this.toasterService.pop('error', 'Ordem de Serviço', error.error.mensagem);
    });
  }

  addLinhaAtendimento(event) {
    console.log(event);
    event.sequencia = this.formFicha.atendimento.length + 1;
    event.idOrdem =  this.formFicha.numeroFicha;
    this.formFicha.atendimento.push(event);
  }

  loadForm(data) {
    this.formFicha.cliente.cnpj = data.cnpj;
    this.formFicha.cliente.nome = data.nome;
    this.formFicha.cliente.celular = data.celular;
    this.formFicha.cliente.fone = data.fone;
    this.formFicha.cliente.id = data.id;
  }

}
