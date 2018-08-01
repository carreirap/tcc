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
import { ActivatedRoute } from '../../../node_modules/@angular/router';
import { Cliente } from '../_models/cliente';
import { FichaAtendimentoService } from './ficha-atendimento-service';


@Component({
  selector: 'app-ficha-atendimento-component',
  templateUrl: './ficha-atendimento-component.component.html',
  styleUrls: ['./ficha-atendimento-component.component.css']
})
export class FichaAtendimentoComponentComponent implements OnInit {
  formFicha: Ficha;
  situacaoTecnica: any;
  toasterService: ToasterService;
  param: any;


  constructor(private service: DataService, toasterService: ToasterService, public modal: NgbModal,
          private datePipe: DatePipe, private authenticationService: AuthenticationService,
          private modalClienteService: ModalClienteService,
          private modalService: ModalService, private pecaServicoOrdemService: PecaServicoOrdemService,
        private modalAtendimento: ModalAtendimentoService,
        private route: ActivatedRoute,private fichaAtendimentoService: FichaAtendimentoService) { 
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

    this.route.params.subscribe(
      params => {
        this.param = params['id']; 
    });
    if (this.param !== undefined) {
      this.service.get('/ficha/buscar?id=' + this.param).subscribe(response => {
        this.loadFicha(response);
      }, (error) => {
        console.log('error in', error);
        // this.toasterService.pop('error', 'Empresa', error.mensagem);
      });
    } else {
      this.formFicha.dataAbertura = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
      this.formFicha.lancamento.situacao = 'Aberto';
      this.formFicha.tipoServico = 'Assitencia';
      this.getNomeUsario();
    }
  }

  onSubmit() {
    if (this.formFicha.lancamento.situacao === 'Aberto') {
      this.formFicha.lancamento.sequencia = 0;
      this.formFicha.lancamento.observacao = 'Abertura'
      this.formFicha.lancamento.data = this.formFicha.dataAbertura
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

  loadFicha(data) {
    this.formFicha.numeroFicha = data.numeroFicha;
    this.formFicha.tipoServico = data.tipoServico;
    this.formFicha.cliente = new Cliente();
    this.formFicha.cliente.cnpj = data.cliente.cnpj
    this.formFicha.cliente.fone = data.cliente.fone;
    this.formFicha.cliente.celular = data.cliente.celular;
    this.formFicha.cliente.nome = data.cliente.nome;
    debugger;
    for (let i=0; i < data.lancamentoLst.length; i++) {
      if (i===0) {
        this.formFicha.dataAbertura = this.datePipe.transform(data.lancamentoLst[i].data, 'dd/MM/yyyy');
        this.formFicha.responsavel = data.lancamentoLst[i].nomeUsuario;
        
      }
      if (i + 1 === data.lancamentoLst.length) {
        this.formFicha.lancamento.situacao = data.lancamentoLst[i].situacao;
      }
      this.formFicha.lancamentoLst.push(data.lancamentoLst[i])
    }
    this.formFicha.pecaOutroServicoDto = data.pecaOutroServicoDto;
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
        this.formFicha.lancamento.nomeUsuario = response.nome;
        this.formFicha.responsavel = response.nome;
        this.formFicha.lancamento.idUsuario = response.id;
    }, (error) => {
      console.log('error in', error);
    });
  }

  addLinha(event) {
    console.log(event.descricao);
    event.sequencia = this.formFicha.pecaOutroServicoDto.length + 1;
    event.idOrdem = this.formFicha.numeroFicha;

    this.service.post('/ficha/pecaServico', event).subscribe(response => {
      console.log(response);
      this.pecaServicoOrdemService.emitirResultado.emit('gravou');
      this.formFicha.pecaOutroServicoDto.push(event);
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
    event.id =  this.formFicha.numeroFicha;

    this.service.post('/ficha/atendimento', event).subscribe(response => {
      console.log(response);
      this.fichaAtendimentoService.emitirResultado.emit('gravou');
      this.formFicha.atendimento.push(event);
      // this.toasterService.pop('success', 'Ordem de Serviço', 'Ordem de serviço cadastrado com sucesso!');
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.fichaAtendimentoService.emitirResultado.emit('falhou');
      // this.toasterService.pop('error', 'Ordem de Serviço', error.error.mensagem);
    });

    
  }

  loadForm(data) {
    this.formFicha.cliente.cnpj = data.cnpj;
    this.formFicha.cliente.nome = data.nome;
    this.formFicha.cliente.celular = data.celular;
    this.formFicha.cliente.fone = data.fone;
    this.formFicha.cliente.id = data.id;
  }

}
