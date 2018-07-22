import { Component, OnInit, ViewChild } from '@angular/core';
import { ToasterService} from 'angular5-toaster';
import { DataService, AuthenticationService } from '../_services';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Ficha } from '../_models/ficha';
import { DatePipe } from '@angular/common';
import { UsuarioLogado } from '../_models/usuario-logado';
import { SituacaoTecnica } from '../_models/situacao-tecnica';
import { ModalClienteService } from '../modal-pesquisa-cliente/modal-cliente-service';


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
          private modalClienteService: ModalClienteService) { 
    this.formFicha = new Ficha();
    this.situacaoTecnica = new SituacaoTecnica();
    this.toasterService = toasterService;
  }

  ngOnInit() {
    this.modalClienteService.carregarCliente.subscribe(
      result => this.loadForm(result)
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

  loadForm(data) {
    this.formFicha.cliente.cnpj = data.cnpj;
    this.formFicha.cliente.nome = data.nome;
    this.formFicha.cliente.celular = data.celular;
    this.formFicha.cliente.fone = data.fone;
    this.formFicha.cliente.id = data.id;
  }

}
