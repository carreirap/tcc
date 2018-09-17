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
import { saveAs } from 'file-saver/FileSaver';
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
  totalPecaOutros: number;
  
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
    this.modalService.carregarLinha.subscribe(
      result => this.addLinha(result)
    );

    this.modalClienteService.carregarCliente.subscribe(
      result => this.loadForm(result)
    );

    this.route.params.subscribe(
      params => {
        this.param = params['id'];
        console.log(this.param);
    });
    if (this.param !== undefined) {
      this.service.get('/ordem/buscar?id=' + this.param).subscribe(response => {
        this.loadOrdem(response);
        this.formOrdem.lancamento.data = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
      }, (error) => {
        console.log('error in', error);
      });

    } else {
      this.formOrdem.lancamento = new Lancamento();
      this.formOrdem.tipoServico = 'Instalacao';
      this.formOrdem.lancamento.situacao = 'Aberto';
      this.formOrdem.atualSituacao = this.formOrdem.lancamento.situacao;
      //this.situacao = this.situacaoTecnica.getSituacaoAbertura();
      this.formOrdem.dataAbertura = this.datePipe.transform(new Date(), 'dd/MM/yyyy');
      //this.formOrdem.lancamento.data = this.formOrdem.dataAbertura;
    }
    this.getNomeUsario();
  }

  private getNomeUsario() {
      let userLog = new UsuarioLogado();
      userLog.usuario = JSON.parse(localStorage.getItem('currentUser')).usuario;
      this.authenticationService.getUpdatedUser(userLog).subscribe(response => {
          this.formOrdem.lancamento.nomeUsuario = response.nome;
          this.formOrdem.responsavel = this.formOrdem.lancamento.nomeUsuario;
          this.formOrdem.lancamento.idUsuario = response.id;
      }, (error) => {
        console.log('error in', error);
      });
  }

  download() {
    this.service.getPdf('/ordem/pdf?id=' + this.formOrdem.numeroOrdem).subscribe(response => {
      this.open(response);
    }); 
  }

  private open(pdf) {
    const blob = new Blob([pdf] , { type: 'application/pdf' });
    saveAs(blob, "ordem-servico" + this.formOrdem.numeroOrdem + ".pdf");
  }

  onSubmit() {
    if (this.formOrdem.lancamento.situacao === 'Aberto') {
      this.formOrdem.lancamento.sequencia = 0;
      this.formOrdem.lancamento.data = this.formOrdem.dataAbertura;
      this.formOrdem.lancamento.observacao = "Abertura da Ordem de Serviço."
    } else {
      this.getSequenciaLancamento();
    }
    this.service.post('/ordem', this.formOrdem).subscribe(response => {
      console.log(response);
      this.setNumeroOrdem(response);
      this.formOrdem.atualSituacao = this.formOrdem.lancamento.situacao;
      this.formOrdem.lancamentoLst.push(this.formOrdem.lancamento);
      this.toasterService.pop('success', 'Ordem de Serviço', this.montaMensagemAtualizacaoSituacao(this.formOrdem.atualSituacao));
      this.situacao = this.situacaoTecnica.getSituacoesBaseadoNoAtual(this.formOrdem.lancamento.situacao);
      if (this.formOrdem.lancamento.situacao === 'Fechado') {
        this.formOrdem.dataSaida = this.formOrdem.lancamento.data;
      }
    }, (error) => { 
      console.log('error in', error.error.mensagem);
      this.toasterService.pop('error', 'Ordem de Serviço', error.error.mensagem);
    });
  }

  montaMensagemAtualizacaoSituacao(situacao) {
    let sit = ""
    if (situacao === "Aberto")
      sit = 'Ordem de serviço cadastrada com sucesso!'
    if (situacao === "Cancelado") 
      sit = 'Ordem de serviço cancelada com sucesso!'
    if (situacao === "Aguardando" || situacao === "Trabalhando" || situacao === "Faturado" || situacao === "Finalizado")  
      sit = 'Ordem de serviço alterada com sucesso!'
    if (sit === "Fechado")  
      sit = 'Ordem de serviço fechada com sucesso!'
    return sit;  
  }
  

  getSequenciaLancamento() {
    let x = 0;
    for (let i = 0; i < this.formOrdem.lancamentoLst.length; i++) {
      x = this.formOrdem.lancamentoLst[i].sequencia;
    }
    this.formOrdem.lancamento.sequencia = x + 1;
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
      this.calcularTotalLinha(event);
      this.formOrdem.itemTables.push(event);
      this.calcularTotalTabela(this.formOrdem.itemTables);
      // this.toasterService.pop('success', 'Ordem de Serviço', 'Ordem de serviço cadastrado com sucesso!');
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.pecaServicoOrdemService.emitirResultado.emit('falhou');
      // this.toasterService.pop('error', 'Ordem de Serviço', error.error.mensagem);
    });
  }

  calcularTotalLinha(event) {
    event.total = (event.valor * event.qtde);
    event.total = this.roundNumber(event.total, 2);
  }

  calcularTotalTabela(table) {
    let total = 0;
    for(let i=0; i < table.length; i++) {
      total = total + table[i].total;
      
    }
    this.totalPecaOutros = this.roundNumber(total, 2);
  }

  roundNumber(number, decimals) {
    var newnumber = new Number(number+'').toFixed(parseInt(decimals));
    return parseFloat(newnumber); 
  }

  removePecaOutroServico() {
    // tslint:disable-next-line:max-line-length
    this.service.delete('/ordem/pecaServico?id=' + this.selectedPecaServico.idOrdem + '&sequencia=' + this.selectedPecaServico.sequencia).subscribe(response => {
      console.log(response);
      this.toasterService.pop('success', 'Ordem de Serviço', 'Peça/Outro Servico excluido com sucesso');
      this.formOrdem.itemTables.splice(this.formOrdem.itemTables.indexOf(this.selectedPecaServico), 1);
      this.calcularTotalTabela(this.formOrdem.itemTables);
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
     

    
    this.calcularTotalTabela(data.pecaOutroServicoDto);
    this.formOrdem.itemTables = data.pecaOutroServicoDto;
    for (let i = 0; i < data.lancamentoLst.length; i++) {
      if (i === 0) {
        this.formOrdem.dataAbertura = this.datePipe.transform(data.lancamentoLst[i].data, 'dd/MM/yyyy');
        this.formOrdem.responsavel = data.lancamentoLst[i].nomeUsuario;
      }
      data.lancamentoLst[i].data = this.datePipe.transform(data.lancamentoLst[i].data, 'dd/MM/yyyy');
      if (i + 1 === data.lancamentoLst.length) {
        this.formOrdem.lancamento.situacao = data.lancamentoLst[i].situacao;
        this.formOrdem.atualSituacao = this.formOrdem.lancamento.situacao;
        this.situacao = this.situacaoTecnica.getSituacoesBaseadoNoAtual(this.formOrdem.lancamento.situacao);
      }
      // if (i + 1 <  data.lancamentoLst.length)
      this.formOrdem.lancamentoLst.push(data.lancamentoLst[i]);
      if (data.lancamentoLst[i].situacao === 'Fechado') {
        this.formOrdem.dataSaida = data.lancamentoLst[i].data;
      }  
    }
    
  }

  mostrarBotoes() {
    if (this.formOrdem.atualSituacao !== 'Aberto' && 
    this.formOrdem.atualSituacao !== 'Faturado' && 
    this.formOrdem.atualSituacao !== 'Fechado' && 
    this.formOrdem.atualSituacao !== 'Finalizado'&& 
    this.formOrdem.atualSituacao !== 'Cancelado' && 
    this.formOrdem.atualSituacao !== 'Aguardando') {
      return true;
    } else {
      return false;
    }
  }

}
