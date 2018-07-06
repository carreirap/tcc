import { Cliente } from './cliente';
import { ItemTable } from './item-table';

export class Ordem {
    numeroOrdem: number;
    cliente: Cliente;
    tipoOrdem: string;
    descDefeito: string;
    descEquipamento: string;
    estadoItensAcompanha: string;
    itemTables: Array<ItemTable>;

    constructor () {
        this.cliente = new Cliente();
        this.itemTables = new Array<ItemTable>();
    }
}
