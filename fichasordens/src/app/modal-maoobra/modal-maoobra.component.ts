import { Component, OnInit, Input, Output } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalService } from './modal-service';
import { PecaServicoOrdemService } from '../ordem-servico/ordem-servico-service';
import { PecaServicoOrdem } from '../_models/peca-servico-ordem';


@Component({
  selector: 'app-modal-maoobra',
  templateUrl: './modal-maoobra.component.html',
  styleUrls: ['./modal-maoobra.component.css']
})

export class ModalMaoobraComponent implements OnInit {
  formItemTable = new PecaServicoOrdem();
  @Input() cValue;
  @Input() dValue;
  error: string;

  constructor(private ngModal: NgbModal, private modalService: ModalService, private pecaServicoOrdemService: PecaServicoOrdemService) { }

  ngOnInit() {
    this.pecaServicoOrdemService.emitirResultado.subscribe(
      result => {
        if (result === 'gravou') {
          this.limpar();
        }
      });
  }


  onSubmit() {
    if (this.formItemTable.qtde === undefined) {
      this.error = 'error';
    }
    if (this.formItemTable.descricao === undefined || this.formItemTable.descricao === '') {
      this.error = 'error';
    }
    if (this.formItemTable.valor === undefined) {
      this.error = 'error';
    }
    const itemTable = new PecaServicoOrdem();
    itemTable.qtde = this.formItemTable.qtde;
    itemTable.descricao = this.formItemTable.descricao;
    itemTable.valor = this.formItemTable.valor;
    if (this.formItemTable.qtde !== undefined) {
      this.modalService.enviarLinhaPaginaChamadora(itemTable);
    }
  }


  limpar() {
    this.formItemTable.descricao = '';
    this.formItemTable.qtde = undefined;
    this.formItemTable.valor = undefined;
    return false;
  }
  

}
