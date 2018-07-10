import { Component, OnInit } from '@angular/core';
import { PaginatorModule } from 'primeng/paginator';
import { TableModule } from 'primeng/table';
import { DataService } from '../_services';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-modal-pesquisa-cliente',
  templateUrl: './modal-pesquisa-cliente.component.html',
  styleUrls: ['./modal-pesquisa-cliente.component.css']
})
export class ModalPesquisaClienteComponent implements OnInit {

  page = 0;
  content: Array<any>;
  pages = 0;
  nomePesquisa: String;
  cnpjPesquisa: String;
  typePesquisa = '';

  constructor(private service: DataService, public modal: NgbModal, private route: ActivatedRoute) { }

  ngOnInit() {
  }

  setPages(i, event: any) {
    event.preventDefault();
    this.page = i;
  }

  public loadClientes(response: any) {
    console.log(response);
    this.content = response.content;
    console.log(this.content);
    this.pages = response['totalPages'];
  }

  public pesquisarPage(type) {
    this.typePesquisa = type;
    if (this.typePesquisa === 'cnpjcpf') {
        this.service.get('/cliente?cnpjcpf=' + this.cnpjPesquisa + '&page=' +
        this.page + '&size=1&sort=nome,DESC').subscribe(response => {
            console.log(response);
            this.loadClientes(response);
        }, (error) => {
            console.log('error in', error.error.mensagem);
        });
    } else {
        if (this.nomePesquisa.length > 3 ) {
            this.service.get('/cliente?nome=' + this.nomePesquisa + '&page=' +
            this.page + '&size=1&sort=nome,DESC').subscribe(response => {
                console.log(response);
                this.loadClientes(response);
            }, (error) => {
                console.log('error in', error.error.mensagem);
            });
        }
    }
  }

  public paginate(event) {
   console.log(event);
   this.page = event.page;
   this.pesquisarPage(this.typePesquisa);
  }

  public mostrarModal(clienteModal) {
    this.modal.open(clienteModal);
    return false;
  }

}
