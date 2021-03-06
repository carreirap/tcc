import { Component, OnInit, HostListener, ViewChild } from '@angular/core';
import { Empresa } from '../_models/empresa';
import { DataService, CustomInterceptor } from '../_services/http.service';
import { ToasterService } from 'angular5-toaster';
import { CpfCnpjDirective } from '../util/cnpj-cpf-validator';
import { Estados } from '../_models/TodosEstados';
import { NgModel } from '@angular/forms';


@Component({
    selector: 'app-dadosempresa-component',
    templateUrl: './dadosempresa-component.component.html',
    styleUrls: ['./dadosempresa-component.component.css']
})
export class DadosempresaComponentComponent implements OnInit {
    formEmpresa: Empresa = new Empresa();
    toasterService: ToasterService;
    diretiva: CpfCnpjDirective;
    estados: Estados = new Estados();

    @ViewChild('cpfcnpjvalidation') pwConfirmModel: NgModel;
    constructor(private service: DataService, toasterService: ToasterService) {
        this.toasterService = toasterService;
    }

    optionsSelect: Array<any>;

    ngOnInit() {
        this.optionsSelect = this.estados.getTodosEstados();

        this.service.get('/empresa').subscribe(response => {
            console.log(response);
            this.loadData(response);
        }, (error) => {
            console.log('error in', error);
            this.toasterService.pop('error', 'Empresa', error.mensagem);
        });
    }

    loadData(data: any) {
        this.formEmpresa.nome = data.nome;
        this.formEmpresa.logradouro = data.logradouro;
        this.formEmpresa.estado = data.estado;
        this.formEmpresa.complemento = data.complemento;
        this.formEmpresa.bairro = data.bairro;
        this.formEmpresa.cep = data.cep;
        this.formEmpresa.cidade = data.cidade;
        this.formEmpresa.cnpj = data.cnpj;
        this.formEmpresa.email = data.email;
        this.formEmpresa.fone = data.fone;
        this.formEmpresa.numero = data.numero;
        this.formEmpresa.site = data.site;
        this.formEmpresa.idEndereco = data.idEndereco;
    }

    onSubmit() {
        this.service.post('/empresa', this.formEmpresa).subscribe(response => {
            console.log(response);
            this.toasterService.pop('success', 'Empresa', 'Empresa alterada com sucesso!');
        }, (error) => {
            console.log('error in', error);
            this.toasterService.pop('error', 'Empresa', JSON.parse(error._body).mensagem);
        });
    }

    setValue(value: string) {
        this.formEmpresa.cnpj = value;
    }

    setCnpjCpfInvalido(mensagem: String) {
        console.log(mensagem);
        if (mensagem !== 'OK') {
            this.pwConfirmModel.control.setErrors({ 'cnpj-cpf': true });
        }
    }

}
