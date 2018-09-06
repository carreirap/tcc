import { Cliente } from './cliente';
import { PecaServicoOrdem } from './peca-servico-ordem';
import { Lancamento } from './lancamentosTecnicos';
import { Atendimento } from './atendimento';


export class Ficha {
    numeroFicha: number;
    cliente: Cliente;
    tipoServico: string;
    pecaOutroServicoDto: Array<PecaServicoOrdem>;
    dataAbertura: string;
    dataFechamento: string;
    atualSituacao: string;
    responsavel: string;
    lancamento:  Lancamento;
    lancamentoLst:  Array<Lancamento>;
    atendimento: Array<Atendimento>;

    constructor () {
        this.cliente = new Cliente();
        this.pecaOutroServicoDto = new Array<PecaServicoOrdem>();
        this.lancamento = new  Lancamento();
        this.lancamentoLst = new Array<Lancamento>();
        this.atendimento = new Array<Atendimento>();
    }
}
