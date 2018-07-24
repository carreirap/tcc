export class SituacaoTecnica {
    situacao: Array<any> = [
        { value: 'Aberto', label: 'Aberto' },
        { value: 'Fechado', label: 'Fechado' },
        { value: 'Trabalhando', label: 'Trabalhando' },
        { value: 'Aguardando', label: 'Aguardando' },
        { value: 'Finalizado', label: 'Finalizado' },
        { value: 'Faturado', label: 'Faturado' }
    ];
    
    public getSituacao() {
        return this.situacao;
    }
}