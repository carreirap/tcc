import { Component, OnInit, Input, Output } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Atendimento } from '../_models/atendimento';
import { ModalAtendimentoService } from './modal-atendimento-service';
import { FichaAtendimentoService } from '../ficha-atendimento-component/ficha-atendimento-service';
import { DataService } from '../_services/http.service';
import { DatePipe } from '../../../node_modules/@angular/common';


@Component({
  selector: 'app-modal-atendimento',
  templateUrl: './modal-atendimento.component.html',
  styleUrls: ['./modal-atendimento.component.css']
})
export class ModalAtendimentoComponent implements OnInit {
  formAtendimento = new Atendimento();
  @Input() cValue;
  @Input() dValue;
  error: string;

  constructor(private ngModal: NgbModal, private modalService: ModalAtendimentoService, private datePipe: DatePipe,
    private fichaAtendimentoService: FichaAtendimentoService,
    private service: DataService) { }


  ngOnInit() {
    this.fichaAtendimentoService.emitirResultado.subscribe(
      // result => this.addLinha(result)
      result => {
        if (result === 'gravou') {
          this.limpar();
        }
      });
  }

  calcular(event) {
    this.service.get('/ficha/calcAtendimento?horas=' + this.formAtendimento.duracao + '&tipo=' +
      this.formAtendimento.descricao).subscribe(response => {
        this.loadValor(response);
      }, (error) => {
        console.log('error in', error.error.mensagem);
      });
    return false;

  }

  onSubmit() {
    if (this.formAtendimento.duracao === undefined) {
      this.error = 'error';
    }
    if (this.formAtendimento.descricao === undefined || this.formAtendimento.descricao === '') {
      this.error = 'error';
    }
    if (this.formAtendimento.dataAtendimento === undefined) {
      this.error = 'error';
    }
    const atend = new Atendimento();
    atend.duracao = this.formAtendimento.duracao;
    atend.descricao = this.formAtendimento.tipo[parseInt(this.formAtendimento.descricao)].label;
    atend.valor = this.formAtendimento.valor;
    atend.dataAtendimento = this.datePipe.transform(this.formAtendimento.dataAtendimento, 'dd/MM/yyyy');
    if (this.formAtendimento.duracao !== undefined) {
      this.modalService.enviarLinhaPaginaChamadora(atend);
    }
  }

  limpar() {
    this.formAtendimento.descricao = '';
    this.formAtendimento.duracao = undefined;
    this.formAtendimento.valor = undefined;
    this.formAtendimento.dataAtendimento = ''
    this.error = undefined;
    return false;
  }

  loadValor(data) {
    this.formAtendimento.valor = data;
  }



}
