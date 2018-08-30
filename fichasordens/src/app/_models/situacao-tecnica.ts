export class SituacaoTecnica {
    situacao: Array<any> = [
        { value: 'Aberto', label: 'Aberto' },
        { value: 'Fechado', label: 'Fechado' },
        { value: 'Trabalhando', label: 'Trabalhando' },
        { value: 'Aguardando', label: 'Aguardando' },
        { value: 'Finalizado', label: 'Finalizado' },
        { value: 'Faturado', label: 'Faturado' }
    ];

    situacaoTodas: Array<any> = [
        { value: 'Todas', label: 'Todas' },
        { value: 'Aberto', label: 'Aberto' },
        { value: 'Aguardando', label: 'Aguardando' },
        { value: 'Faturado', label: 'Faturado' },
        { value: 'Fechado', label: 'Fechado' },
        { value: 'Finalizado', label: 'Finalizado' },
        { value: 'Trabalhando', label: 'Trabalhando' }
    ];

    situacaoAberto: Array<any> = [
        { value: 'Aberto', label: 'Aberto' },
        { value: 'Cancelado', label: 'Cancelado' },
        { value: 'Trabalhando', label: 'Trabalhando' }
    ];

    situacaoTrabalhando: Array<any> = [
        { value: 'Trabalhando', label: 'Trabalhando' },
        { value: 'Aguardando', label: 'Aguardando' },
        { value: 'Finalizado', label: 'Finalizado' },
        { value: 'Cancelado', label: 'Cancelado' }
    ];

    situacaoAguardando: Array<any> = [
        { value: 'Aguardando', label: 'Aguardando' },
        { value: 'Trabalhando', label: 'Trabalhando' }
    ];

    situacaoFinalizado: Array<any> = [
        { value: 'Finalizado', label: 'Finalizado' },
        { value: 'Faturado', label: 'Faturado' }
    ];

    situacaoFaturado: Array<any> = [
        { value: 'Faturado', label: 'Faturado' },
        { value: 'Fechado', label: 'Fechado' }
    ];


    public getTodasSituacao() {
        return this.situacao;
    }
    public getSituacoes_Todas() {
        return this.situacaoTodas;
    }

    public getSituacoesBaseadoNoAtual(atual) {
        if (atual === 'Aberto') {
            return this.situacaoAberto;
        }
        if (atual === 'Trabalhando') {
            return this.situacaoTrabalhando;
        }
        if (atual === 'Aguardando') {
            return this.situacaoAguardando;
        }
        if (atual === 'Finalizado') {
            return this.situacaoFinalizado;
        }
        if (atual === 'Faturado') {
            return this.situacaoFaturado;
        }
    }
}
