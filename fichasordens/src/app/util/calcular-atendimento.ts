import { Directive, ElementRef, Input, HostListener, Output, EventEmitter, OnInit } from '@angular/core';
import { DataService } from '../_services';
// import { ConverterService } from '../service/converter.service';

declare var $: any;

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[calcularAtendimento]'
})
export class CalcularAtendimento implements OnInit {
    
    ngOnInit(): void {
        
    }
    // tslint:disable-next-line:no-output-on-prefix
    @Output()
    onPressEnter: EventEmitter<any> = new EventEmitter();

    @Output()
    PressTab: EventEmitter<any> = new EventEmitter();

    @Input('inputTextFilter') params: string;

    constructor(private el: ElementRef, private service: DataService) { }

    arrayFunction: any[] = [, 'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown']

    // tslint:disable-next-line:use-life-cycle-interface
    ngAfterViewInit() {
        // this.input = $($(this.el.nativeElement).find('input')[0]);
    }

    @HostListener('keyup', ['$event']) onKeyUp(event: KeyboardEvent) {
        console.log("key" + event.key + "  " + this.params);
        if (event.key === 'Enter') {
            this.onPressEnter.emit('calcular');
            // this.pesquisarPage(this.el.nativeElement.value, 'cnpjcpf');
        } else if (this.arrayFunction.indexOf(event.key) < 0) {
            this.PressTab.emit('nome');
            // this.pesquisarPage(this.el.nativeElement.value, 'nome');
        }
    }
}
