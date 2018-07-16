import { Cliente } from './cliente';
import { PecaServicoOrdem } from './peca-servico-ordem';
import { OrdemServicoLanc } from './lancamentosTecnicos';


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
    // situacao: string;
    dataAbertura: string;
    dataSaida: string;
    responsavel: string;
    ordemServicoLanc:  OrdemServicoLanc;
    ordemServicoLancLst:  Array<OrdemServicoLanc>;

    constructor () {
        this.cliente = new Cliente();
        this.itemTables = new Array<PecaServicoOrdem>();
        this.ordemServicoLanc = new  OrdemServicoLanc();
        this.ordemServicoLancLst = Array<OrdemServicoLanc>();
    }
}
