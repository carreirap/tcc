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
import { Atendimento } from '../_models/atendimento';
import { PecaServicoOrdem } from '../_models/peca-servico-ordem';


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
  // selectedAtend: string[] = [];
  selectedAtend: Atendimento;
  selectedPecaServico: PecaServicoOrdem;
  situacao: any;

  constructor(private service: DataService, toasterService: ToasterService, public modal: NgbModal,
          private datePipe: DatePipe, private authenticationService: AuthenticationService,
          private modalClienteService: ModalClienteService,
          private modalService: ModalService, private pecaServicoOrdemService: PecaServicoOrdemService,
        private modalAtendimento: ModalAtendimentoService,
        private route: ActivatedRoute, private fichaAtendimentoService: FichaAtendimentoService) {
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
        this.formFicha.lancamento.data = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
      }, (error) => {
        console.log('error in', error);
        // this.toasterService.pop('error', 'Empresa', error.mensagem);
      });
    } else {
      this.formFicha.dataAbertura = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
      this.formFicha.lancamento.situacao = 'Aberto';
      this.formFicha.tipoServico = 'Assitencia';
    }
    this.getNomeUsario();
  }

  onSubmit() {
    if (this.formFicha.lancamento.situacao === 'Aberto') {
      this.formFicha.lancamento.sequencia = 0;
      this.formFicha.lancamento.observacao = 'Abertura';
      this.formFicha.lancamento.data = this.formFicha.dataAbertura;
    } else {
      this.getSequenciaLancamento();
    }
    this.service.post('/ficha', this.formFicha).subscribe(response => {
      console.log(response);
      this.setNumeroFicha(response);
      this.formFicha.lancamentoLst.push(this.formFicha.lancamento);
      this.toasterService.pop('success', 'Ficha de Atendimento', 'Ficha de Atendimento cadastrado com sucesso!');
      this.situacao = this.situacaoTecnica.getSituacoesBaseadoNoAtual(this.formFicha.lancamento.situacao);
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.toasterService.pop('error', 'Ficha de Atendimento', error.error.mensagem);
    });
  }

  getSequenciaLancamento() {
    let x = 0;
    for (let i = 0; i < this.formFicha.lancamentoLst.length; i++) {
      x = this.formFicha.lancamentoLst[i].sequencia;
    }
    this.formFicha.lancamento.sequencia = x + 1;
  }

  remove() {
    console.log(this.selectedAtend);
    // tslint:disable-next-line:max-line-length
    this.service.delete('/ficha/atendimento?idFicha=' + this.selectedAtend.id + '&sequencia=' + this.selectedAtend.sequencia).subscribe(response => {
      console.log(response);
      this.toasterService.pop('success', 'Ficha de Atendimento', 'Atendimento excluido com sucesso');
      this.formFicha.atendimento.splice(this.formFicha.atendimento.indexOf(this.selectedAtend), 1);
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.toasterService.pop('error', 'Ficha de Atendimento', error.error.mensagem);
    });
    return false;
  }

  removePecaOutroServico() {
    console.log(this.selectedAtend);
    // tslint:disable-next-line:max-line-length
    this.service.delete('/ficha/pecaServico?idFicha=' + this.selectedPecaServico.idOrdem + '&sequencia=' + this.selectedPecaServico.sequencia).subscribe(response => {
      console.log(response);
      this.toasterService.pop('success', 'Ficha de Atendimento', 'Peça/Outro Servico excluido com sucesso');
      this.formFicha.pecaOutroServicoDto.splice(this.formFicha.pecaOutroServicoDto.indexOf(this.selectedPecaServico), 1);
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.toasterService.pop('error', 'Ficha de Atendimento', error.error.mensagem);
    });
    return false;
  }

  loadFicha(data) {
    this.formFicha.numeroFicha = data.numeroFicha;
    this.formFicha.tipoServico = data.tipoServico;
    this.formFicha.cliente = new Cliente();
    this.formFicha.cliente.cnpj = data.cliente.cnpj;
    this.formFicha.cliente.fone = data.cliente.fone;
    this.formFicha.cliente.celular = data.cliente.celular;
    this.formFicha.cliente.nome = data.cliente.nome;
    this.formFicha.cliente.id = data.cliente.id;

    for (let i = 0; i < data.lancamentoLst.length; i++) {
      if (i === 0) {
        this.formFicha.dataAbertura = this.datePipe.transform(data.lancamentoLst[i].data, 'dd/MM/yyyy');
        this.formFicha.responsavel = data.lancamentoLst[i].nomeUsuario;
      }
      data.lancamentoLst[i].data = this.datePipe.transform(data.lancamentoLst[i].data, 'dd/MM/yyyy');
      if (i + 1 === data.lancamentoLst.length) {
        this.formFicha.lancamento.situacao = data.lancamentoLst[i].situacao;
        this.situacao = this.situacaoTecnica.getSituacoesBaseadoNoAtual(this.formFicha.lancamento.situacao);
      }
      this.formFicha.lancamentoLst.push(data.lancamentoLst[i]);
    }
    this.formFicha.pecaOutroServicoDto = data.pecaOutroServicoDto;
    for (let i = 0; i < data.atendimento.length; i++) {
      data.atendimento[i].dataAtendimento = this.datePipe.transform(data.atendimento[i].dataAtendimento, 'dd/MM/yyyy');
      this.formFicha.atendimento.push(data.atendimento[i]);
    }

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
    const userLog = new UsuarioLogado();
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
