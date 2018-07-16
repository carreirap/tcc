import { Injectable, EventEmitter, Output } from '@angular/core';

@Injectable()
export class PecaServicoOrdemService {

    @Output() emitirResultado: EventEmitter<String> = new EventEmitter();

    constructor() { }

    enviarRetorno(data) {
        this.emitirResultado.emit(data);
    }

}
