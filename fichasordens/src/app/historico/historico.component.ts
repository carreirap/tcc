import { Component, OnInit } from '@angular/core';
import { Historico } from '../_models/historicoServicos';
import { SituacaoTecnica } from '../_models/situacao-tecnica';
import { DataService } from '../_services';
import { ToasterService} from 'angular5-toaster';
import { TipoServico } from '../_models/tipoServico';
import { DatePipe } from '../../../node_modules/@angular/common';

@Component({
  selector: 'app-historico',
  templateUrl: './historico.component.html',
  styleUrls: ['./historico.component.css']
})
export class HistoricoComponent implements OnInit {
  formHist: Historico;
  inicio: Date;
  fim: Date;
  situacaoTecnica: SituacaoTecnica;
  situacao: any;
  toasterService: ToasterService;
  tipoServico: TipoServico;
  content: Array<any>;
  pages = 0;
  page = 0;
  path = '';

  constructor(private service: DataService, toasterService: ToasterService, private datePipe: DatePipe) {
    this.formHist = new Historico();
    this.situacaoTecnica = new SituacaoTecnica();

    this.toasterService = toasterService;
   }

  ngOnInit() {
    this.tipoServico = new TipoServico();
    this.formHist.situacao = 'Todas';
    this.formHist.tipo = 'Ficha';
    this.situacao = this.situacaoTecnica.getSituacoes_Todas();
    console.log(this.situacao);
  }

  onSubmit() {
    this.content = undefined;
    if (this.inicio !== undefined) {
      this.inicio.setMinutes( this.inicio.getMinutes() + this.inicio.getTimezoneOffset() );
      this.formHist.inicio = this.datePipe.transform(this.inicio, 'dd/MM/yyyy');
    }
    if (this.fim !== undefined)  {
      this.fim.setMinutes( this.fim.getMinutes() + this.fim.getTimezoneOffset() );
      this.formHist.fim = this.datePipe.transform(this.fim, 'dd/MM/yyyy');
    }
    this.service.post('/historico?page=' +
            this.page + '&size=3&sort=id,DESC', this.formHist).subscribe(response => {
              this.loadHistorico(response);
    this.toasterService.pop('success', 'Pesquisa Historico', 'Pesquisa realizada com sucesso!');
    }, (error) => {
      console.log('error in', error.error.mensagem);-
      this.toasterService.pop('error', 'Pesquisa Historico', error.error.mensagem);
    });
  }

  public loadHistorico(response: any) {
    if (this.formHist.tipo === 'Ficha') {
      this.path = '/fichas';
    } else {
      this.path = '/ordem';
    }

    this.content = response.content;
    this.pages = response['totalPages'];
  }

  public paginate(event) {
    console.log(event.page);
    this.page = event.page;
    this.onSubmit();
   }
}
