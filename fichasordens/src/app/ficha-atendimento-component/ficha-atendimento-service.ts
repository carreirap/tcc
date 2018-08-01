import { Injectable, EventEmitter, Output } from '@angular/core';

@Injectable()
export class FichaAtendimentoService {

    @Output() emitirResultado: EventEmitter<String> = new EventEmitter();

    constructor() { }

    enviarRetorno(data) {
        this.emitirResultado.emit(data);
    }

}
