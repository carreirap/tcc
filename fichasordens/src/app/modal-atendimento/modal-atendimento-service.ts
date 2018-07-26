import { Injectable, EventEmitter, Output } from '@angular/core';
import { PecaServicoOrdem } from '../_models/peca-servico-ordem';
import { Atendimento } from '../_models/atendimento';

@Injectable()
export class ModalAtendimentoService {

    @Output() carregarLinha: EventEmitter<Atendimento> = new EventEmitter();

    constructor() { }

    enviarLinhaPaginaChamadora(data) {
        this.carregarLinha.emit(data);
    }

}