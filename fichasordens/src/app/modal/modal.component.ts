import { ViewChild, ViewContainerRef, Injector, OnInit, Component } from '@angular/core';
import { ModalService } from './modal-service';

@Component({
    // tslint:disable-next-line:component-selector
    selector: 'modal-placeholder',
    template: '<div #modalplaceholder></div>'
})
export class ModalPlaceholderComponent implements OnInit {
    @ViewChild('modalplaceholder', {read: ViewContainerRef})
    viewContainerRef;

    constructor(
        private modalService: ModalService,
        private injector: Injector) {
    }
    ngOnInit(): void {
        this.modalService.registerViewContainerRef(this.viewContainerRef);
        this.modalService.registerInjector(this.injector);
    }
}
