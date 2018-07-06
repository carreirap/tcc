import { Directive, ElementRef, Input, HostListener, Output, EventEmitter } from '@angular/core';
import { DataService } from '../_services';
// import { ConverterService } from '../service/converter.service';

declare var $: any;

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[pequisarCliente]'
})
export class PesquisarDirective {
    @Input() pages: string;
    @Input() cnpj: String;
    @Input() nome: String;
    // tslint:disable-next-line:no-output-on-prefix
    @Output()
    onPressEnter: EventEmitter<any> = new EventEmitter();

    @Output()
    PressAnyKey: EventEmitter<any> = new EventEmitter();

    constructor(private el: ElementRef, private service: DataService) { }

    arrayFunction: any[] = [, 'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown']

    // tslint:disable-next-line:use-life-cycle-interface
    ngAfterViewInit() {
        // this.input = $($(this.el.nativeElement).find('input')[0]);
    }

    @HostListener('keyup', ['$event']) onKeyUp(event: KeyboardEvent) {
        console.log(this.pages);
        if (event.key === 'Enter') {
            this.onPressEnter.emit('cnpjcpf');
            // this.pesquisarPage(this.el.nativeElement.value, 'cnpjcpf');
        } else if (this.arrayFunction.indexOf(event.key) < 0) {
            this.PressAnyKey.emit('nome');
            // this.pesquisarPage(this.el.nativeElement.value, 'nome');
        }
    }

    /*@HostListener('click', ['$event']) onClick($event){
        console.info('clicked: ' + $event);
        if (this.cnpj !== undefined || this.cnpj === '') {
            this.pesquisarPage(this.cnpj, 'cnpjcpf');
        } else {
            this.pesquisarPage(this.nome, 'nome');
        }
    }

    public pesquisarPage( valueField, type: string) {
        if (type === 'cnpjcpf') {
            this.service.get('/cliente?cnpjcpf=' + valueField + '&page=' + this.pages).subscribe(response => {
                console.log(response);
                this.onPressEnter.emit(response);
            }, (error) => {
                console.log('error in', error.error.mensagem);
            });
        } else {
            console.log(event);
            const value = this.el.nativeElement.value;
            if (value.length > 3 ) {
                this.service.get('/cliente?nome=' + valueField + '&page=' + this.pages).subscribe(response => {
                    console.log(response);
                    this.PressAnyKey.emit(response);
                }, (error) => {
                    console.log('error in', error.error.mensagem);
                });
            }
        }
    } */
}
