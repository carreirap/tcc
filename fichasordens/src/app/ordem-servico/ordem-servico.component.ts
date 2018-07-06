import { Component, OnInit } from '@angular/core';
import { ToasterService} from 'angular5-toaster';
import { DataService } from '../_services';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Ordem } from '../_models/ordem';
import { ItemTable } from '../_models/item-table';

@Component({
  selector: 'app-ordem-servico',
  templateUrl: './ordem-servico.component.html',
  styleUrls: ['./ordem-servico.component.css']
})
export class OrdemServicoComponent implements OnInit {
  toasterService: ToasterService;
  formOrdem = new Ordem();
  item: ItemTable;
  situacaoSelect: Array<any> = [
    { value: 'Aberto', label: 'Aberto' },
    { value: 'Trabalhando', label: 'Trabalhando' },
    { value: 'Aguardando', label: 'Aguardando' }
  ];
  constructor(private service: DataService, toasterService: ToasterService, public modal: NgbModal) {
    this.toasterService = toasterService;
  }

  ngOnInit() {
    this.item = new ItemTable();
    this.item.qtde = 1;
    this.item.descricao = 'Descricao';
    this.item.valor = 5680;
    this.item.garantia = 3;
    this.item.nf = 69;
    this.formOrdem.itemTables.push(this.item);
  }

  mostrarModal(clienteModal) {
    this.modal.open(clienteModal);
    return false;
  }

}
