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

  constructor(private service: DataService, toasterService: ToasterService) {
    this.formHist = new Historico();
    this.situacaoTecnica = new SituacaoTecnica();
    
    this.toasterService = toasterService;
   }

  ngOnInit() {
    this.tipoServico = new TipoServico();
    this.situacao = this.situacaoTecnica.getTodasSituacao();
    console.log(this.situacao);
  }

  onSubmit() {
    this.service.post('/historico', this.formHist).subscribe(response => {
    this.toasterService.pop('success', 'Pesquisa Historico', 'Pesquisa Realizado com sucesso!');
    }, (error) => {
      console.log('error in', error.error.mensagem);
      this.toasterService.pop('error', 'Pesquisa Historico', error.error.mensagem);
    });
  }

}
