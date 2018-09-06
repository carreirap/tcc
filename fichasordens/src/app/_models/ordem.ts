import { Cliente } from './cliente';
import { PecaServicoOrdem } from './peca-servico-ordem';
import { Lancamento } from './lancamentosTecnicos';


export class Ordem {
    numeroOrdem: number;
    cliente: Cliente;
    tipoServico: string;
    descDefeito: string;
    descEquip: string;
    estadoItensAcomp: string;
    itemTables: Array<PecaServicoOrdem>;
    fabricante: string;
    modelo: string;
    serie: string;
    atualSituacao: string;
    dataAbertura: string;
    dataSaida: string;
    responsavel: string;
    lancamento:  Lancamento;
    lancamentoLst:  Array<Lancamento>;

    constructor () {
        this.cliente = new Cliente();
        this.itemTables = new Array<PecaServicoOrdem>();
        this.lancamento = new  Lancamento();
        this.lancamentoLst = Array<Lancamento>();
    }
}
