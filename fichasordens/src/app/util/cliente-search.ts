import { Directive, ElementRef, Input, HostListener, Output, EventEmitter } from '@angular/core';
import { DataService } from '../_services';
// import { ConverterService } from '../service/converter.service';

declare var $: any;

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[pequisarCliente]'
})
export class PesquisarDirective {

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
        if (event.key === 'Enter') {
            this.service.get('/cliente?cnpjcpf=' + this.el.nativeElement.value).subscribe(response => {
                console.log(response);
                this.onPressEnter.emit(response);
            }, (error) => {
                console.log('error in', error.error.mensagem);
            });
        } else if (this.arrayFunction.indexOf(event.key) < 0) {
            console.log(event);
            const value = this.el.nativeElement.value;
            if (value.length > 3 ) {
                this.service.get('/cliente?nome=' + this.el.nativeElement.value).subscribe(response => {
                    console.log(response);
                    this.PressAnyKey.emit(response);
                }, (error) => {
                    console.log('error in', error.error.mensagem);
                });
            }
        }
    }
}

