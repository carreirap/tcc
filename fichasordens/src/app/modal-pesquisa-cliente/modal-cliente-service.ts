import { Injectable, EventEmitter, Output } from '@angular/core';
import { PecaServicoOrdem } from '../_models/peca-servico-ordem';

@Injectable()
export class ModalClienteService {

    @Output() carregarCliente: EventEmitter<PecaServicoOrdem> = new EventEmitter();

    constructor() { }

    enviarDadosCliente(data) {
        this.carregarCliente.emit(data);
    }

}
