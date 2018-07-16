import { Injectable, EventEmitter, Output } from '@angular/core';
import { PecaServicoOrdem } from '../_models/peca-servico-ordem';

@Injectable()
export class ModalService {

    @Output() carregarLinha: EventEmitter<PecaServicoOrdem> = new EventEmitter();

    constructor() { }

    enviarLinhaPaginaChamadora(data) {
        this.carregarLinha.emit(data);
    }

}
