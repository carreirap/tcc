import { Cliente } from './cliente';
import { ItemTable } from './item-table';

export class Ordem {
    numeroOrdem: number;
    cliente: Cliente;
    tipoServico: string;
    descDefeito: string;
    descEquip: string;
    estadoItensAcomp: string;
    itemTables: Array<ItemTable>;
    fabricante: string;
    modelo: string;
    serie: string;
    situacao: string;
    dataAbertura: string;
    dataSaida: string;
    responsavel: string;

    constructor () {
        this.cliente = new Cliente();
        this.itemTables = new Array<ItemTable>();
    }
}
