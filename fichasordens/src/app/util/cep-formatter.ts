import { Directive, ElementRef, Input, HostListener, Output, EventEmitter } from '@angular/core';
// import { ConverterService } from '../service/converter.service';

declare var $: any;

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[formatCep]'
})
export class CepDirective {

    // tslint:disable-next-line:no-output-on-prefix
    @Output()
    onPressEnter: EventEmitter<any> = new EventEmitter();

    @Output() cnpjvaluetyped = new EventEmitter();

    @Output() focusoutcnpj = new EventEmitter<string>();

    arrayFunction: any[] = [, 'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown']

    constructor(private el: ElementRef) {}

    // tslint:disable-next-line:use-life-cycle-interface
    ngAfterViewInit() {
        // this.input = $($(this.el.nativeElement).find('input')[0]);
    }

    @HostListener('keyup', ['$event']) onKeyUp(event: KeyboardEvent) {

        if (event.key === 'Enter') {
            this.onPressEnter.emit();
        } else if (this.arrayFunction.indexOf(event.key) < 0) {
            console.log(event);
            this.el.nativeElement.value = this.formatCep(this.el.nativeElement.value);
            this.cnpjvaluetyped.emit(this.el.nativeElement.value);
        }
    }

    
    transmitirRetorno(mensagem: any) {
        this.focusoutcnpj.emit(mensagem.mensagem);
    }

    formatCep(num) {
        if (num) {
            num = num.toString();
            num = num.replace(/\D/g, '');

            switch (num.length) {
                case 2:
                    num = num.replace(/(\d+)(\d{3})/, '$1.$2');
                    break;
                case 5:
                    num = num.replace(/(\d+)(\d{3})/, '$1.$2');
                    break;
                case 6:
                    num = num.replace(/(\d+)(\d{3})(\d{3})/, ' $1.$2-$3');
                    break;
                case 7:
                    num = num.replace(/(\d+)(\d{3})(\d{3})/, ' $1.$2-$3');
                    break;
                case 8:
                    num = num.replace(/(\d+)(\d{3})(\d{3})/, ' $1.$2-$3');
                    break;
            }
        }
        return num.trim();
    }
}
