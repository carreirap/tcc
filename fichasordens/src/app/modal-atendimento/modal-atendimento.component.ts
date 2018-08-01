import { Component, OnInit, Input, Output } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Atendimento } from '../_models/atendimento';
import { ModalAtendimentoService } from './modal-atendimento-service';
import { FichaAtendimentoService } from '../ficha-atendimento-component/ficha-atendimento-service';


@Component({
  selector: 'app-modal-atendimento',
  templateUrl: './modal-atendimento.component.html',
  styleUrls: ['./modal-atendimento.component.css']
})
export class ModalAtendimentoComponent implements OnInit {
  formAtendimento = new Atendimento();
  @Input() cValue;
  @Input() dValue;

  constructor(private ngModal: NgbModal, private modalService: ModalAtendimentoService, private fichaAtendimentoService: FichaAtendimentoService) { }

  
  ngOnInit() {
    this.fichaAtendimentoService.emitirResultado.subscribe(
      // result => this.addLinha(result)
      result => {
        if (result === 'gravou') {
          this.limpar();
        }
      });
  }

  onSubmit() {
    const atend = new Atendimento();
    atend.duracao = this.formAtendimento.duracao;
    atend.descricao = this.formAtendimento.descricao;
    atend.valor = this.formAtendimento.valor;
    atend.dataAtendimento = this.formAtendimento.dataAtendimento;
    if (this.formAtendimento.duracao !== undefined) {
      this.modalService.enviarLinhaPaginaChamadora(atend);
    }
  }

  limpar() {
    this.formAtendimento.descricao = '';
    this.formAtendimento.duracao = undefined;
    this.formAtendimento.valor = undefined;
    this.formAtendimento.dataAtendimento = ''
    return false;
  }

  

}
