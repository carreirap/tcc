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
import { ActivatedRoute } from '../../../node_modules/@angular/router';
import { SituacaoTecnica } from '../_models/situacao-tecnica';
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
  situacaoTecnica: any;
  responsavel: any;
  item: PecaServicoOrdem;
  selectedPecaServico: PecaServicoOrdem;
  page = 0;
  content: Array<any>;
  pages = 0;
  nomePesquisa: String;
  cnpjPesquisa: String;
  typePesquisa = '';
  param: any;
  situacao: any;

  @ViewChild('cpfcnpjvalidation') pwConfirmModel: NgModel;
  constructor(private service: DataService, toasterService: ToasterService, public modal: NgbModal,
              private datePipe: DatePipe, private userService: UserService,
              private modalService: ModalService, private pecaServicoOrdemService: PecaServicoOrdemService,
              private modalClienteService: ModalClienteService,
              private authenticationService: AuthenticationService,
              private route: ActivatedRoute
              ) {
    this.toasterService = toasterService;
    this.situacaoTecnica = new SituacaoTecnica();
  }

  ngOnInit() {
    this.route.params.subscribe(
      params => {
        this.param = params['id'];
        console.log(this.param);
    });
    if (this.param !== undefined) {
      this.service.get('/ordem/buscar?id=' + this.param).subscribe(response => {
        this.loadOrdem(response);
        // this.formFicha.lancamento.data = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
      }, (error) => {
        console.log('error in', error);
      });

    } else {
      this.formOrdem.lancamento = new Lancamento();
      this.getNomeUsario();
      this.modalService.carregarLinha.subscribe(
        result => this.addLinha(result)
      );

      this.modalClienteService.carregarCliente.subscribe(
        result => this.loadForm(result)
      );
      this.formOrdem.lancamento.situacao = 'Aberto';
      this.formOrdem.lancamento.data = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
    }
    
  }

  private getNomeUsario() {
      let userLog = new UsuarioLogado();
      userLog.usuario = JSON.parse(localStorage.getItem('currentUser')).usuario;
      this.authenticationService.getUpdatedUser(userLog).subscribe(response => {
          this.formOrdem.lancamento.nomeUsuario = response.nome;
          this.responsavel = this.formOrdem.lancamento.nomeUsuario;
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

  removePecaOutroServico() {
    // tslint:disable-next-line:max-line-length
    this.service.delete('/ordem/pecaServico?id=' + this.selectedPecaServico.idOrdem + '&sequencia=' + this.selectedPecaServico.sequencia).subscribe(response => {
      console.log(response);
      this.toasterService.pop('success', 'Ordem de Serviço', 'Peça/Outro Servico excluido com sucesso');
      this.formOrdem.itemTables.splice(this.formOrdem.itemTables.indexOf(this.selectedPecaServico), 1);
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.toasterService.pop('error', 'Ordem de Serviço', error.error.mensagem);
    });
    return false;
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
  loadOrdem(data) {
    this.formOrdem.numeroOrdem = data.numeroOrdem;
    this.formOrdem.tipoServico = data.tipoServico;
    this.formOrdem.cliente.id = data.cliente.id;
    this.formOrdem.cliente.nome = data.cliente.nome;
    this.formOrdem.cliente.celular = data.cliente.celular;
    this.formOrdem.cliente.fone = data.cliente.fone;
    this.formOrdem.cliente.cnpj = data.cliente.cnpj;
    this.formOrdem.descDefeito = data.descDefeito;
    this.formOrdem.descEquip = data.descEquip;
    this.formOrdem.estadoItensAcomp = data.estadoItensAcomp;
    this.formOrdem.modelo = data.modelo;
    this.formOrdem.fabricante = data.fabricante;
    this.formOrdem.serie = data.serie;

    for (let i = 0; i < data.lancamentoLst.length; i++) {
      if (i === 0) {
        this.formOrdem.dataAbertura = this.datePipe.transform(data.lancamentoLst[i].data, 'dd/MM/yyyy');
        this.formOrdem.responsavel = data.lancamentoLst[i].nomeUsuario;
      }
      data.lancamentoLst[i].data = this.datePipe.transform(data.lancamentoLst[i].data, 'dd/MM/yyyy');
      if (i + 1 === data.lancamentoLst.length) {
        this.formOrdem.lancamento.situacao = data.lancamentoLst[i].situacao;
        this.situacao = this.situacaoTecnica.getSituacoesBaseadoNoAtual(this.formOrdem.lancamento.situacao);
      }
      this.formOrdem.lancamentoLst.push(data.lancamentoLst[i]);
    }
    this.formOrdem.itemTables = data.pecaOutroServicoDto;
    
  }

}
