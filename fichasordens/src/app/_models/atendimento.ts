export  class Atendimento {
    sequencia: number;
    usuario: string;
    idUsuario: number;
    dataAtendimento: string;
    descricao: string;
    duracao: number;
    valor:number;
    id: number;

    tipo: Array<any> = [
        { value: '0', label: 'Visita Tecnica' },
        { value: '1', label: 'Hora Tecnica' }];
}