import { Component, OnInit } from '@angular/core';
import { Historico } from '../_models/historicoServicos';
import { SituacaoTecnica } from '../_models/situacao-tecnica';
import { DataService } from '../_services';
import { ToasterService} from 'angular5-toaster';
import { TipoServico } from '../_models/tipoServico';

@Component({
  selector: 'app-historico',
  templateUrl: './historico.component.html',
  styleUrls: ['./historico.component.css']
})
export class HistoricoComponent implements OnInit {
  formHist: Historico;
  situacaoTecnica: SituacaoTecnica;
  situacao: any;
  toasterService: ToasterService;
  tipoServico: TipoServico;
  content: Array<any>;
  pages = 0;
  page = 0;
  path = "";

  constructor(private service: DataService, toasterService: ToasterService) {
    this.formHist = new Historico();
    this.situacaoTecnica = new SituacaoTecnica();
    
    this.toasterService = toasterService;
   }

  ngOnInit() {
    this.tipoServico = new TipoServico();
    this.formHist.situacao = "Todas"
    this.formHist.tipo = 'Ficha';
    this.situacao = this.situacaoTecnica.getSituacoes_Todas();
    console.log(this.situacao);
  }

  onSubmit() {
    this.content = undefined;
    this.service.post('/historico?page=' +
            this.page + '&size=1&sort=id,DESC', this.formHist).subscribe(response => {
              this.loadHistorico(response);
    this.toasterService.pop('success', 'Pesquisa Historico', 'Pesquisa Realizado com sucesso!');
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.toasterService.pop('error', 'Pesquisa Historico', error.error.mensagem);
    });
  }

  public loadHistorico(response: any) {
    if (this.formHist.tipo == "Ficha") {
      this.path = '/fichas';
    } else {
      this.path = '/ordem';
    }

    this.content = response.content;
    this.pages = response['totalPages'];
  }

  public paginate(event) {
    this.page = event.page;
    this.onSubmit();
   }
}
