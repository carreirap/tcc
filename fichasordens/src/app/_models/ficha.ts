import { Cliente } from './cliente';
import { PecaServicoOrdem } from './peca-servico-ordem';
import { Lancamento } from './lancamentosTecnicos';


export class Ficha {
    numeroFicha: number;
    cliente: Cliente;
    tipoServico: string;
    itemTables: Array<PecaServicoOrdem>;
    dataAbertura: string;
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