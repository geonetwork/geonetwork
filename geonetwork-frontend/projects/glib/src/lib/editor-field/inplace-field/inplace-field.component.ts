import {
  Component,
  computed,
  ContentChild,
  inject,
  input,
  model,
  OnInit,
  TemplateRef,
} from '@angular/core';
import { Button } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { Inplace } from 'primeng/inplace';
import { InputText } from 'primeng/inputtext';
import {
  BatchEditRequest,
  DateRangeDetails,
  RecordsApi,
  VerticalRange,
} from 'g5api';
import { RecordsApi as Gn4RecordsApi } from 'gapi';
import { EeaResourceIdentifierFieldComponent } from '../eea-resource-identifier-field/eea-resource-identifier-field.component';
import {
  API5_CONFIGURATION,
  API_CONFIGURATION,
} from '../../config/config.loader';
import { AutoFocus } from 'primeng/autofocus';
import { NgTemplateOutlet } from '@angular/common';

@Component({
  selector: 'g-inplace-field',
  imports: [
    Button,
    FormsModule,
    Inplace,
    InputText,
    EeaResourceIdentifierFieldComponent,
    AutoFocus,
    NgTemplateOutlet,
  ],
  templateUrl: './inplace-field.component.html',
})
export class InplaceFieldComponent implements OnInit {
  uuid = input.required<string>();
  property = input.required<string>();
  value = model<string | DateRangeDetails | VerticalRange | undefined>();
  initialValue: string | DateRangeDetails | VerticalRange | undefined;

  active = input<boolean>(false);

  @ContentChild('display') displayTemplate: TemplateRef<any> | undefined;
  @ContentChild('edit') editTemplate: TemplateRef<any> | undefined;

  apiConfiguration = inject(API_CONFIGURATION);
  api5Configuration = inject(API5_CONFIGURATION);

  recordsApi = computed(() => {
    return new RecordsApi(this.api5Configuration());
  });
  gn4RecordsApi = computed(() => {
    return new Gn4RecordsApi(this.apiConfiguration());
  });

  ngOnInit(): void {
    this.initialValue = this.value();
  }

  onFieldValueChange(event: DateRangeDetails | VerticalRange) {
    this.value.set(event);
  }

  saveEdit(event: Event, cb: Function) {
    let batchEditRequest: BatchEditRequest = {
      uuids: [this.uuid()],
      batchEditParameter: [
        {
          property: this.property(),
          value:
            (typeof this.value() === 'string'
              ? (this.value() as string)
              : JSON.stringify(this.value())) || '',
        },
      ],
    };
    this.recordsApi()
      .batchEdit(batchEditRequest)
      .then(response => {
        this.gn4RecordsApi().index({ uuids: [this.uuid()] });
        cb(event);
      });
  }

  cancelEdit(event: Event, cb: Function) {
    this.value.set(this.initialValue);
    cb(event);
  }
}
